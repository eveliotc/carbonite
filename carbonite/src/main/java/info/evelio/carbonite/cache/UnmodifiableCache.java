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

import static info.evelio.carbonite.util.Util.illegalAccess;
import static info.evelio.carbonite.util.Util.notNullArg;

/**
 * A {@link Cache} wrapper to make it unmodifiable
 */
public final class UnmodifiableCache<K, V> implements Cache<K, V> {
  private final Cache<K, V> mWrapped;

  public UnmodifiableCache(Cache<K, V> cache) {
    notNullArg(cache, "Given cache must not be null");

    mWrapped = cache;
  }

  @Override
  public V get(K key) {
    return mWrapped.get(key);
  }

  @Override
  public UnmodifiableCache<K, V> set(K key, V value) {
    illegalAccess(true, "You must not try to modify an UnmodifiableCache.");
    return null; // never gonna give you up
  }
}
