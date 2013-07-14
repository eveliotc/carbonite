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
