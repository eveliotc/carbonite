package info.evelio.carbonite;

import android.content.Context;

import java.util.concurrent.Future;

import static info.evelio.carbonite.Util.nonEmptyArg;

public abstract class Carbonite {

  public abstract <T> Carbonite set(String key, T value);
  public abstract <T> Future<T> get(String key, Class<T> type);

  public abstract <T> Carbonite memory(String key, T value);
  public abstract <T> Carbonite storage(String key, T value);

  public abstract <T> T memory(String key, Class<T> type);
  public abstract <T> Future<T> storage(String key, Class<T> type);

  /*package*/ static final class Defaults {
    public static final boolean NULL_VALUES = false;
    public static final float LOAD_FACTOR = 0.75f;
    public static final int CAPACITY = 0;
    public static final CacheType TYPE = CacheType.MEMORY;
    public static final CacheFactory FACTORY = CacheFactoryImp.INSTANCE;
  }

  public enum CacheType {
    MEMORY('m'),
    STORAGE('s');

    /*package*/ final char mPrefix;

    CacheType(char prefix) {
      nonEmptyArg(prefix, "Prefix must not be empty.");

      mPrefix = prefix;
    }
  }

  public static CarboniteBuilder using(Context context) {
    return new CarboniteImp.Builder( context.getApplicationContext() );
  }

  public static void setLogEnabled(boolean enabled) {
    L.sLogEnabled = enabled;
  }

}
