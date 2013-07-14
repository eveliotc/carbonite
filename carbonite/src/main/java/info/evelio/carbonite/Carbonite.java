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
import info.evelio.carbonite.cache.CacheFactory;
import info.evelio.carbonite.util.L;

import static info.evelio.carbonite.util.Util.nonEmptyArg;

public abstract class Carbonite implements CarboniteApi {

  /*package*/ static final class Defaults {
    public static final float LOAD_FACTOR = 0.75f;
    public static final int CAPACITY = 0;
    public static final int MAX_SIZE = 256;
    public static final CacheType TYPE = CacheType.MEMORY;
    public static final CacheFactory FACTORY = CacheFactoryImp.INSTANCE;
    public static final String STORAGE_DIRECTORY_NAME = Carbonite.class.getSimpleName();
    public static final int THREADS = 4;
  }

  public enum CacheType {
    MEMORY("m"),
    STORAGE("s");

    private final String mPrefix;

    CacheType(String prefix) {
      nonEmptyArg(prefix, "Prefix must not be empty.");

      mPrefix = prefix;
    }

    /*package*/ String getPrefix() {
      return mPrefix;
    }

  }

  public static CarboniteBuilder using(Context context) {
    return new CarboniteImp.Builder(context.getApplicationContext());
  }

  public static void setLogEnabled(boolean enabled) {
    L.setLogEnabled(enabled);
  }

}
