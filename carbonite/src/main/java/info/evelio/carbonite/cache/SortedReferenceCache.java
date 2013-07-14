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

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A {@link java.util.TreeMap} implementation of {@link MapCache} using natural order
 */
public class SortedReferenceCache<K, V> extends MapCache<K, V> {

  public SortedReferenceCache(Options options) {
    super(options);
  }

  @Override
  protected Map<K, V> onCreateMap(MapCache.Options options) {
    final Options opts = (Options) options;
    final Comparator<K> comparator = opts.comparator();
    if (comparator == null) {
      L.w("carbonite:SortedReferenceCache", "No comparator given will use natural order.");
    }
    return new TreeMap<K, V>(comparator);
  }


  public static class Options<K> extends MapCache.Options<SortedReferenceCache> {
    private final Comparator<K> mComparator;

    public Options(Comparator<K> comparator) {
      mComparator = comparator;
    }

    public Comparator<K> comparator() {
      return mComparator;
    }

    @Override
    public CacheType cacheType() {
      return CacheType.MEMORY;
    }

    @Override
    public Class<? extends SortedReferenceCache> imp() {
      return SortedReferenceCache.class;
    }
  }

}