package info.evelio.carbonite.cache;

import info.evelio.carbonite.util.LruCache;

import static info.evelio.carbonite.cache.CacheType.MEMORY;

/**
 * An implementation wrapping {@link LruCache}
 * @param <K>
 * @param <V>
 */
public class MemoryLruCache<K, V> implements Cache<K, V> {
  private final LruCache<K, V> mCache;

  public MemoryLruCache(Options options) {
    mCache = new LruCache<K, V>(options.maxSize());
  }

  @Override
  public V get(K key) {
    return mCache.get(key);
  }

  @Override
  public Cache<K, V> set(K key, V value) {
    mCache.put(key, value);
    return this;
  }

  public static class Options implements CacheOptions<MemoryLruCache> {
    private final int mMaxSize;

    public Options(int maxSize) {
      mMaxSize = maxSize;
    }

    public int maxSize() {
      return mMaxSize;
    }

    @Override
    public CacheType cacheType() {
      return MEMORY;
    }

    @Override
    public Class<? extends MemoryLruCache> imp() {
      return MemoryLruCache.class;
    }
  }
}
