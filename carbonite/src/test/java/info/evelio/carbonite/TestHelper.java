package info.evelio.carbonite;

import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.UnmodifiableCache;

public class TestHelper {
  public static <K, V> ReferenceCache<K, V> gimmeReferenceCacheOfOne(boolean nullAllowed) {
    return new ReferenceCache<K, V>(1, 1, nullAllowed);
  }

  public static <K, V> UnmodifiableCache<K, V> unmodifiable(Cache<K, V> cache) {
    return new UnmodifiableCache<K, V>(cache);
  }

}
