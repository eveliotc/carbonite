package info.evelio.carbonite.cache;

import java.util.Map;

import static info.evelio.carbonite.util.Util.validateKey;

/**
 * A simple implementation that uses a {@link java.util.Map} to keep strong references to keys and values in Memory
 * @param <V> Type of values
 */
public abstract class MapCache<K, V> implements Cache<K, V> {
  private final Map<K, V> mCache;

  public MapCache(int capacity, float loadFactor) {
    mCache = onCreateMap(capacity, loadFactor);
  }

  protected abstract Map<K,V> onCreateMap(int capacity, float loadFactor);

  @Override
  public V get(K key) {
    validateKey(key);
    return mCache.get(key);
  }

  @Override
  public MapCache<K, V> set(K key, V value) {
    if (value == null) {
      return this;
    }

    validateKey(key);
    mCache.put(key, value);
    return this;
  }
}
