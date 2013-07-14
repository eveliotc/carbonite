/*
 * Copyright 2013 Evelio Tarazona Cáceres <evelio@evelio.info>
 * Copyright 2013 Carbonite contributors <contributors@evelio.info>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.evelio.carbonite.util;

import android.util.Log;

import static info.evelio.carbonite.util.Util.notNullArg;

/**
 * @hide
 */
public final class L {
  private static boolean sLogEnabled = true;

  public static void setLogEnabled(boolean logEnabled) {
    sLogEnabled = logEnabled;
  }

  private L() {
    // No instances baby!
  }

  public static void e(String tag, String msg, Throwable tr) {
    if (sLogEnabled) {
      validate(tag, msg);
      Log.e(tag, msg, tr);
    }
  }

  public static void i(String tag, String msg) {
    if (sLogEnabled) {
      validate(tag, msg);
      Log.i(tag, msg);
    }
  }

  public static void w(String tag, String msg) {
    if (sLogEnabled) {
      validate(tag, msg);
      Log.w(tag, msg);
    }
  }

  private static void validate(String tag, String msg) {
    notNullArg(tag, "Log tag must not be null.");
    notNullArg(msg, "Log message must not be null.");
  }

}
