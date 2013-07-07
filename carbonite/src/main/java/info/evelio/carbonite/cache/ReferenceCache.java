package info.evelio.carbonite.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link HashMap} implementation of {@link MapCache}
 */
public class ReferenceCache<K, V> extends MapCache<K, V> {

  public ReferenceCache(int capacity, float loadFactor, boolean nullValues) {
    super(capacity, loadFactor, nullValues);
  }

  @Override
  protected Map<K, V> onCreateMap(int capacity, float loadFactor) {
    return new HashMap<K, V>(capacity, loadFactor);
  }
}
