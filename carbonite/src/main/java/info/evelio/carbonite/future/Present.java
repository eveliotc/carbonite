package info.evelio.carbonite.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Present<T> implements Future<T> {
  private final T mValue;

  public Present(T value) {
    mValue = value;
  }

  @Override
  public boolean cancel(boolean b) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    return mValue;
  }

  @Override
  public T get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return mValue;
  }
}
