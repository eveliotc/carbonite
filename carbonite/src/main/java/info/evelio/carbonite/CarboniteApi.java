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
import info.evelio.carbonite.cache.CacheOptions;
import info.evelio.carbonite.cache.CacheType;

import java.util.concurrent.Future;

public interface CarboniteApi {
  <T> Carbonite set(String key, T value);

  <T> Future<T> get(String key, Class<T> type);

  <T> Carbonite memory(String key, T value);

  <T> Carbonite storage(String key, T value);

  <T> T memory(String key, Class<T> type);

  <T> T storage(String key, Class<T> type);


  public interface CarboniteBuilder {
    Context context();

    CacheBuilder retaining(Class type);

    // not funny
    CarboniteBuilder iLoveYou();

    // build methods
    Carbonite iKnow();

    Carbonite build();
  }

  public interface CacheBuilder extends CarboniteBuilder {

    CacheBuilder in(CacheType type);

    CacheType cacheType();

    CacheBuilder use(CacheFactory factory);

    CacheFactory factory();

    CacheBuilder use(CacheOptions opts);

    CacheOptions opts();

    Class type();

    CarboniteBuilder builder();

    // implements CarboniteBuilder
    CacheBuilder and(CacheType type);
  }


}
