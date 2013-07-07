package info.evelio.carbonite.cache;

import org.junit.Assert;
import org.junit.Test;

public abstract class MapCacheTest {
  @Test
  public void testSetIsRetained() {
    MapCache<String, String> cache = gimmeCacheOfOne(false);
    cache.set("key", "value");

    Assert.assertEquals("value", cache.get("key"));
    Assert.assertNotEquals("different", cache.get("key"));
  }

  @Test(expected = Exception.class) public void testNotNull() {
    gimmeCacheOfOne(false).set("key", null);
  }

  @Test public void testNull() {
    gimmeCacheOfOne(true).set("key", null);
  }

  protected abstract MapCache<String, String> gimmeCacheOfOne(boolean nullAllowed);
}
