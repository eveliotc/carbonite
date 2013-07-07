package info.evelio.carbonite;

import android.content.Context;

public interface CarboniteBuilder {
  public Context context();
  public Options retaining(Class type);

  public CarboniteBuilder iLoveYou();
  public Carbonite iKnow();

  public interface Options {
    public Options on(Carbonite.CacheType type);
    public Options capacity(int capacity);
    public Options loadFactor(float loadFactor);
    public Options nullValues(boolean allow);

    public Options imp(Class<? extends Cache> imp);
    public Options factory(CacheFactory factory);

    public Carbonite.CacheType on();
    public int capacity();
    public float loadFactor();
    public boolean nullValues();

    public Class<? extends Cache> imp();
    public CacheFactory factory();

    public CarboniteBuilder builder();
  }
}
