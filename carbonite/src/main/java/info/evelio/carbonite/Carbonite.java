package info.evelio.carbonite;

import android.content.Context;
import info.evelio.carbonite.cache.CacheFactory;

import static info.evelio.carbonite.Util.nonEmptyArg;

public abstract class Carbonite implements CarboniteApi {

  /*package*/ static final class Defaults {
    public static final float LOAD_FACTOR = 0.75f;
    public static final int CAPACITY = 0;
    public static final CacheType TYPE = CacheType.MEMORY;
    public static final CacheFactory FACTORY = CacheFactoryImp.INSTANCE;
    public static final String STORAGE_DIRECTORY_NAME = Carbonite.class.getSimpleName();
  }

  public enum CacheType {
    MEMORY("m"),
    STORAGE("s");

    /*package*/ final String mPrefix;

    CacheType(String prefix) {
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
