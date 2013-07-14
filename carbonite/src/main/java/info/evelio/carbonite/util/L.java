package info.evelio.carbonite.util;

import android.util.Log;

public class L {
  private static boolean sLogEnabled = true;

  public static void e(String tag, String msg, Throwable tr) {
    if (sLogEnabled) {
      Log.e(tag, msg, tr);
    }
  }

  public static void setLogEnabled(boolean logEnabled) {
    sLogEnabled = logEnabled;
  }
}
