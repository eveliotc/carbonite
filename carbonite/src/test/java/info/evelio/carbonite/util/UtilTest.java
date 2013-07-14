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
package info.evelio.carbonite.util;

import org.junit.Test;

import static info.evelio.carbonite.util.Util.obtainValidKey;
import static info.evelio.carbonite.util.Util.validateKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UtilTest {
  // TODO Boilerplate tests here

  private static final Class<? extends Object> TARGET_CLASS = Object.class;
  private static final String GIVEN_KEY = obtainValidKey(TARGET_CLASS);

  @Test public void testInvalidStringKeys() {
    assertInvalidKey(null);
    assertInvalidKey("");
    assertInvalidKey(" ");
    assertInvalidKey("$");
    assertInvalidKey("á");
    assertInvalidKey("adot.");
  }

  @Test public void testValidStringKeys() {
    validateKey(GIVEN_KEY);
    validateKey("m_" + GIVEN_KEY);
    validateKey("yup");
    validateKey("this_is-valid");
    validateKey("n4mb3rs");
    validateKey("1234567890");
    validateKey("-____-");
  }


  private static void assertInvalidKey(String key) {
    try {
      validateKey(key);
      fail("Key " + key + " seems valid");
    } catch (Exception e) {}
    // pass
  }

  @Test public void testLenArray() {
    assertEquals(0, Util.len((Object[]) null));
    assertEquals(0, Util.len(new Object[]{}));

    assertEquals(1, Util.len(new Object[]{"hello"}));
  }

  @Test public void testEmptyStr() {
    assertTrue(Util.empty((String) null));
    assertTrue(Util.empty(""));

    assertFalse(Util.empty("something"));
    assertFalse(Util.empty(" "));
  }

  @Test public void testEmptyChar() {
    final char empty = ' ';
    assertTrue(Util.empty(empty));

    assertFalse(Util.empty('e'));
  }
}
