package info.evelio.carbonite;

import org.junit.Test;

import static info.evelio.carbonite.Carbonite.CacheType.values;
import static info.evelio.carbonite.Util.isEmpty;
import static info.evelio.carbonite.Util.len;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class KeyBuildingTest {

  @Test public void testBuildsProperlyForName() {
    final String actualKey = "key";

    Carbonite.CacheType[] types = values();
    for (Carbonite.CacheType type : types) {
      final String key = CarboniteImp.buildKey(type, actualKey);
      assertKey(type, key, actualKey);
    }
  }

  @Test public void testBuildsProperlyForClassWithoutCache() {
    Carbonite.CacheType[] types = values();
    for (Carbonite.CacheType type : types) {
      final String key = CarboniteImp.buildKey(type, Object.class, null);
      assertKey(type, key, Object.class.getName());
    }

  }

  @Test public void testBuildsProperlyForClassWithCache() {
    final CarboniteImp.KeyCache cache = new CarboniteImp.KeyCache();
    final Class daClass = Object.class;
    Carbonite.CacheType[] types = values();
    for (Carbonite.CacheType type : types) {
      final String key = CarboniteImp.buildKey(type, daClass, cache);
      assertKey(type, key, Object.class.getName());
      // Ensure cache keeps same cached keys
      assertEquals(key, cache.get(type).get(daClass));
    }
  }

  private static void assertKey(Carbonite.CacheType type, String key, String actualKey) {
    assertFalse(isEmpty(key));
    final String[] twoHalves = key.split(String.valueOf(CarboniteImp.SEPARATOR));
    assertFalse(isEmpty(twoHalves));
    assertEquals(2, len(twoHalves));
    assertEquals(String.valueOf(type.mPrefix), twoHalves[0]);
    assertEquals(actualKey, twoHalves[1]);
  }
}
