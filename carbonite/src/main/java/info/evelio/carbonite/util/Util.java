package info.evelio.carbonite.util;

import info.evelio.carbonite.future.Present;
import info.evelio.carbonite.future.UncheckedFuture;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bunch of boilerplate
 *
 * @hide
 */
public class Util {


  public static int len(Object[] arr) {
    return empty(arr) ? 0 : arr.length;
  }

  public static int len(Collection collection) {
    return empty(collection) ? 0 : collection.size();
  }


  public static int len(Map map) {
    return empty(map) ? 0 : map.size();
  }


  public static int len(String str) {
    return empty(str) ? 0 : str.length();
  }

  // Validation

  public static boolean empty(String str) {
    return str == null || str.equals(""); // We could trim it but don't want to generate garbage
  }

  public static boolean empty(Object[] arr) {
    return arr == null || arr.length < 1;
  }

  public static boolean empty(Collection collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean empty(Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean empty(char ch) {
    return Character.isWhitespace(ch);
  }

  /**
   * Regex stolen from DiskLruCache
   */
  /*package*/ static final Matcher LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}").matcher("");
  public static void validateKey(Object key) {
    notNullArg(key, "Key must not be null.");
  }

  public static void validateKey(String key) {
    nonEmptyArg(key, "Key must not be empty.");
    if (!LEGAL_KEY_PATTERN.reset(key).matches()) {
      // not using boolean expression above in illegalArg as we don't want to generate an String every time
      illegalArg(true, "Key must match the regex [a-z0-9_-]{1,64}. Given key " + key);
    }
  }

  public static String obtainValidKey(Class type) {
    notNullArg(type, "Type must not be null.");

    /*
    Using Locale.US as docs (https://developer.android.com/reference/java/util/Locale.html) say:

    > The best choice there is usually Locale.US – this locale is guaranteed to be available on all devices,
    > and the fact that it has no surprising special cases and is frequently used
    > (especially for computer-computer communication) means that it tends to be the most efficient choice too.
    */
    return type.getName().replace('.', '_').replace('$', '-').toLowerCase(Locale.US);
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

  public static void runtimeException(String msg, Throwable thr) {
    throw new RuntimeException(msg, thr);
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
    illegalArg(empty(ch), msg);
  }

  public static void nonEmptyArg(String str, String msg) {
    illegalArg(empty(str), msg);
  }

  public static void nonEmptyArg(Object[] arr, String msg) {
    illegalArg(empty(arr), msg);
  }

  public static void nonEmptyArg(Collection collection, String msg) {
    illegalArg(empty(collection), msg);
  }

  public static void nonEmpty(char ch, String msg) {
    illegalState(empty(ch), msg);
  }

  public static void nonEmpty(String str, String msg) {
    illegalState(empty(str), msg);
  }

  public static void nonEmpty(Object[] arr, String msg) {
    illegalState(empty(arr), msg);
  }

  public static void nonEmpty(Collection collection, String msg) {
    illegalState(empty(collection), msg);
  }

  public static void nonEmpty(Map map, String msg) {
    illegalState(empty(map), msg);
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

  public static void closeSilently(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException th) {
        L.e("carbonite:io", "Failure closing " + closeable, th);
      }
    }

  }

  public static <T> Class<T> checkedClass(T value) {
    return (Class<T>) value.getClass();
  }

  public static <T> Future<T> present(T result) {
    return new Present<T>(result);
  }

  public static ExecutorService newFixedCachedThread(int threads, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(0, threads,
        60L, TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>(),
        threadFactory);
  }
}
