package info.evelio.carbonite;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {
  // TODO Boilerplate tests here

  @Test public void testLenArray() {
    assertEquals(0, Util.len((Object[]) null));
    assertEquals(0, Util.len(new Object[]{}));

    assertEquals(1, Util.len(new Object[]{"hello"}));
  }

  @Test public void testEmptyStr() {
    assertTrue(Util.isEmpty((String) null));
    assertTrue(Util.isEmpty(""));

    assertFalse(Util.isEmpty("something"));
    assertFalse(Util.isEmpty(" "));
  }

  @Test public void testEmptyChar() {
    final char empty = ' ';
    assertTrue(Util.isEmpty(empty));

    assertFalse(Util.isEmpty('e'));
  }
}
