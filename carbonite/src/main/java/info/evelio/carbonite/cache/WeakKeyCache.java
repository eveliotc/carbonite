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

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A cache that uses {@link WeakHashMap} ensuring to release keys and values when keys are GC approachable
 * @param <K>
 * @param <V>
 */
public class WeakKeyCache<K, V> extends MapCache<K, V> {

  public WeakKeyCache(Options opts) {
    super(opts);
  }

  @Override
  protected Map<K, V> onCreateMap(MapCache.Options options) {
    final Options opts = (Options) options;
    return new WeakHashMap<K, V>(opts.initialCapacity(), opts.loadFactor());
  }

  public static class Options extends MapCache.CapacityLoadOptions<WeakKeyCache> {

    public Options(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
    }

    @Override
    public Class<? extends WeakKeyCache> imp() {
      return WeakKeyCache.class;
    }
  }
}
