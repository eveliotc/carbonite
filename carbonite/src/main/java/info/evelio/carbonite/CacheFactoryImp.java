package info.evelio.carbonite;

import android.content.Context;
import com.esotericsoftware.kryo.Kryo;
import info.evelio.carbonite.cache.Cache;
import info.evelio.carbonite.cache.CacheFactory;
import info.evelio.carbonite.cache.ReferenceCache;
import info.evelio.carbonite.cache.StorageLruCache;
import info.evelio.carbonite.serialization.KryoSerializer;

import java.io.File;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.util.Util.*;

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
        return new ReferenceCache<String, T>(options.capacity(), options.loadFactor());
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
