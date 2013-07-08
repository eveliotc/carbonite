package info.evelio.carbonite.cache;

public interface Cache<K, V> {
  V get(K key);
  Cache<K, V> set(K key, V value);
}
