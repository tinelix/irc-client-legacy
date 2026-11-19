package dev.tinelix.irc.android.support.filters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.ServerStats;
import dev.tinelix.irc.android.support.models.IRCUsers;

import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_CHANNEL_NAMES;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_CHANNEL_NAMES_END;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_ADMINS;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_CHANNELS;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_CLIENTS;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_GLOBAL;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_LOCAL;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_LUSER_ME;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_MOTD;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_MOTD_BEGIN;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_MOTD_END;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_RULES;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_RULES_BEGIN;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_RULES_END;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_TOPIC;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_TOPIC_CREATED;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_WHO;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_CMD_WHO_END;
import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.IN_PING;

public class IRCOutputFilter {

    private final String[] mLines;
    private final boolean mReverse;
    private final boolean mShowTimestamp;
    private IRCBroadcastSignal mIncompleteSignal;
    private LinkedHashMap<String, List<IRCBroadcastSignal>> mSignalsMap;

    public IRCOutputFilter(String[] lines, boolean reverse, boolean showTimestamp) {
        mLines = lines;
        mReverse = reverse;
        mShowTimestamp = showTimestamp;
    }

    public IRCOutputFilter(String[] lines, boolean reverse) {
        mLines = lines;
        mReverse = reverse;
        mShowTimestamp = false;
    }

    public IRCOutputFilter(String[] lines) {
        mLines = lines;
        mReverse = false;
        mShowTimestamp = false;
    }

    public LinkedHashMap<String, List<IRCBroadcastSignal>> getFilteredOutputSignals(
            IRCBroadcastSignal continueWith
    ) {
        if(mIncompleteSignal != null && !mIncompleteSignal.isFinished())
            mIncompleteSignal = continueWith;

        return getFilteredOutputSignals();
    }

    public LinkedHashMap<String, List<IRCBroadcastSignal>> getFilteredOutputSignals() {

        System.gc();

        mSignalsMap = new LinkedHashMap<>();
        mSignalsMap.put("@server", new ArrayList<IRCBroadcastSignal>());

        for (String line : mLines) {

            if(line.length() == 0) continue;

            IRCBroadcastSignal signal = IRCBroadcastSignal.fromString(
                    line, true, mShowTimestamp
            );

            if(signal == null) continue;

            boolean isValidTypes = signal.type != IN_PING &&
                                   signal.type != IN_CMD_LUSER_ME &&
                                   signal.type != IN_CMD_WHO_END;

            signal = filter(signal);

            if(signal == null || !isValidTypes || !signal.isFinished()) continue;

            if(signal.isFinished() && mIncompleteSignal != null && signal.type == mIncompleteSignal.type)
                mIncompleteSignal = null;

            ArrayList<IRCBroadcastSignal> signalsList = (ArrayList<IRCBroadcastSignal>)
                    mSignalsMap.get(signal.destination != null ? signal.destination : "@server");

            if(signalsList == null) signalsList = new ArrayList<>();

            String destination;

            if(signal.destination != null) {
                if(!mSignalsMap.containsKey(signal.destination))
                    mSignalsMap.put(signal.destination, signalsList);

                destination = signal.destination;
            } else {
                destination = "@server";
            }

            if (mReverse)
                signalsList.add(0, signal);
            else
                signalsList.add(signal);

            mSignalsMap.put(destination, signalsList);
        }

        return mSignalsMap;
    }

    private IRCBroadcastSignal filter(IRCBroadcastSignal signal) {

        if (signal.type == IN_CMD_WHO || signal.type == IN_CMD_TOPIC ||
            signal.type == IN_CMD_CHANNEL_NAMES) {
            if(mIncompleteSignal == null)
                mIncompleteSignal = signal;
            else
                mIncompleteSignal.merge(signal);

            return null;
        } else if(signal.type == IN_CMD_WHO_END || signal.type == IN_CMD_TOPIC_CREATED ||
                  signal.type == IN_CMD_CHANNEL_NAMES_END) {

            if (mIncompleteSignal != null) {
                if(mIncompleteSignal instanceof IRCUsers)
                    ((IRCUsers) mIncompleteSignal).parseContent();

                mIncompleteSignal.finish();
            }

            return mIncompleteSignal;

        } else if (signal.destination == null) {

            switch (signal.type) {
                case IN_CMD_LUSER_ADMINS:
                    mIncompleteSignal = signal;
                    return null;
                case IN_CMD_LUSER_CLIENTS:
                case IN_CMD_LUSER_LOCAL:
                case IN_CMD_LUSER_CHANNELS:
                case IN_CMD_LUSER_GLOBAL:
                    if(mIncompleteSignal == null) return null;

                    if(mIncompleteSignal instanceof ServerStats) {
                        mIncompleteSignal.merge(signal);

                        if (signal.type != IN_CMD_LUSER_GLOBAL) return null;

                        mIncompleteSignal.finish();
                        return mIncompleteSignal;
                    }
                    break;
                case IN_CMD_MOTD_BEGIN:
                    mIncompleteSignal = new IRCBroadcastSignal(IN_CMD_MOTD, true);
                    return null;
                case IN_CMD_RULES:
                case IN_CMD_MOTD:
                    if(mIncompleteSignal == null) break;
                    mIncompleteSignal.merge(signal);
                    return null;
                case IN_CMD_RULES_END:
                case IN_CMD_MOTD_END:
                    if(mIncompleteSignal == null) break;
                    mIncompleteSignal.finish();
                    return mIncompleteSignal;
                case IN_CMD_RULES_BEGIN:
                    mIncompleteSignal = new IRCBroadcastSignal(IN_CMD_RULES, true);
                    return null;
            }
        }

        return signal;
    }

    public IRCBroadcastSignal getIncompleteSignal() {
        return mIncompleteSignal;
    }

    public IRCUsers getChannelMembers(String destName) {

        IRCUsers users = null;

        ArrayList<IRCBroadcastSignal> signals = new ArrayList<>();

        if(destName == null) return null;

        for (int i = mLines.length - 1; i >= 0; i--) {
            String line = mLines[i];

            if(line.length() == 0) continue;

            IRCBroadcastSignal signal = IRCBroadcastSignal.fromString(
                    line, true, mShowTimestamp
            );

            if(signal == null) continue;

            boolean isValidTypes = signal.type == IRCBroadcastSignal.IN_CMD_WHO ||
                                   signal.type == IRCBroadcastSignal.IN_CMD_WHO_END;

            if(!isValidTypes && signals.size() > 0) break;

            if(!isValidTypes) continue;

            if(signal.destination != null && !destName.equals(signal.destination)) continue;

            signals.add(0, signal);
        }

        for (IRCBroadcastSignal signal : signals) {
            if (signal.type == IN_CMD_WHO && signal instanceof IRCUsers) {

                if (users == null)
                    users = (IRCUsers) signal;
                else
                    users.merge(signal);

            } else if(signal.type == IN_CMD_WHO_END) {
                if (users != null)
                    users.parseContent();
                return users;
            }
        }

        return null;
    }
}
