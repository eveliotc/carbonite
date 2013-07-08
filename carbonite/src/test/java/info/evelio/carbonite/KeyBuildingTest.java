package info.evelio.carbonite;

import org.junit.Test;

import static info.evelio.carbonite.Carbonite.CacheType.values;
import static info.evelio.carbonite.Util.empty;
import static info.evelio.carbonite.Util.len;
import static info.evelio.carbonite.Util.obtainValidKey;
import static info.evelio.carbonite.Util.validateKey;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeyBuildingTest {

  private static final Class<? extends Object> TARGET_CLASS = Object.class;
  private static final String GIVEN_KEY = obtainValidKey(TARGET_CLASS);

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
      final String key = CarboniteImp.buildKey(type, TARGET_CLASS, null);
      assertKey(type, key, GIVEN_KEY);
    }

  }

  @Test public void testBuildsProperlyForClassWithCache() {
    final CarboniteImp.KeyCache cache = new CarboniteImp.KeyCache();
    Carbonite.CacheType[] types = values();
    for (Carbonite.CacheType type : types) {
      final String key = CarboniteImp.buildKey(type, TARGET_CLASS, cache);
      assertKey(type, key, GIVEN_KEY);
      // Ensure cache keeps same cached keys
      assertEquals(key, cache.get(type).get(TARGET_CLASS));
    }
  }

  private static void assertKey(Carbonite.CacheType type, String key, String actualKey) {
    assertFalse(empty(key));
    validateKey(actualKey);
    validateKey(key);
    final String[] pieces = key.split(String.valueOf(CarboniteImp.SEPARATOR));
    assertFalse(empty(pieces));
    assertTrue(len(pieces) >= 2);
    assertEquals(String.valueOf(type.mPrefix), pieces[0]);
  }
}
