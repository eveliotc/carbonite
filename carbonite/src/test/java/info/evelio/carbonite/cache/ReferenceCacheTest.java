package info.evelio.carbonite.cache;

import org.junit.Assert;
import org.junit.Test;

import static info.evelio.carbonite.TestHelper.gimmeReferenceCacheOfOne;

public class ReferenceCacheTest {

  @Test public void testSetIsRetained() {
    ReferenceCache<String, String> cache = gimmeReferenceCacheOfOne(false);
    cache.set("key", "value");

    Assert.assertEquals("value", cache.get("key"));
    Assert.assertNotEquals("different", cache.get("key"));
  }

  @Test(expected = Exception.class) public void testNotNull() {
    gimmeReferenceCacheOfOne(false).set("key", null);
  }

  @Test public void testNull() {
    gimmeReferenceCacheOfOne(true).set("key", null);
  }

}
