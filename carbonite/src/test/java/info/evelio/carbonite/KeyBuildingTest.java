/*
 * Copyright 2013 Evelio Tarazona Cáceres <evelio@evelio.info>
 * Copyright 2013 Carbonite contributors <contributors@evelio.info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.evelio.carbonite;

import org.junit.Test;

import static info.evelio.carbonite.Carbonite.CacheType.values;
import static info.evelio.carbonite.util.Util.empty;
import static info.evelio.carbonite.util.Util.len;
import static info.evelio.carbonite.util.Util.obtainValidKey;
import static info.evelio.carbonite.util.Util.validateKey;
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
    assertEquals(String.valueOf(type.getPrefix()), pieces[0]);
  }
}
