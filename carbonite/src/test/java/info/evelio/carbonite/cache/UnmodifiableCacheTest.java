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
package info.evelio.carbonite.cache;

import org.junit.Test;

import static info.evelio.carbonite.util.TestHelper.gimmeReferenceCacheOfOne;
import static info.evelio.carbonite.util.TestHelper.unmodifiable;
import static org.junit.Assert.assertEquals;

public class UnmodifiableCacheTest {
  @Test(expected = Throwable.class) public void testNoNullAccepted() {
    unmodifiable(null);
  }

  @Test public void testNotNullHappy() {
    final ReferenceCache<Object, Object> notNullCache = gimmeReferenceCacheOfOne();
    unmodifiable( notNullCache );
  }

  @Test (expected = Throwable.class) public void testUnmodifiableIsUnmodifiable() {
    final ReferenceCache<Object, Object> notNullCache = gimmeReferenceCacheOfOne();
    final UnmodifiableCache<Object, Object> unmodifiable = unmodifiable(notNullCache);
    unmodifiable.set("something", "here");
  }

  @Test public void testUnmodifiableCanRetrieve() {
    final ReferenceCache<Object, Object> notNullCache = gimmeReferenceCacheOfOne();
    notNullCache.set("something", "here");
    final UnmodifiableCache<Object, Object> unmodifiable = unmodifiable(notNullCache);
    assertEquals("here", unmodifiable.get("something"));
  }
}
