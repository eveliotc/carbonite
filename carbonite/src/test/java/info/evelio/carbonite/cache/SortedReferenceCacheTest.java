package info.evelio.carbonite.cache;

public class SortedReferenceCacheTest extends MapCacheTest {
  @Override
  protected MapCache<String, String> gimmeCacheOfOne(boolean nullAllowed) {
    return new SortedReferenceCache<String, String>(1, 1, nullAllowed);
  }
}
