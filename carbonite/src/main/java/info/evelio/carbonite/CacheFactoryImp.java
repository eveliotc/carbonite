package info.evelio.carbonite;

import info.evelio.carbonite.cache.ReferenceCache;

import static info.evelio.carbonite.Carbonite.CacheType;
import static info.evelio.carbonite.Util.illegalState;
import static info.evelio.carbonite.Util.notNullArg;

/*package*/ class CacheFactoryImp<K, T> implements CacheFactory<K, T> {
  /*package*/ static final CacheFactoryImp INSTANCE = new CacheFactoryImp();

  @Override
  public Cache<K, T> build(CarboniteBuilder.Options options) {
    notNullArg(options, "Invalid options given.");

    if (options.imp() != null) {
      illegalState(true, "Implementation instantiation not yet implemented.");
    }

    final CacheType type = options.in();

    switch (type) {
      case MEMORY:
        return new ReferenceCache<K, T>(options.capacity(), options.loadFactor(), options.nullValues());
      case STORAGE:
      default:
        illegalState(true, "Not yet implemented cache type " + type);
        return null;
    }
  }
}
