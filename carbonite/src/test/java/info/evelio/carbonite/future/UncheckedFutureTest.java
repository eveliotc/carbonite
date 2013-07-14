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
