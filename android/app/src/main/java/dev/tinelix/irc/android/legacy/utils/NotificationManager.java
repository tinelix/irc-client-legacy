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

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;

public class NotificationManager {
    private final ArrayList<Integer> mNotifIds;
    private Context mContext;
    private android.app.NotificationManager mNotifMan;
    public  boolean ledIndicate;
    public  boolean vibrate;
    public  boolean playSound;
    public  String  ringtone_url;

    public NotificationManager(Context context) {
        mContext = context;
        mNotifIds = new ArrayList<>();

        SharedPreferences global_prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        this.ledIndicate  = global_prefs.getBoolean("notifyLED", true);
        this.vibrate      = global_prefs.getBoolean("notifyVibrate", true);
        this.playSound    = global_prefs.getBoolean("notifySound", true);
        this.ringtone_url = global_prefs.getString("notifyRingtone", "");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotifMan = mContext.getSystemService(android.app.NotificationManager.class);

            if(mNotifMan != null) {
                NotificationChannel channel = mNotifMan.getNotificationChannel("lp_updates");

                if(channel != null)
                    mNotifMan.deleteNotificationChannel(channel.getId());
            }
        } else {
            mNotifMan = (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public void createNotificationChannel(String channelId) {
        createNotificationChannel(channelId, ledIndicate, vibrate, playSound, true);
    }

    public void createNotificationChannel(
            String channelId, boolean ledIndicate, boolean vibrate, boolean playSound,
            boolean visibleOnLockScreen
    ) {

        int channelTitleId = R.string.notif_channels_default;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotifMan = mContext.getSystemService(android.app.NotificationManager.class);

            NotificationChannel channel;

            if(mNotifMan == null)
                return;

            channel = mNotifMan.getNotificationChannel(channelId);

            switch (channelId) {
                case "channel_msgs":
                    channelTitleId = R.string.notif_channels_chanmsg;
                    break;
                case "private_msgs":
                    channelTitleId = R.string.notif_channels_privmsg;
                    break;
                case "service_notifs":
                    channelTitleId = R.string.notif_channels_service;
                    break;
            }

            if(channel == null) {
                int importance = playSound ?
                        android.app.NotificationManager.IMPORTANCE_HIGH :
                        android.app.NotificationManager.IMPORTANCE_LOW;

                channel = new NotificationChannel(
                        channelId,
                        mContext.getResources().getString(channelTitleId),
                        importance
                );
            }

            channel.enableLights(ledIndicate);
            channel.enableVibration(vibrate);
            channel.setLockscreenVisibility(
                    visibleOnLockScreen ?
                            Notification.VISIBILITY_PUBLIC :
                            Notification.VISIBILITY_SECRET
            );

            if(playSound) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                if (!ringtone_url.equals("content://settings/system/notification_sound")) {
                    channel.setSound(Uri.parse(ringtone_url), audioAttributes);
                } else {
                    channel.setSound(null, null);
                }
            }

            mNotifMan.createNotificationChannel(channel);
        } else {
            mNotifMan = (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public Notification createServiceNotification(
            int icon, String channelId, String title, String summary,
            long startTime
    ) {
        Notification notification;
        int backgroundColor = mContext.getResources().getColor(
                dev.tinelix.holoexperience.library.R.color.holo_blue_dark
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder =
                    new Notification.Builder(mContext, channelId)
                            .setColor(backgroundColor)
                            .setSmallIcon(icon)
                            .setContentTitle(title)
                            .setContentText(summary)
                            .setVisibility(Notification.VISIBILITY_SECRET);

            if(startTime > 0)
                builder.setWhen(startTime).setUsesChronometer(true).setOngoing(true);

            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;

            Notification.BigTextStyle style = new Notification.BigTextStyle().bigText(summary);

            builder.setStyle(style);

        } else {

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(mContext)
                        .setColor(backgroundColor)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(summary)
                        .setVisibility(NotificationCompat.VISIBILITY_SECRET);

            if(startTime > 0)
                builder.setWhen(startTime).setUsesChronometer(true).setOngoing(true);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                NotificationCompat.BigTextStyle style =
                        new NotificationCompat.BigTextStyle().bigText(summary);

                builder.setStyle(style);
            }

            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }
        return notification;
    }

    public void notify(int id, Notification notif) {
        mNotifMan.notify(id, notif);
    }
}
