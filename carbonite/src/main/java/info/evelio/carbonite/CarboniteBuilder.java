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
