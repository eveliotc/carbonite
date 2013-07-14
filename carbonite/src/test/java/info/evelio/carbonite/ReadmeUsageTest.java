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
package info.evelio.carbonite;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static info.evelio.carbonite.cache.CacheType.MEMORY;
import static info.evelio.carbonite.cache.CacheType.STORAGE;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ReadmeUsageTest {
  public static final String SOMETHING = "something";
  public static final String KEY = "data";

  @Test public void buildTest() {
    Carbonite carbonite = readmeBuild();

    assertThat(carbonite).isNotNull();
  }

  @Test public void memorySetAndGetTest() {
    Carbonite carbonite = readmeBuild();

    carbonite.memory(KEY, data());

    YourPojo stored = carbonite.memory(KEY, YourPojo.class);

    assertData(stored, SOMETHING);
  }

  @Test public void storageSetAndGetTest() {
    Carbonite carbonite = readmeBuild();

    carbonite.storage(KEY, data());

    YourPojo stored = carbonite.storage(KEY, YourPojo.class);

    assertData(stored, SOMETHING);
  }

  @Test public void setAndGetTest() throws ExecutionException, InterruptedException {
    Carbonite carbonite = readmeBuild();

    carbonite.set(KEY, data());

    Future<YourPojo> future = carbonite.get(KEY, YourPojo.class);

    YourPojo stored = future.get();

    assertData(stored, SOMETHING);
  }

  @Test public void setAndGetLoadingTest() throws ExecutionException, InterruptedException, TimeoutException {
    Carbonite carbonite = readmeBuild();

    carbonite.storage(KEY, data());

    Future<YourPojo> future = carbonite.get(KEY, YourPojo.class);
    YourPojo stored = future.get();
    if (stored != null) { // we might get it right away
      assertData(stored, SOMETHING);
    } else { // we will wait up to
      stored = future.get(1, TimeUnit.SECONDS);
      assertData(stored, SOMETHING);
    }
  }

  private Carbonite readmeBuild() {
    return Carbonite.using( mockContext() ) /* getApplicationContext() is used and not retained */
        .retaining(YourPojo.class)
        .in(MEMORY) /* optional */
        .and(STORAGE) /* optional */
        /* This can be replaced by just build() */
        .iLoveYou() /* Does nothing */
        .iKnow(); // calls build()
  }


  private Context mockContext() {
    final Context context = spy(Robolectric.application);
    try {
      when(context.getCacheDir()).thenReturn( createTempDirectory() );
    } catch (Exception e) {
      throw new RuntimeException("Unable to bind getCacheDir() to a temp dir.", e);
    }
    return context;

  }

  private static YourPojo data() {
    return new YourPojo(SOMETHING);
  }

  private static void assertData(YourPojo data, String expected) {
    assertThat(data).isNotNull();
    assertThat(data.getData()).isEqualTo(expected);
  }

  public static class YourPojo {
    private final String mData;

    public YourPojo() {
      this(null);
    }

    public YourPojo(String data) {
      mData = data;
    }

    private String getData() {
      return mData;
    }

  }

  /**
   * Stolen from http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java
   */
  private static File createTempDirectory()
      throws IOException {
    final File temp;

    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

    if (!temp.delete()) {
      throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
    }

    if (!temp.mkdir()) {
      throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
    }

    return temp;
  }
}
