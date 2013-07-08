package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.UnmodifiableCache;
import info.evelio.carbonite.future.Present;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Future;

import static info.evelio.carbonite.Carbonite.CacheType.MEMORY;
import static info.evelio.carbonite.Carbonite.CacheType.STORAGE;
import static info.evelio.carbonite.Carbonite.Defaults.LOAD_FACTOR;
import static info.evelio.carbonite.Util.empty;
import static info.evelio.carbonite.Util.illegalAccess;
import static info.evelio.carbonite.Util.illegalState;
import static info.evelio.carbonite.Util.len;
import static info.evelio.carbonite.Util.nonEmpty;
import static info.evelio.carbonite.Util.nonEmptyArg;
import static info.evelio.carbonite.Util.notNull;
import static info.evelio.carbonite.Util.notNullArg;
import static info.evelio.carbonite.Util.obtainValidKey;
import static info.evelio.carbonite.Util.validateKey;

/*package*/ class CarboniteImp extends Carbonite {

  private final Cache<String, Cache> mCaches;

  public CarboniteImp(Cache<String, Cache> caches) {
    mCaches = new UnmodifiableCache<String, Cache>(caches);
  }

  // both
  @Override
  public <T> Carbonite set(String key, T value) {
    if (value == null) {
      return this;
    }

    memory(key, value);

    if (cacheFor(STORAGE, value.getClass()) != null) {
      illegalState(true, "Unimplemented");
    }

    return this;
  }

  @Override
  public <T> Future<T> get(String key, Class<T> type) {
    final T memoryResult = memory(key, type);
    if (memoryResult != null) {
      return new Present(memoryResult);
    }
    illegalState(true, "Unimplemented");
    return null;
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

    final Cache<String, T> cache = (Cache<String, T>) cacheFor(cacheType, value.getClass());

    if (cache != null) {
      cache.set(key, value);
    }

    return this;
  }

  private <T> Cache<String, T> cacheFor(CacheType cacheType, Class<T> type) {
    final Cache<String, T> cache = mCaches.get( buildKey(cacheType, type) );
    return cache;
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

    return new StringBuilder(cacheType.mPrefix)
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

      return new CarboniteImp( caches );
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

}
