package info.evelio.carbonite;

import info.evelio.carbonite.future.UncheckedFuture;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * A bunch of boilerplate
 *
 * @hide
 */
public class Util {


  public static int len(Object[] arr) {
    return isEmpty(arr) ? 0 : arr.length;
  }

  public static int len(Collection collection) {
    return isEmpty(collection) ? 0 : collection.size();
  }


  public static int len(Map map) {
    return isEmpty(map) ? 0 : map.size();
  }


  public static int len(String str) {
    return isEmpty(str) ? 0 : str.length();
  }

  // Validation

  public static boolean isEmpty(String str) {
    return str == null || str.equals(""); // We could trim it but don't want to generate garbage
  }

  public static boolean isEmpty(Object[] arr) {
    return arr == null || arr.length < 1;
  }

  public static boolean isEmpty(Collection collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isEmpty(Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean isEmpty(char ch) {
    return Character.isWhitespace(ch);
  }


  public static void validateKey(Object key) {
    notNullArg(key, "Key must not be null.");
  }

  public static void validateKey(String key) {
    illegalArg(isEmpty(key), "Key must not be empty.");
  }

  public static <T> void validateValue(T value, boolean allowNullValues) {
    illegalArg(value == null && !allowNullValues, "null values are not allowed.");
  }

  public static void illegalArg(boolean throwIt, String msg) {
    if (throwIt) {
      throw new IllegalArgumentException(msg);
    }
  }

  public static void illegalState(boolean throwIt, String msg) {
    if (throwIt) {
      throw new IllegalStateException(msg);
    }
  }

  public static void illegalAccess(boolean throwIt, String msg) {
    if (throwIt) {
      throw new IllegalAccessError(msg);
    }
  }

  public static void notNullArg(Object bob, String msg) {
    illegalArg(bob == null, msg);
  }

  public static void notNull(Object bob, String msg) {
    if (bob == null) {
      throw new NullPointerException(msg);
    }
  }

  public static void nonEmptyArg(char ch, String msg) {
    illegalArg(isEmpty(ch), msg);
  }

  public static void nonEmptyArg(String str, String msg) {
    illegalArg(isEmpty(str), msg);
  }

  public static void nonEmptyArg(Object[] arr, String msg) {
    illegalArg(isEmpty(arr), msg);
  }

  public static void nonEmptyArg(Collection collection, String msg) {
    illegalArg(isEmpty(collection), msg);
  }

  public static void nonEmpty(char ch, String msg) {
    illegalState(isEmpty(ch), msg);
  }

  public static void nonEmpty(String str, String msg) {
    illegalState(isEmpty(str), msg);
  }

  public static void nonEmpty(Object[] arr, String msg) {
    illegalState(isEmpty(arr), msg);
  }

  public static void nonEmpty(Collection collection, String msg) {
    illegalState(isEmpty(collection), msg);
  }

  public static void nonEmpty(Map map, String msg) {
    illegalState(isEmpty(map), msg);
  }

  public static <V> UncheckedFuture<V> unchecked(Future<V> future) {
    return new UncheckedFuture(future);
  }

  public static <V> V getUnchecked(Future<V> future) {
    return unchecked(future).getUnchecked();
  }

  public static void pokeball(Throwable pokemon) {
    L.e("carbonite:pokeball", "Gotta catch 'em all!", pokemon);
  }
}
