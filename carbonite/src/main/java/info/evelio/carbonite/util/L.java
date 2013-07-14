package info.evelio.carbonite.util;

import android.util.Log;

public class L {
  /*package*/ static boolean sLogEnabled = true;

  public static void e(String tag, String msg, Throwable tr) {
    if (sLogEnabled) {
      Log.e(tag, msg, tr);
    }
  }
}
