package info.evelio.carbonite.cache;

import info.evelio.carbonite.Cache;

import java.util.Map;

import static info.evelio.carbonite.Util.validateKey;
import static info.evelio.carbonite.Util.validateValue;

/**
 * A simple implementation that uses a {@link java.util.Map} to keep strong references to keys and values in Memory
 * @param <V> Type of values
 */
public abstract class MapCache<K, V> implements Cache<K, V> {
  protected final boolean mNullValues;
  private final Map<K, V> mCache;

  public MapCache(int capacity, float loadFactor, boolean nullValues) {
    mCache = onCreateMap(capacity, loadFactor);
    mNullValues = nullValues;
  }

  protected abstract Map<K,V> onCreateMap(int capacity, float loadFactor);

  @Override
  public V get(K key) {
    validateKey(key);
    return mCache.get(key);
  }

  @Override
  public V set(K key, V value) {
    validateKey(key);
    validateValue(value, mNullValues);
    return mCache.put(key, value);
  }
}
