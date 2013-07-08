package info.evelio.carbonite.cache;

import static info.evelio.carbonite.TestHelper.gimmeReferenceCacheOfOne;

public class ReferenceCacheTest extends MapCacheTest {

  @Override
  protected MapCache<String, String> gimmeCacheOfOne() {
    return gimmeReferenceCacheOfOne();
  }

}
