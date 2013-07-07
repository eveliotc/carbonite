package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.UnmodifiableCache;
import info.evelio.carbonite.future.Present;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import static info.evelio.carbonite.Carbonite.Defaults.*;
import static info.evelio.carbonite.CarboniteBuilder.Options;
import static info.evelio.carbonite.Util.*;
import static info.evelio.carbonite.Util.nonEmptyArg;

/*package*/ class CarboniteImp extends Carbonite {

  private final Cache<Class, Cache> mCaches;

  public CarboniteImp(Cache<Class, Cache> caches) {
    mCaches = new UnmodifiableCache<Class, Cache>(caches);
  }

  @Override
  public <T> Carbonite set(String key, T value) {
    illegalState(true, "Unimplemented");
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

  @Override
  public <T> Carbonite storage(String key, T value) {
    return this;
  }

  @Override
  public <T> Future<T> storage(String key, Class<T> type) {
    return null;
  }

  // Memory

  @Override
  public <T> Carbonite memory(String key, T value) {
    validateKey(key);

    notNull(value, "Unable to determinate type of null value.");

    final Cache<String, T> cache = (Cache<String, T>) cacheFor( value.getClass() );
    cache.set(key, value);

    return this;
  }

  @Override
  public <T> T memory(String key, Class<T> type) {
    validateKey(key);

    return cacheFor(type).get(key);
  }

  private <T> Cache<String, T> cacheFor(Class<T> type) {
    notNullArg(type, "Type must not be null");

    final Cache<String, T> cache = mCaches.get(type);
    notNull(cache, "Cache for given type is null, did you include it when retaining(Class)?.");

    return cache;
  }

  // Building stuff

  private static KeyCache sKeyCache;

  /*package static*/ enum CacheType {
    MEMORY('m'),
    STORAGE('s');
    private static final char SEPARATOR = ':';

    private final char mKeyName;

    CacheType(char keyName) {
      nonEmptyArg(keyName, "Key name must not be empty.");

      mKeyName = keyName;
    }

    public String buildKey(Class type) {
      notNullArg(type, "Class must not be null");

      if (sKeyCache == null) {
        sKeyCache = new KeyCache();
      }

      final Cache<Class, String> typeCache = sKeyCache.get(this);
      String key = typeCache.get(type);
      if (isEmpty(key)) {
        key = buildKey( type.getName() );
        typeCache.set(type, key);
      }

      return key;
    }

    public String buildKey(String givenKey) {
      nonEmptyArg(givenKey, "Given key must not be empty.");

      return new StringBuilder(mKeyName)
          .append(SEPARATOR)
          .append(givenKey).toString();
    }

  };

  /*package*/ static class DefaultCacheFactory {
    public static <T> Cache buildFor(Options options, CacheType type) {
      switch (type) {
        case MEMORY:
          return new ReferenceCache<String, T>(options.inMemory(), LOAD_FACTOR, options.nullValues());
        case STORAGE:
        default:
          Util.illegalState(true, "Not yet implemented cache type " + type);
          return null;
      }
    }
  }


  /*package*/ static class Builder implements CarboniteBuilder {
    private final Context mContext;
    private Map<Class, Options> mClasses;

    public Builder(Context applicationContext) {
      notNullArg(applicationContext, "Context must not be null.");

      mContext = applicationContext;
    }

    @Override
    public Options retaining(Class type) {
      notNullArg(type, "Class must not be null");

      if (mClasses == null) {
        mClasses = new LinkedHashMap<Class, Options>(1, LOAD_FACTOR);
      }

      DefaultOptions options = new DefaultOptions(this);
      mClasses.put(type, options);
      return options;
    }

    @Override
    public CarboniteBuilder iLoveYou() {
      return this;
    }

    @Override
    public Carbonite iKnow() {
      nonEmpty(mClasses, "You must specify types you will cache.");

      final int length = len(mClasses);
      final Set<Map.Entry<Class,Options>> entries = mClasses.entrySet();

      // This is were we set all our caches
      final Cache<Class, Cache> caches = new ReferenceCache<Class, Cache>(length, 1, false);

      // For every retained class
      for (Map.Entry<Class,Options> entry : entries) {
        final Class key = entry.getKey();
        final Options options = entry.getValue();
        // For every cache type
        for (CacheType cacheType : CacheType.values()) {
          // try to built with given options
          final Cache built = DefaultCacheFactory.buildFor( options, cacheType );
          if (built != null) { // success?
            caches.set( key, built ); // alrite let's cache it!
          }
        }
      }

      return new CarboniteImp( caches );
    }

  }

  /*package*/ static class DefaultOptions implements Options {
    private final CarboniteBuilder mCarboniteBuilder;
    /*package*/ int mMemory = MEMORY_VALUE;
    /*package*/ int mStorage = STORAGE_VALUE;
    /*package*/ boolean mNullValues = NULL_VALUES;

    public DefaultOptions(CarboniteBuilder carboniteBuilder) {
      notNullArg(carboniteBuilder, "Builder must not be null.");

      mCarboniteBuilder = carboniteBuilder;
    }

    @Override
    public Options inMemory(int capacity) {
      illegalArg(capacity < 1, "If you don't want objects in memory, why are you even using this?");

      mMemory = capacity;
      return this;
    }

    @Override
    public Options inStorage(int capacity) {
      mStorage = capacity;
      return this;
    }

    @Override
    public Options nullValues(boolean allow) {
      mNullValues = allow;
      return this;
    }

    @Override
    public CarboniteBuilder plus() {
      return mCarboniteBuilder;
    }

    @Override
    public CarboniteBuilder and() {
      return mCarboniteBuilder;
    }

    @Override
    public int inMemory() {
      return mMemory;
    }

    @Override
    public int inStorage() {
      return mStorage;
    }

    @Override
    public boolean nullValues() {
      return mNullValues;
    }
  }

  /*package*/ static class KeyCache implements Cache<CacheType, Cache<Class, String>> {
    private final ReferenceCache<CacheType, Cache<Class, String>> mRealCache;

    KeyCache() {
      mRealCache = new ReferenceCache<CacheType, Cache<Class, String>>(CacheType.values().length, 1, false);
    }

    @Override
    public Cache<Class, String> get(CacheType key) {
      validateKey(key);

      Cache<Class, String> value = mRealCache.get(key);
      if (value == null) {
        value = new ReferenceCache<Class, String>(1, LOAD_FACTOR, false);
        mRealCache.set(key, value);
      }
      return value;
    }

    @Override
    public Cache<Class, String> set(CacheType key, Cache<Class, String> value) {
      illegalAccess(true, "Set is not supported as internal values are lazy loaded.");
      return null;
    }
  }

}
