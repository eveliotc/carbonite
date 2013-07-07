package info.evelio.carbonite;

public interface Cache<K, V> {
  V get(K key);
  V set(K key, V value);
}
