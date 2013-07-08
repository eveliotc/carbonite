package info.evelio.carbonite.cache;

import org.junit.Assert;
import org.junit.Test;

public abstract class MapCacheTest {
  @Test
  public void testSetIsRetained() {
    MapCache<String, String> cache = gimmeCacheOfOne();
    cache.set("key", "value");

    Assert.assertEquals("value", cache.get("key"));
    Assert.assertNotEquals("different", cache.get("key"));
  }

  @Test public void testNullNoOp() {
    gimmeCacheOfOne().set("key", null);
  }

  protected abstract MapCache<String, String> gimmeCacheOfOne();
}
