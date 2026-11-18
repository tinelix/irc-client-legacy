package dev.tinelix.irc.android.legacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.PluralsRes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Global {
    @SuppressLint("SimpleDateFormat")
    public static String formatTimestamp(Context ctx, long time, String pattern) {
        String strftime;
        Date dt = new Date(time);
        long currentTime = Calendar.getInstance().getTime().getTime();
        Date dt_midnight = new Date(currentTime + 86400);

        dt_midnight.setHours(0);
        dt_midnight.setMinutes(0);
        dt_midnight.setSeconds(0);

        if(pattern == null) {
            if ((dt_midnight.getTime() - time) < 86400) {
                strftime = String.format("%s %s", ctx.getResources().getString(R.string.today_at),
                        new SimpleDateFormat("HH:mm").format(dt));
            } else if ((dt_midnight.getTime() - time) < (86400000L)) { // one day = 86400 seconds
                strftime = String.format("%s %s", ctx.getResources().getString(R.string.yesterday_at),
                        new SimpleDateFormat("HH:mm").format(dt));
            } else if ((dt_midnight.getTime() - time) < 3153600000L) { // one year = 365 days = 3,153,600 seconds
                strftime = String.format("%s %s %s", new SimpleDateFormat("d MMMM").format(dt),
                        ctx.getResources().getString(R.string.date_at),
                        new SimpleDateFormat("HH:mm").format(dt));
            } else {
                strftime = String.format("%s %s %s", new SimpleDateFormat("d MMMM yyyy").format(dt),
                        ctx.getResources().getString(R.string.date_at),
                        new SimpleDateFormat("HH:mm").format(dt));
            }
        } else {
            strftime = new SimpleDateFormat(pattern).format(dt);
        }
        return strftime;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for(int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[value >>> 4];
            hexChars[i * 2 + 1] = hexArray[value & 0x0F];
        }

        return new String(hexChars);
    }

    public static String generateAndroidCodenames() {
        switch (Build.VERSION.SDK_INT) {
            case 2:
                return "petitfour";
            case 3:
                return "cupcake";
            case 4:
                return "donut";
            case 5:
            case 6:
            case 7:
                return "eclair";
            case 8:
                return "froyo";
            case 9:
            case 10:
                return "gingerbread";
            case 11:
            case 12:
            case 13:
                return "honeycomb";
            case 14:
            case 15:
                return "ics"; // ICS -> Ice Cream Sandwich
            case 16:
            case 17:
            case 18:
                return "jellybean";
            case 19:
                return "kitkat";
            case 20:
                return "kitkat-w";
            case 21:
            case 22:
                return "lollipop";
            case 23:
                return "marshmallow";
            case 24:
            case 25:
                return "nougat";
            case 26:
            case 27:
                return "oreo";
            case 28:
                return "pie";
            case 29:
                return "quincetart";
            case 30:
                return "rvc";
            case 31:
            case 32:
                return "snowcone";
            case 33:
                return "tiramisu";
            case 34:
                return "udc";
            case 35:
                return "uic";
            case 36:
                return "baklava";
            case 37:
                return "cinnamonbun";
        }

        return "android";
    }

    public static String getPluralQuantityString(Context ctx, @PluralsRes int id, long value) {
        String qStr;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            qStr = ctx.getResources().getQuantityString(
                    id, Global.getEndNumberFromLong(value), value
            );
        } else {
            // Using patched getQuantityString() method version for Android 2.x
            IRCApplication app;
            app = ctx instanceof IRCApplication ?
                    (IRCApplication) ctx : (IRCApplication) ctx.getApplicationContext();
            qStr = app.pluralResources.getQuantityString(
                    id, Global.getEndNumberFromLong(value), value
            );
        }
        return qStr;
    }

    // Workaround for Plurals methods, because they only work with int (32-bit) numbers.
    public static int getEndNumberFromLong(long number) {
        String numberStr = Long.toString(number);
        return Integer.parseInt(numberStr.substring(numberStr.length() - 2));
    }
}
