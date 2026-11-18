package dev.tinelix.irc.android.legacy.ui.lists.adapters;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import dev.tinelix.irc.android.support.models.IRCServer;

public class ChatClientService extends Service {

    public static final String IRC_CONNECT = "ircConnect";
    private ChatClientBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data = intent.getExtras();
        if(data != null) {
            String action = data.getString("action");

            if(action == null)
                action = "";

            switch (action) {
                case IRC_CONNECT:
                    break;
            }
        }
        return Service.START_STICKY;
    }

    public void createSocket(IRCServer server) {

    }

    private class ChatClientBinder extends Binder {

        public ChatClientService getService() {
            return ChatClientService.this;
        }

    }
}
