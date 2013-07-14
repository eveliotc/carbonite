package info.evelio.carbonite.cache;

import static info.evelio.carbonite.CarboniteBuilder.Options;

public interface CacheFactory<K, T> {
  Cache<K, T> build(Options options);
}
