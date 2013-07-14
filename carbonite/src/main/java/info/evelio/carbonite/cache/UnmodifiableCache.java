package info.evelio.carbonite.cache;

import static info.evelio.carbonite.util.Util.illegalAccess;
import static info.evelio.carbonite.util.Util.notNullArg;

/**
 * A {@link Cache} wrapper to make it unmodifiable
 */
public final class UnmodifiableCache<K,V> implements Cache<K,V> {
  private final Cache<K,V> mWrapped;

  public UnmodifiableCache(Cache<K,V> cache) {
    notNullArg(cache, "Given cache must not be null");

    mWrapped = cache;
  }

  @Override
  public V get(K key) {
    return mWrapped.get(key);
  }

  @Override
  public UnmodifiableCache<K,V> set(K key, V value) {
    illegalAccess(true, "You must not try to modify an UnmodifiableCache.");
    return null; // never gonna give you up
  }
}
