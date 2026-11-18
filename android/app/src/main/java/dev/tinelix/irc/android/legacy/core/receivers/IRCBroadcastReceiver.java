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

package dev.tinelix.irc.android.legacy.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class IRCBroadcastReceiver extends BroadcastReceiver {

    private IRCSocketWrapper mSocket;
    private Handler mHandler;
    private Context mContext;
    private ServerProfile mProfile;
    private SharedPreferences mProfilePrefs;
    private IRCBroadcastSignal mMotdSignal;

    public IRCBroadcastReceiver() {
        mHandler = new Handler(Looper.myLooper());
    }

    public IRCBroadcastReceiver(Context ctx) {
        mContext = ctx;
        mHandler = new Handler(Looper.myLooper());
    }

    public IRCBroadcastReceiver(Context ctx, ServerProfile profile) {
        mContext = ctx;
        mProfile = profile;

        if(mContext instanceof BaseActivity) {
            if(((BaseActivity) mContext).getService() != null)
                mSocket = ((BaseActivity) mContext).getService().getWrapper(profile);
        }

        if(profile != null)
            mProfilePrefs = mContext.getSharedPreferences(profile.name, 0);

        mHandler = new Handler(Looper.myLooper());
    }

    public IRCBroadcastReceiver(ServerProfile profile) {
        mProfile = profile;
        mHandler = new Handler(Looper.myLooper());
    }

    public void setContext(Context context) {
        mContext = context;

        if(mProfile != null)
            mProfilePrefs = mContext.getSharedPreferences(mProfile.name, 0);
    }

    public void setServerProfile(ServerProfile profile) {
        this.mProfile = profile;

        if(mProfile != null)
            mProfilePrefs = mContext.getSharedPreferences(mProfile.name, 0);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final Bundle data = intent.getExtras();

        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        receiveRawData(context, data);
                    }
                }
        );

        thread.start();
    }

    @SuppressWarnings("ConstantConditions")
    private void receiveRawData(Context context, final Bundle data) {
        if(data == null)
            return;

        System.gc();

        if((data.containsKey("is_connected") && !data.getBoolean("is_connected")) ||
           (data.containsKey("error_code") && data.containsKey("server")))
                mHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                if(mContext instanceof BaseActivity)
                                    ((BaseActivity) mContext).onDisconnect(mProfile, data);
                            }
                        }
                );

        if(data.containsKey("raw_data") && data.containsKey("server")) {

            final String[] lines = data.getString("raw_data").split("\r\n");
            data.putInt("lines_count", lines.length);

            mHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            if(mContext instanceof BaseActivity) {
                                BaseActivity activity = (BaseActivity) mContext;
                                activity.onReceive(mProfile, data, lines);
                            }
                        }
                    }
            );

        } else if(data.containsKey("server")) {
            mHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            if(mContext instanceof BaseActivity)
                                ((BaseActivity) mContext).onConnect(mProfile, data);
                        }
                    }
            );
        }
    }
}
