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

public class PresentTest {
  @Test public void presentWithNullGetsNull() throws ExecutionException, InterruptedException {
    final Present present = new Present(null);
    Assert.assertEquals(null, present.get());
  }

  @Test public void presentWithObjGetsObj() throws ExecutionException, InterruptedException {
    final Object obj = new Object();
    Present present = new Present(obj);
    Assert.assertEquals(obj, present.get());
  }
}
