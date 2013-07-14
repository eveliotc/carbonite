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
package info.evelio.carbonite.cache;

import info.evelio.carbonite.util.L;

import java.util.HashMap;
import java.util.Map;

import static info.evelio.carbonite.cache.CacheType.MEMORY;

/**
 * A {@link HashMap} implementation of {@link MapCache}
 */
public class ReferenceCache<K, V> extends MapCache<K, V> {
  public static final float DEFAULT_LOAD_FACTOR = 0.75F;

  public ReferenceCache(Options opts) {
    super(opts);
  }

  @Override
  protected Map<K, V> onCreateMap(MapCache.Options options) {
    final Options opts = (Options) options;
    return new HashMap<K, V>(opts.initialCapacity(), opts.loadFactor());
  }


  public static class Options extends MapCache.Options<ReferenceCache> {
    private final int mInitialCapacity;
    private final float mLoadFactor;

    public Options(int initialCapacity, float loadFactor) {
      if (initialCapacity < 0) {
        L.i("carbonite:ReferenceCache", "Using initial capacity of 0 as you didn't provide a valid one.");
        initialCapacity = 0;
      }
      mInitialCapacity = initialCapacity;
      if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
        loadFactor = DEFAULT_LOAD_FACTOR;
        L.i("carbonite:ReferenceCache",
            String.format("Using %.2f as load factor as you didn't provide a valid one.", DEFAULT_LOAD_FACTOR));
      }
      mLoadFactor = loadFactor;
    }


    public int initialCapacity() {
      return mInitialCapacity;
    }

    public float loadFactor() {
      return mLoadFactor;
    }

    @Override
    public CacheType cacheType() {
      return MEMORY;
    }

    @Override
    public Class<? extends ReferenceCache> imp() {
      return ReferenceCache.class;
    }
  }

}
