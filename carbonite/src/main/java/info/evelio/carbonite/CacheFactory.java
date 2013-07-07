package info.evelio.carbonite;

import static info.evelio.carbonite.CarboniteBuilder.Options;

public interface CacheFactory<K, T> {
  public Cache<K, T> build(Options options);
}
