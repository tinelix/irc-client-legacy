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

package dev.tinelix.irc.android.legacy;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.seppius.i18n.plurals.PluralResources;

import java.io.File;

import dev.tinelix.irc.android.legacy.core.receivers.IRCBroadcastReceiver;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class IRCApplication extends Application {

    String mPrefsDir;
    private IRCBroadcastReceiver mReceiver;
    public PluralResources pluralResources;

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            try {
                pluralResources = new PluralResources(getResources());
            } catch (SecurityException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }

        SharedPreferences globalPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = globalPrefs.edit();

        if(!globalPrefs.contains("uiTheme"))
            editor.putString("uiTheme", "Dark");

        if(!globalPrefs.contains("uiLanguage"))
            editor.putString("uiLanguage", "System");

        if(!globalPrefs.contains("showMsgTimestamps"))
            editor.putBoolean("showMsgTimestamps", true);

        if(!globalPrefs.contains("firstStartupTimestamp"))
            editor.putLong("firstStartupTimestamp", System.currentTimeMillis());

        editor.commit();
    }

    public void registerIRCReceiver(Context context, ServerProfile profile) {
        if(mReceiver == null) {
            mReceiver = new IRCBroadcastReceiver(context, profile);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mReceiver, new IntentFilter("dev.tinelix.irc.android.legacy.IRC_DATA_RECEIVE")
            );
        } else {
            mReceiver.setContext(context);
            mReceiver.setServerProfile(profile);
        }
    }

    public void unregisterIRCReceiver() {
        if(mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
