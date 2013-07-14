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
import com.esotericsoftware.kryo.Kryo;
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.CacheFactory;
import info.evelio.carbonite.cache.MemoryLruCache;
import info.evelio.carbonite.cache.StorageLruCache;
import info.evelio.carbonite.serialization.KryoSerializer;

import java.io.File;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Carbonite.Defaults.MAX_SIZE;
import static info.evelio.carbonite.util.Util.illegalState;
import static info.evelio.carbonite.util.Util.notNull;
import static info.evelio.carbonite.util.Util.notNullArg;
import static info.evelio.carbonite.util.Util.obtainValidKey;

/*package*/ class CacheFactoryImp<T> implements CacheFactory<String, T> {
  /*package*/ static final CacheFactoryImp INSTANCE = new CacheFactoryImp();

  @Override
  public Cache<String, T> build(CarboniteBuilder.Options options) {
    notNullArg(options, "Invalid options given.");

    if (options.imp() != null) {
      illegalState(true, "Implementation instantiation not yet implemented.");
    }

    final CacheType cacheType = options.in();

    switch (cacheType) {
      case MEMORY:
        // TODO actually define Options per cache implementation so we don't call capacity mixed units
        final int maxSize = Math.max(MAX_SIZE, options.capacity());
        return new MemoryLruCache<String, T>(maxSize);
      case STORAGE:
        final Class type = options.retaining();
        return new StorageLruCache<T>(
            buildCacheDir(options.context(), type),
            options.capacity(),
            type,
            new KryoSerializer<T>(new Kryo(), type)); // TODO yikes a builder or something
      default:
        illegalState(true, "Not yet implemented cache type " + cacheType);
        return null;
    }
  }

  private static File buildCacheDir(Context context, Class type) {
    File cacheDir = context.getCacheDir();
    notNull(cacheDir, "context.getCacheDir() returned null.");

    cacheDir = new File(cacheDir, Carbonite.Defaults.STORAGE_DIRECTORY_NAME);
    return new File(cacheDir, obtainValidKey(type));
  }
}
