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

import info.evelio.carbonite.util.Util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * @hide
 */
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
