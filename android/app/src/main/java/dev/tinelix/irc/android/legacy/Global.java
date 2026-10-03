package dev.tinelix.irc.android.legacy;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Global {
    @SuppressLint("SimpleDateFormat")
    public static String formatTimestamp(Context ctx, long time) {
        String strftime;
        Date dt = new Date(time);
        long currentTime = Calendar.getInstance().getTime().getTime();
        Date dt_midnight = new Date(currentTime + 86400);

        dt_midnight.setHours(0);
        dt_midnight.setMinutes(0);
        dt_midnight.setSeconds(0);

        if((dt_midnight.getTime() - time) < 86400) {
            strftime = String.format("%s %s", ctx.getResources().getString(R.string.today_at),
                    new SimpleDateFormat("HH:mm").format(dt));
        } else if((dt_midnight.getTime() - time) < (86400000L)) { // one day = 86400 seconds
            strftime = String.format("%s %s", ctx.getResources().getString(R.string.yesterday_at),
                    new SimpleDateFormat("HH:mm").format(dt));
        } else if((dt_midnight.getTime() - time) < 3153600000L) { // one year = 365 days = 3,153,600 seconds
            strftime = String.format("%s %s %s", new SimpleDateFormat("d MMMM").format(dt),
                    ctx.getResources().getString(R.string.date_at),
                    new SimpleDateFormat("HH:mm").format(dt));
        } else {
            strftime = String.format("%s %s %s", new SimpleDateFormat("d MMMM yyyy").format(dt),
                    ctx.getResources().getString(R.string.date_at),
                    new SimpleDateFormat("HH:mm").format(dt));
        }
        return strftime;
    }
}
