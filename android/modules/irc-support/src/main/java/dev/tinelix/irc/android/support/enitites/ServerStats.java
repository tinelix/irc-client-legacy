package dev.tinelix.irc.android.support.enitites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.ArrayList;
import java.util.Locale;

import dev.tinelix.irc.android.support.R;

public class ServerStats extends IRCBroadcastSignal {
    private int mChannelsCount;
    private int mAdminsCount;
    private int mLocalOnlineUsersCount;
    private int mLocalMaxUsersCount;
    private int mGlobalOnlineUsersCount;
    private int mGlobalMaxUsersCount;
    private int mUnknownsCount;

    public ServerStats(String rawContent) {
        super(IRCBroadcastSignal.IN_CMD_LUSER_ME, true);
        mRawContent = rawContent;
        parseContent();
    }

    public ServerStats(int type, String rawContent) {
        super(type, true);
        mRawContent = rawContent;
        parseContent();
    }

    public boolean parseContent() {
        try {
            String[] words = mRawContent.split(" ");
            int type = Integer.parseInt(words[1]);

            switch (type) {
                case IRCBroadcastSignal.IN_CMD_LUSER_UNKNOWNS:
                    if (words.length >= 4)
                        mUnknownsCount = Integer.parseInt(words[3]);
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_ADMINS:
                    if (words.length >= 4)
                        mAdminsCount = Integer.parseInt(words[3]);
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_CHANNELS:
                    if (words.length >= 4)
                        mChannelsCount = Integer.parseInt(words[3]);
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_LOCAL:
                    if(words.length >= 5 &&
                       words[3].matches("^\\d+$") &&
                       words[4].matches("^\\d+$")) {
                        mLocalOnlineUsersCount = Integer.parseInt(words[3]);
                        mLocalMaxUsersCount = Integer.parseInt(words[4]);
                    } else if(words.length >= 10 &&
                              words[6].matches("^\\d+$") &&
                              words[9].matches("^\\d+$")) {

                        mLocalOnlineUsersCount = Integer.parseInt(words[6]);
                        mLocalMaxUsersCount = Integer.parseInt(words[9]);
                    }
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_GLOBAL:
                    if(words.length >= 5 &&
                            words[3].matches("^\\d+$") &&
                            words[4].matches("^\\d+$")) {
                        mGlobalOnlineUsersCount = Integer.parseInt(words[3]);
                        mGlobalMaxUsersCount = Integer.parseInt(words[4]);
                    } else if(words.length >= 10 &&
                              words[6].matches("^\\d+$") &&
                              words[9].matches("^\\d+$")) {

                        mGlobalOnlineUsersCount = Integer.parseInt(words[6]);
                        mGlobalMaxUsersCount = Integer.parseInt(words[9]);
                    }
                    break;
            }
            finish();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void merge(IRCBroadcastSignal signal) {

        if(signal == null)
            return;

        if(signal instanceof ServerStats) {

            ServerStats stats = ((ServerStats) signal);

            switch (signal.type) {
                case IRCBroadcastSignal.IN_CMD_LUSER_CHANNELS:
                    mChannelsCount = stats.getChannelsCount();
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_ADMINS:
                    mAdminsCount = stats.getAdminsCount();
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_LOCAL:
                    mLocalOnlineUsersCount = stats.getLocalOnlineUsersCount();
                    mLocalMaxUsersCount = stats.getLocalMaxUsersCount();
                    break;
                case IRCBroadcastSignal.IN_CMD_LUSER_GLOBAL:
                    mGlobalOnlineUsersCount = stats.getGlobalOnlineUsersCount();
                    mGlobalMaxUsersCount = stats.getGlobalMaxUsersCount();
                    break;
            }
        }
    }

    public int getChannelsCount() {
        return mChannelsCount;
    }

    public int getLocalOnlineUsersCount() {
        return mLocalOnlineUsersCount;
    }

    public int getLocalMaxUsersCount() {
        return mLocalMaxUsersCount;
    }

    public int getGlobalOnlineUsersCount() {
        return mGlobalOnlineUsersCount;
    }

    public int getGlobalMaxUsersCount() {
        return mGlobalMaxUsersCount;
    }

    public int getAdminsCount() {
        return mAdminsCount;
    }

    public int getUnknownsCount() {
        return mUnknownsCount;
    }

    private String getLocalizedBody(Context context) {
        Resources res = context.getResources();

        String localOnlineStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.users_count,
                mLocalOnlineUsersCount, 6, false
        );

        String localMaxStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.users_count,
                mLocalOnlineUsersCount, 6, false
        );

        String globalOnlineStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.users_count,
                mGlobalOnlineUsersCount, 6, false
        );

        String globalMaxStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.users_count,
                mGlobalMaxUsersCount, 6, false
        );

        String adminsStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.administrators_count,
                mAdminsCount, 6, false
        );

        String channelsStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.channels_count,
                mChannelsCount, 6, false
        );

        String unknownsStr = IRCBroadcastSignal.getQuantityString(
                context, R.plurals.new_users_count,
                mUnknownsCount, 6, false
        );

        return res.getString(
                R.string.ircs_signal_luser,
                localOnlineStr, localMaxStr,
                globalOnlineStr, globalMaxStr,
                unknownsStr, adminsStr, channelsStr
        );
    }

    @Override
    public SpannableString getLocalizedSpannedBody(Context context, int highlightColor) {

        String localizedBody = getLocalizedBody(context);

        String[] lines = localizedBody.split("\n");

        SpannableString spannedBody = new SpannableString(localizedBody);

        // Local statistics title
        spannedBody.setSpan(
                new StyleSpan(Typeface.BOLD),
                0, lines[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannedBody.setSpan(
                new ForegroundColorSpan(highlightColor),
                0, lines[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Global statistics title
        int globalIndex = localizedBody.indexOf(lines[4]);

        spannedBody.setSpan(
                new StyleSpan(Typeface.BOLD),
                globalIndex, globalIndex + lines[4].length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        spannedBody.setSpan(
                new ForegroundColorSpan(highlightColor),
                globalIndex, globalIndex + lines[4].length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannedBody;
    }
}
