/*
 * Copyright 2013 Evelio Tarazona Cáceres <evelio@evelio.info>
 * Copyright 2013 Carbonite contributors <contributors@evelio.info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.UnmodifiableCache;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.setThreadPriority;
import static info.evelio.carbonite.Carbonite.CacheType.MEMORY;
import static info.evelio.carbonite.Carbonite.CacheType.STORAGE;
import static info.evelio.carbonite.Carbonite.Defaults.LOAD_FACTOR;
import static info.evelio.carbonite.Carbonite.Defaults.THREADS;
import static info.evelio.carbonite.util.Util.checkedClass;
import static info.evelio.carbonite.util.Util.empty;
import static info.evelio.carbonite.util.Util.illegalAccess;
import static info.evelio.carbonite.util.Util.illegalArg;
import static info.evelio.carbonite.util.Util.len;
import static info.evelio.carbonite.util.Util.newFixedCachedThread;
import static info.evelio.carbonite.util.Util.nonEmpty;
import static info.evelio.carbonite.util.Util.nonEmptyArg;
import static info.evelio.carbonite.util.Util.notNull;
import static info.evelio.carbonite.util.Util.notNullArg;
import static info.evelio.carbonite.util.Util.obtainValidKey;
import static info.evelio.carbonite.util.Util.present;
import static info.evelio.carbonite.util.Util.validateKey;


/*package*/ class CarboniteImp extends Carbonite {

  private final Cache<String, Cache> mCaches;
  private final ExecutorService mExecutor; // TODO this should be static

  /*package*/ CarboniteImp(Cache<String, Cache> caches, ExecutorService executor) {
    mCaches = new UnmodifiableCache<String, Cache>(caches);
    mExecutor = executor;
  }

  // both
  @Override
  public <T> Carbonite set(String key, T value) {
    if (value == null) {
      return this;
    }

    memory(key, value);

    later(ACTION.SET, key, value, cacheFor(STORAGE, checkedClass(value)));

    return this;
  }

  @Override
  public <T> Future<T> get(String key, Class<T> type) {

    final T memoryResult = memory(key, type);

    if (memoryResult != null) {
      return present(memoryResult);
    }

    return later(ACTION.GET, key, null, cacheFor(STORAGE, type));
  }

  // storage

  @Override
  public <T> Carbonite storage(String key, T value) {
    return doSet(key, value, STORAGE);
  }

  @Override
  public <T> T storage(String key, Class<T> type) {
    return doGet(key, type, STORAGE);
  }

  // memory

  @Override
  public <T> Carbonite memory(String key, T value) {
    return doSet(key, value, MEMORY);
  }

  @Override
  public <T> T memory(String key, Class<T> type) {
    return doGet(key, type, MEMORY);
  }

  private <T> T doGet(String key, Class<T> type, CacheType cacheType) {
    validateKey(key);

    final Cache<String, T> cache = cacheFor(cacheType, type);

    if (cache == null) {
      return null;
    }

    return cache.get(key);
  }

  private <T> Carbonite doSet(String key, T value, CacheType cacheType) {
    if (value == null) {
      return this;
    }

    validateKey(key);

    final Cache<String, T> cache = cacheFor(cacheType, checkedClass(value));

    if (cache != null) {
      cache.set(key, value);
    }

    return this;
  }

  private <T> Cache<String, T> cacheFor(CacheType cacheType, Class<T> type) {
    final Cache<String, T> cache = mCaches.get(buildKey(cacheType, type));
    return cache;
  }

  // Async stuff
  private enum ACTION { SET, GET };

  private <T> Future<T> later(ACTION action, String key, T value, Cache<String, T> cache) {
    notNullArg(action, "No action given.");

    // Optimization: don't validate or send to execution if we are setting null
    if (value == null && action == ACTION.SET) {
      return null; // EEE: We don't use futures for set, we might at some point when listeners are needed
    }

    validateKey(key);

    // TODO allow only unique ACTIONS, if we are setting/getting certain key multiple times submit just once
    switch (action) {
      case SET:
        return (Future<T>) mExecutor.submit(new SetTask(key, value, cache));
      case GET:
        return mExecutor.submit(new GetTask(key, cache));
      default:
        illegalArg(true, "Unknown action " + action);
        return null;
    }

  }

  private static final class SetTask<T> implements Runnable {
    private final String mKey;
    private final T mValue;
    private final Cache<String, T>  mCache;

    private SetTask(String key, T value, Cache<String, T> cache) {
      mKey = key;
      mValue = value;
      mCache = cache;
    }

    @Override
    public void run() {
      mCache.set(mKey, mValue);
    }
  }

  private static final class GetTask<T> implements Callable<T> {
    private final String mKey;
    private final Cache<String, T>  mCache;

    private GetTask(String key, Cache<String, T> cache) {
      mKey = key;
      mCache = cache;
    }

    @Override
    public T call() throws Exception {
      return mCache.get(mKey);
    }
  }

  // Building stuff
  private static KeyCache sKeyCache;
  /*package*/ static final char SEPARATOR = '_';

  private static String buildKey(CacheType cacheType, Class type) {
    if (sKeyCache == null) {
      sKeyCache = new KeyCache();
    }

    return buildKey(cacheType, type, sKeyCache);
  }

  /*package*/ static String buildKey(CacheType cacheType, Class type, KeyCache cacheKeys) {
    notNullArg(cacheType, "Cache type must not be null");
    notNullArg(type, "Class must not be null");

    String key;
    if (cacheKeys != null) {
      final Cache<Class, String> typeCache = cacheKeys.get(cacheType);
      key = typeCache.get(type);
      if (empty(key)) {
        key = buildClassKey(cacheType, type);
        typeCache.set(type, key);
      }
    } else {
      key = buildClassKey(cacheType, type);
    }

    return key;
  }

  private static String buildClassKey(CacheType cacheType, Class type) {
    return buildKey(cacheType, obtainValidKey(type));
  }

  /*package*/ static String buildKey(CacheType cacheType, String givenKey) {
    notNullArg(cacheType, "Cache type must not be null");
    nonEmptyArg(givenKey, "Given key must not be empty.");

    return new StringBuilder(cacheType.getPrefix())
        .append(SEPARATOR)
        .append(givenKey).toString();
  }

  /*package*/ static class Builder implements CarboniteBuilder {
    private final Context mContext;
    private Set<Options> mOptions;

    public Builder(Context applicationContext) {
      notNullArg(applicationContext, "Context must not be null.");

      mContext = applicationContext;
    }

    @Override
    public Context context() {
      return mContext;
    }

    @Override
    public Options retaining(Class type) {
      notNullArg(type, "Class must not be null");

      if (mOptions == null) {
        mOptions = new LinkedHashSet<Options>(1, LOAD_FACTOR);
      }

      BaseOptions options = new BaseOptions(this, type);
      mOptions.add(options);
      return options;
    }

    @Override
    public CarboniteBuilder iLoveYou() {
      return this;
    }

    @Override
    public Carbonite iKnow() {
      return build();
    }

    @Override
    public Carbonite build() {
      nonEmpty(mOptions, "You must specify types you will cache.");

      final int length = len(mOptions);

      // This is where we set all our caches
      final Cache<String, Cache> caches = new ReferenceCache<String, Cache>(length, 1);

      // For every retained class
      for (final Options options : mOptions) {
        final Class type = options.retaining();
        final CacheType cacheType = options.in();

        // try to built with given options
        final Cache built = options.factory().build(options);
        notNull(built, "Failure building cache");

        caches.set(buildKey(cacheType, type), built); // alrite let's cache it!
      }

      return new CarboniteImp(caches, newFixedCachedThread(THREADS, new CarboniteThreadFactory()));
    }

  }

  /*package*/ static class KeyCache implements Cache<CacheType, Cache<Class, String>> {
    private final ReferenceCache<CacheType, Cache<Class, String>> mRealCache;

    KeyCache() {
      mRealCache = new ReferenceCache<CacheType, Cache<Class, String>>(CacheType.values().length, 1);
    }

    @Override
    public Cache<Class, String> get(CacheType key) {
      validateKey(key);

      Cache<Class, String> value = mRealCache.get(key);
      if (value == null) {
        value = new ReferenceCache<Class, String>(1, LOAD_FACTOR);
        mRealCache.set(key, value);
      }
      return value;
    }

    @Override
    public Cache<CacheType, Cache<Class, String>> set(CacheType key, Cache<Class, String> value) {
      illegalAccess(true, "Set is not supported as internal values are lazy loaded.");
      return null;
    }
  }

  /*package*/ static class CarboniteThreadFactory implements ThreadFactory {

    @NotNull
    @Override
    public Thread newThread(Runnable r) {
      return new CarboniteThread(r);
    }
  }

  /*package*/ static class CarboniteThread extends Thread {
    public CarboniteThread(Runnable r) {
      super(r);
      setName("CarboniteBg");
    }

    @Override
    public void run() {
      setThreadPriority(THREAD_PRIORITY_BACKGROUND);
      super.run();
    }
  }

}
