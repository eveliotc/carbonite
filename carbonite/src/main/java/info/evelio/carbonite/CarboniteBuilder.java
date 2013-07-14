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
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.CacheFactory;

public interface CarboniteBuilder {
  Context context();
  Options retaining(Class type);

  // not funny
  CarboniteBuilder iLoveYou();
  // build methods
  Carbonite iKnow();
  Carbonite build();

  public interface Options extends CarboniteBuilder {
    Options in(Carbonite.CacheType type);

    Options capacity(int capacity);
    Options loadFactor(float loadFactor);

    Options imp(Class<? extends Cache> imp);
    Options factory(CacheFactory factory);

    Carbonite.CacheType in();
    int capacity();
    float loadFactor();

    Class<? extends Cache> imp();
    CacheFactory factory();

    CarboniteBuilder builder();
    Class<? extends Object> retaining();

    /**
     * Note: Must spawn a new Options for current retained class instead of return this
     */
    Options and(Carbonite.CacheType type);
  }
}
