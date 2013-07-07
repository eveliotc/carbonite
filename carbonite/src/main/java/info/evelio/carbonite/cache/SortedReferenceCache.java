package info.evelio.carbonite.cache;

import java.util.Map;
import java.util.TreeMap;

/**
 * A {@link java.util.TreeMap} implementation of {@link MapCache} using natural order
 */
public class SortedReferenceCache<K, V> extends MapCache<K, V> {

  public SortedReferenceCache(int capacity, float loadFactor, boolean nullValues) {
    super(capacity, loadFactor, nullValues);
  }

  @Override
  protected Map<K, V> onCreateMap(int capacity, float loadFactor) {
    return new TreeMap<K, V>();
  }
}