/*
 * Copyleft © 2022, 2026 Dmitry Tretyakov (aka. Tinelix)
 *
 * This file is part of Tinelix IRC Client for Android.
 *
 * Tinelix IRC Client is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see https://www.gnu.org/licenses/.
 *
 * Source code: https://github.com/tinelix/irc-client-legacy
 */

package dev.tinelix.irc.android.legacy.utils;

import android.app.PendingIntent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class IRCParser {

    public static final String TAG = "IRCParser";
    private boolean showTimestamp = false;

    public String parseString(String raw) {
        String[] array = raw.split(" ");
        String[] memberMsgArray = array[0].split("!");

        StringBuilder stringBuilder = new StringBuilder();
        String parsed = "";

        if(raw.startsWith("PING")) {
            Log.w(TAG, "PING messages ignored.");
            parsed = "";
        } else {

        }
        return parsed;
    }

    public String getMessageBody(String raw) {
       String[] array = raw.split(" ");
       StringBuilder stringBuilder = new StringBuilder();
       String parsed;
       if(array[1].startsWith("PRIVMSG")) {
          for(int index = 3; index < array.length; index++) {
               if(index == 3) {
                  stringBuilder.append(array[index].substring(1).replace("http//", "http://")
                       .replace("https//", "https://").replace("ftp//", "ftp://"));
               } else {
                  stringBuilder.append(" " + array[index]);
               }
          }
          parsed = stringBuilder.toString();
          Log.i("Tinelix IRC Parser", "\r\nDone!\r\n\r\nOriginal string: [" + raw + "]\r\nCode: [" + array[1] + "]");
       } else {
           parsed = "";
       }
       return parsed;
    }
}
