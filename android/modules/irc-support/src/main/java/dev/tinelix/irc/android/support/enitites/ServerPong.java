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

package dev.tinelix.irc.android.support.enitites;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Locale;

import dev.tinelix.irc.android.support.R;

public class ServerPong extends IRCBroadcastSignal {

    public long value;
    public long prevValue;
    public long uptime;

    public ServerPong(boolean isIncoming) {
        super(IN_CMD_PONG, isIncoming);
        mIsFinished = true;
        value = -1;
        prevValue = -1;
    }

    @Override
    public boolean parseContent(String rawContent) {
        body = "Pong!";
        type = IN_CMD_PONG;
        mIsFinished = true;
        return true;
    }

    @Override
    public SpannableString getLocalizedSpannedBody(Context context, int highlightColor) {

        if(value <= 0) return new SpannableString("");

        Resources res = context.getResources();

        String uptimeTc = getUptimeTimecode();

        String localizedBody;
        SpannableString spannedBody;

        String[] qualityArray = res.getStringArray(R.array.ircs_connection_qualities);

        if(value >= 5000) {
            if(value >= prevValue * 1.5 || value <= prevValue / 1.5)
                localizedBody = res.getString(
                        R.string.ircs_signal_ping_2, qualityArray[3],
                        value, uptimeTc
                );
            else
                localizedBody = res.getString(
                        R.string.ircs_signal_ping_2,
                        value >= 2000 ? qualityArray[3] : qualityArray[4],
                        value, uptimeTc
                );
        } else if(value >= 1000) {
            if(value >= prevValue * 1.5 || value <= prevValue / 1.5)
                localizedBody = res.getString(
                    R.string.ircs_signal_ping_2, qualityArray[3],
                    value, uptimeTc
                );
            else
                localizedBody = res.getString(
                    R.string.ircs_signal_ping_2,
                    value >= 2000 ? qualityArray[3] : qualityArray[2],
                    value, uptimeTc
                );
        } else if(value >= 200) {
            if(value >= 500 && (value >= prevValue * 1.5 || value <= prevValue / 1.5))
                localizedBody = res.getString(
                        R.string.ircs_signal_ping, qualityArray[3],
                        value, uptimeTc
                );
            else
                localizedBody = res.getString(
                        R.string.ircs_signal_ping, qualityArray[1],
                        value, uptimeTc
                );
        } else {
            localizedBody = res.getString(
                    R.string.ircs_signal_ping, qualityArray[0],
                    value, uptimeTc
            );
        }

        spannedBody = new SpannableString(localizedBody);

        spannedBody.setSpan(
                new StyleSpan(Typeface.BOLD), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannedBody.setSpan(
                new ForegroundColorSpan(highlightColor), 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannedBody;
    }

    public void setPingValues(int currentPing, int prevPing) {
        value = currentPing;
        prevValue = prevPing;
        mIsFinished = true;
    }

    public String getUptimeTimecode() {

        String timecode;
        long minute = 60 * 1000;
        long hour = 60 * minute;
        long day = 24 * hour;

        String pattern;

        if(uptime >= day)
            pattern = "%d:%02d:%02d:%02d";
        else if(uptime >= hour)
            pattern = "%d:%02d:%02d";
        else
            pattern = "%d:%02d";

        if(uptime >= day)
            timecode = String.format(
                    Locale.getDefault(), pattern,
                    day,
                    (uptime / hour) % 24,    // hours
                    (uptime / minute) % 60,  // minutes
                    (uptime / 1000) % 60     // seconds
            );
        else if(uptime >= hour)
            timecode = String.format(
                    Locale.getDefault(), pattern,
                    uptime / hour,           // hours
                    (uptime / minute) % 60,  // minutes
                    (uptime / 1000) % 60     // seconds
            );
        else
            timecode = String.format(
                    Locale.getDefault(), pattern,
                    (uptime / minute),  // minutes
                    (uptime / 1000) % 60     // seconds
            );

        return timecode;
    }
}
