package dev.tinelix.irc.android.legacy.core.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.legacy.core.activities.MainActivity;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.utils.NotificationManager;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;
import dev.tinelix.irc.android.support.filters.IRCOutputFilter;
import dev.tinelix.irc.android.support.listeners.SocketConnectionListener;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

import static dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal.OUT_RAW_PONG;
import static java.lang.Thread.sleep;

public class ChatClientService extends Service implements SocketConnectionListener {

    private static final String TAG = "IRC-CCS";

    public static final String IRC_CONNECT = "ircConnect";

    private ChatClientBinder mBinder = new ChatClientBinder();
    private final ArrayList<IRCSocketWrapper> mWrappers = new ArrayList<>();
    private SharedPreferences mProfilePrefs;
    private ServerProfile mProfile;
    private NotificationManager mNotifManager;
    private Notification mNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null)
            return START_NOT_STICKY;

        Bundle data = intent.getExtras();
        if(data != null) {
            String action = data.getString("action");
            String prefsName = data.getString("prefs_name");

            mProfilePrefs = getSharedPreferences(prefsName, 0);

            if(action == null)
                action = "";

            ServerProfile profile = ServerProfile.from(prefsName, mProfilePrefs);

            switch (action) {
                case IRC_CONNECT:

                    if(ServersCacheDatabase.isInitialized()) {
                        IRCServer server = ServersCacheDatabase.getServer(
                                String.format(
                                        "%s:%s",
                                        profile.getServerParams().address,
                                        profile.getServerParams().port
                                )
                        );

                        if(server != null)
                            profile.setServerParams(server);
                    }

                    connect(profile);
                    break;
            }

            if (mNotification == null) {
                mNotification = createServiceNotification();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    startForeground(0x4000, mNotification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
                else
                    startForeground(0x4000, mNotification);
            }
        }
        return Service.START_STICKY;
    }

    private Notification createServiceNotification() {

        mNotifManager = new NotificationManager(this);

        mNotifManager.createNotificationChannel(
                "service_notifs",
                false, false, false, false
        );

        Intent notifIntent;

        String summary = getResources().getString(R.string.notif_connection_summary_none);
        long when = 0;

        notifIntent = new Intent(this, MainActivity.class);
        notifIntent.setAction(Intent.ACTION_MAIN);
        notifIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        if(mWrappers.size() >= 2) {

            long ping1 = 0;
            long ping2 = 0;

            IRCServer server1 = null;
            IRCServer server2 = null;

            for(int i = 0; i < mWrappers.size(); i++) {
                IRCSocketWrapper wrapper = mWrappers.get(i);
                IRCServer server = wrapper.getServerProfile().getServerParams();

                long ping = wrapper.getCurrentPing();

                if(i == 0) {
                    server1 = server;
                    ping1 = ping;
                    when = wrapper.getConnectionStartTime();
                } else if(i == 1) {
                    server2 = server;
                    ping2 = ping;
                } else {
                    break;
                }
            }

            if(server1 != null && server2 != null) {
                if (mWrappers.size() > 2) {
                    if (ping1 >= 1000 || ping2 >= 1000)
                        summary = getResources().getString(
                                R.string.notif_connection_summary_many_with_ping_2,
                                server1.address, (float)ping1 / 1000,
                                server2.address, (float)ping2 / 1000
                        );
                    else if (ping1 > 0 && ping2 > 0)
                        summary = getResources().getString(
                                R.string.notif_connection_summary_many_with_ping,
                                server1.address, ping1,
                                server2.address, ping2,
                                mWrappers.size() - 2
                        );
                    else
                        summary = getResources().getString(
                                R.string.notif_connection_summary_many,
                                server1.address, server2.address,
                                mWrappers.size() - 2
                        );
                } else {
                    if (ping1 >= 1000 || ping2 >= 1000)
                        summary = getResources().getString(
                                R.string.notif_connection_summary_two_with_ping_2,
                                server1.address, (float)ping1 / 1000,
                                server2.address, (float)ping2 / 1000
                        );
                    else if (ping1 > 0 && ping2 > 0)
                        summary = getResources().getString(
                                R.string.notif_connection_summary_two_with_ping,
                                server1.address, ping1,
                                server2.address, ping2
                        );
                    else
                        summary = getResources().getString(
                                R.string.notif_connection_summary_two,
                                server1.address, server2.address
                        );
                }
            }

        } else if(mWrappers.size() == 1) {
            IRCSocketWrapper wrapper = mWrappers.get(0);
            IRCServer server = wrapper.getServerProfile().getServerParams();

            long ping = wrapper.getCurrentPing();

            if(ping >= 1000)
                summary = getResources().getString(
                        R.string.notif_connection_summary_with_ping_2,
                        server.address, (float)ping / 1000
                );
            else if(ping > 0)
                summary = getResources().getString(
                        R.string.notif_connection_summary_with_ping,
                        server.address, ping
                );
            else
                summary = getResources().getString(
                        R.string.notif_connection_summary, server.address
                );

            when = wrapper.getConnectionStartTime();
        } else {
            summary = getResources().getString(R.string.notif_connection_summary_none);
        }

        mNotification = mNotifManager.createServiceNotification(
            R.drawable.ic_notification_icon, "service_notifs",
            getResources().getString(R.string.app_name),
            summary, when
        );

        mNotification.contentIntent = PendingIntent.getActivity(
                this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        return mNotification;
    }

    public void connect(final ServerProfile profile) {

        if(getWrapper(profile) != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    onConnect(getWrapper(profile), profile);
                }
            });
            return;
        }

        final IRCSocketWrapper wrapper = new IRCSocketWrapper(this, profile);
        wrapper.setConnectionListener(ChatClientService.this);

        if(UsersCacheDatabase.isInitialized())
            wrapper.setNicknames(UsersCacheDatabase.getNicknameList());

        mWrappers.add(wrapper);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        wrapper.connect(false);
                    }
                }
        ).start();
    }

    public IRCSocketWrapper getWrapper(ServerProfile profile) {
        for (IRCSocketWrapper wrapper : mWrappers) {
            if(wrapper != null && wrapper.getServerProfile().name.equals(profile.name))
                return wrapper;
        }

        return null;
    }

    @Override
    public void onConnect(final IRCSocketWrapper wrapper, final ServerProfile profile) {

        Bundle data = new Bundle();
        IRCServer server = wrapper.getServerProfile().getServerParams();

        data.putString("server", server.address + ":" + server.port);
        data.putBoolean("is_connected",
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_CONNECTED ||
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_RUNNING
        );

        Intent intent = new Intent();
        intent.setAction("dev.tinelix.irc.android.legacy.IRC_DATA_RECEIVE");
        intent.putExtras(data);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        ArrayList<ServerProfile.Nickname> nicknames = wrapper.getNicknamesList();

        createServiceNotification();
        mNotifManager.notify(0x4000, mNotification);

        if(nicknames == null)
            return;

        if(wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_RUNNING)
            return;

        wrapper.sendCommand(
                new IRCCommand(
                        IRCBroadcastSignal.OUT_CMD_USER,
                        String.format(
                                "%s 8 * :%s",
                                profile.hostname,
                                profile.realname
                        )
                ), profile.getServerParams().outputEncoding
        );

        if(nicknames.size() > 0) {
            wrapper.selectNickname(0);
            wrapper.readRawData();
        } else {
            wrapper.disconnect();
        }
    }

    public HashMap<String, List<IRCBroadcastSignal>> createSignalsHistory(
            ServerProfile profile, boolean reverse
    ) {

        IRCSocketWrapper wrapper = getWrapper(profile);

        if(wrapper == null)
            return null;

        String rawHistory = wrapper.getSocketHistory(65536);

        if(rawHistory == null || rawHistory.length() == 0)
            return null;

        String[] lines = rawHistory.split("\r\n");
        IRCOutputFilter filter = new IRCOutputFilter(lines, reverse, true);

        return filter.getFilteredOutputSignals();
    }

    public ArrayList<IRCBroadcastSignal> createSignalsHistory(
            ServerProfile profile, String destName, boolean reverse
    ) {
        if(destName == null)
            destName = "@server";

        IRCSocketWrapper wrapper = getWrapper(profile);

        if(wrapper == null)
            return null;

        String rawHistory = wrapper.getSocketHistory(65536);

        if(rawHistory.length() == 0)
            return null;

        String[] lines = rawHistory.split("\r\n");
        IRCOutputFilter filter = new IRCOutputFilter(lines, reverse, true);

        HashMap<String, List<IRCBroadcastSignal>> signalsMap = filter.getFilteredOutputSignals();

        ArrayList<IRCBroadcastSignal> signals;

        if(signalsMap.containsKey(destName)) {
            signals = (ArrayList<IRCBroadcastSignal>) signalsMap.get(destName);

            for (int i = 0; i < signals.size(); i++) {
                long receivedDate = Long.parseLong(lines[i].split(" ", 2)[0]);
                IRCBroadcastSignal signal = signals.get(i);
                signal.date = receivedDate;
                signals.set(i, signal);
            }

            return signals;
        }

        return null;
    }

    public IRCUsers getChannelMembersFromHistory(ServerProfile profile, String destName) {
        if(destName == null) return null;

        IRCSocketWrapper wrapper = getWrapper(profile);

        if(wrapper == null) return null;

        String rawHistory = wrapper.getSocketHistory(262144);

        if(rawHistory.length() == 0) return null;

        String[] lines = rawHistory.split("\r\n");
        IRCOutputFilter filter = new IRCOutputFilter(lines, true, true);

        return filter.getChannelMembers(destName);
    }

    @Override
    public void onReceive(
            IRCSocketWrapper wrapper, ServerProfile profile,
            String rawData, long dataSize, long receivedDate
    ) {

        String[] lines = rawData.split("\r\n");

        if(lines.length == 1)
            Log.d(TAG, String.format(
                            "Input RAW from %s:%s  [%s]\r\n" +
                            "Packet size: %s bytes",
                            profile.getServerParams().address, profile.getServerParams().port,
                            rawData.replace("\r\n", ""),
                            dataSize
                       )
            );
        else if(lines.length > 1) {
            StringBuilder builder = new StringBuilder();

            builder.append(
                    String.format(
                            "Input RAW from %s:%s\r\n",
                            profile.getServerParams().address,
                            profile.getServerParams().port
                    )
            );

            for(int i = 0; i < lines.length; i++) {
                builder.append(
                    String.format(
                        Locale.getDefault(), "%03d - [%s]\r\n", i + 1, lines[i]
                    )
                );

                if(i == 20) {
                    builder.append("...\r\n");
                    break;
                }
            }

            builder.append(
                String.format("Lines count: %s | Packet size: %s bytes", lines.length, dataSize)
            );

            Log.d(TAG, builder.toString());
        }

        Bundle data = new Bundle();
        IRCServer server = wrapper.getServerProfile().getServerParams();

        data.putString("server", server.address + ":" + server.port);
        data.putString("raw_data", rawData);
        data.putLong("date", receivedDate);
        data.putBoolean("is_connected",
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_CONNECTED ||
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_RUNNING
        );

        Intent intent = new Intent();
        intent.setAction("dev.tinelix.irc.android.legacy.IRC_DATA_RECEIVE");
        intent.putExtras(data);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        for(String line : lines) {
            if(line.length() == 0) continue;

            String[] words = line.split(" ", 3);

            try {
                if (words.length > 2) {
                    switch (words[1]) {
                        case "433":
                            sleep(400);
                            wrapper.selectNextNickname();
                            break;
                        case "001":
                            wrapper.startPingBouncerTask(profile.pingInterval, true);
                            break;
                        case "PONG":
                            createServiceNotification();
                            mNotifManager.notify(0x4000, mNotification);
                            break;
                    }
                }

                if(words.length == 2 && words[0].equals("PING")) {
                    wrapper.sendCommand(
                            new IRCCommand(OUT_RAW_PONG, words[1]),
                            profile.getServerParams().outputEncoding
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisconnect(IRCSocketWrapper wrapper, ServerProfile profile) {
        IRCSocketWrapper socketWrapper = getWrapper(profile);
        mWrappers.remove(socketWrapper);

        Bundle data = new Bundle();
        IRCServer server = wrapper.getServerProfile().getServerParams();

        createServiceNotification();
        mNotifManager.notify(0x4000, mNotification);

        data.putString("server", server.address + ":" + server.port);
        data.putBoolean("is_connected",
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_CONNECTED ||
                wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_RUNNING
        );

        Log.e(TAG, String.format(
                "Connection closed with %s:%s",
                server.address, server.port
        ));

        Intent intent = new Intent();
        intent.setAction("dev.tinelix.irc.android.legacy.IRC_DATA_RECEIVE");
        intent.putExtras(data);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(int errorCode, ServerProfile profile) {
        Bundle data = new Bundle();

        IRCServer server = profile.getServerParams();

        data.putString("server", server.address + ":" + server.port);
        data.putInt("error_code", errorCode);

        Log.e(TAG, String.format(
                "Connection error (%s:%s): %s", server.address, server.port, errorCode
        ));

        Intent intent = new Intent();
        intent.setAction("dev.tinelix.irc.android.legacy.IRC_DATA_RECEIVE");
        intent.putExtras(data);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public class ChatClientBinder extends Binder {

        public ChatClientService getService() {
            return ChatClientService.this;
        }

    }
}
