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

import static info.evelio.carbonite.util.Util.validateKey;

/**
 * A simple implementation that uses a {@link java.util.Map} to keep strong references to keys and values in Memory
 * @param <V> Type of values
 */
public abstract class MapCache<K, V> implements Cache<K, V> {
  private final Map<K, V> mCache;

  public MapCache(int capacity, float loadFactor) {
    mCache = onCreateMap(capacity, loadFactor);
  }

  protected abstract Map<K, V> onCreateMap(int capacity, float loadFactor);

  @Override
  public V get(K key) {
    validateKey(key);
    return mCache.get(key);
  }

  @Override
  public MapCache<K, V> set(K key, V value) {
    if (value == null) {
      return this;
    }

    validateKey(key);
    mCache.put(key, value);
    return this;
  }
}
