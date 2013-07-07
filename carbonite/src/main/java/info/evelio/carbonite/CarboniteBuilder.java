package info.evelio.carbonite;

import android.content.Context;

public interface CarboniteBuilder {
  public Context context();
  public Options retaining(Class type);

  // not funny
  public CarboniteBuilder iLoveYou();
  // build methods
  public Carbonite iKnow();
  public Carbonite build();

  public interface Options extends CarboniteBuilder {
    public Options in(Carbonite.CacheType type);

    public Options capacity(int capacity);
    public Options loadFactor(float loadFactor);
    public Options nullValues(boolean allow);

    public Options imp(Class<? extends Cache> imp);
    public Options factory(CacheFactory factory);

    public Carbonite.CacheType in();
    public int capacity();
    public float loadFactor();
    public boolean nullValues();

    public Class<? extends Cache> imp();
    public CacheFactory factory();

    public CarboniteBuilder builder();
    public Class<? extends Object> retaining();

    /**
     * Note: Must spawn a new Options for current retained class instead of return this
     */
    public Options and(Carbonite.CacheType type);
  }
}
