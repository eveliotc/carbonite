package info.evelio.carbonite.cache;

import info.evelio.carbonite.util.LruCache;

/**
 * An implementation wrapping {@link LruCache}
 * @param <K>
 * @param <V>
 */
public class MemoryLruCache<K, V> implements Cache<K, V> {
  private final LruCache<K, V> mCache;

  public MemoryLruCache(int maxSize) {
    mCache = new LruCache<K, V>(maxSize);
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
}
