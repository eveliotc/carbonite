package info.evelio.carbonite.future;

import info.evelio.carbonite.util.Util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UncheckedFuture<V> implements Future<V> {
  private final Future<V> mWrapped;

  public UncheckedFuture(Future<V> future) {
    mWrapped = future;
  }

  @Override
  public boolean cancel(boolean b) {
    if (mWrapped != null) {
      return mWrapped.cancel(b);
    }
    return false;
  }

  @Override
  public boolean isCancelled() {
    if (mWrapped != null) {
      return mWrapped.isCancelled();
    }
    return false;
  }

  @Override
  public boolean isDone() {
    if (mWrapped != null) {
      return mWrapped.isDone();
    }
    return false;
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    if (mWrapped != null) {
      return mWrapped.get();
    }
    return null;
  }

  @Override
  public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    if (mWrapped != null) {
      return mWrapped.get(l, timeUnit);
    }
    return null;
  }

  public V getUnchecked() {
    try {
      return get();
    } catch (Throwable pokemon) {
      Util.pokeball(pokemon);
    }
    return null;
  }

  public V getUnchecked(long l, TimeUnit timeUnit) {
    try {
      return get(l, timeUnit);
    } catch (Throwable pokemon) {
      Util.pokeball(pokemon);
    }
    return null;
  }

}
