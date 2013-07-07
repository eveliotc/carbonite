package info.evelio.carbonite.future;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UncheckedFutureTest {

  @Test public void getUncheckedWithNullGetsNull() {
    Assert.assertEquals(null, new UncheckedFuture(null).getUnchecked());
  }

  @Test public void getUncheckedWithObjGetsObj() {
    final Object obj = new Object();
    final Future<Object> theFutureNow = new TheFutureNow(obj);

    Assert.assertEquals(obj, new UncheckedFuture(theFutureNow).getUnchecked());
  }


  private static class TheFutureNow implements Future<Object> {
    private final Object mObject;
    public TheFutureNow(Object obj) {
      mObject = obj;
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
      return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
      return mObject;
    }

    @Override
    public Object get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
      return mObject;
    }
  }
}
