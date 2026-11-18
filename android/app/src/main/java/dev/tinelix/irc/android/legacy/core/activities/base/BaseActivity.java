package dev.tinelix.irc.android.legacy.core.activities.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import dev.tinelix.holoexperience.library.app.HoloCompatActivity;
import dev.tinelix.irc.android.legacy.IRCApplication;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.receivers.IRCBroadcastReceiver;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class BaseActivity extends HoloCompatActivity {

    protected Intent mServiceIntent;

    protected SharedPreferences appPrefs;
    protected ChatClientService mClientService;
    protected IRCBroadcastReceiver mIRCReceiver;
    protected boolean mServiceBound;
    protected String mServerAddress;

    protected ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            mClientService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBound = true;
            ChatClientService.ChatClientBinder binder =
                    (ChatClientService.ChatClientBinder) service;

            mClientService = binder.getService();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getAppPreferences() {
        return appPrefs;
    }

    public void setActionBar() {

    }

    protected void registerReceivers(ServerProfile profile) {
        IRCApplication app = (IRCApplication) getApplication();
        app.registerIRCReceiver(this, profile);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void unregisterReceiver() {
        IRCApplication app = (IRCApplication) getApplication();
        app.unregisterIRCReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void bindService(ServerProfile profile) {
        mServiceBound = true;

        if(mServiceIntent == null) {
            mServiceIntent = new Intent(getApplicationContext(), ChatClientService.class);
            mServiceIntent.putExtra("action", ChatClientService.IRC_CONNECT);
            mServiceIntent.putExtra("prefs_name", profile.name);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            getApplicationContext().startForegroundService(mServiceIntent);
        else
            getApplicationContext().startService(mServiceIntent);

        bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void onDisconnect(ServerProfile profile, Bundle data) {

    }

    public void onConnect(ServerProfile profile, Bundle data) {
        registerReceivers(profile);
    }

    public void onReceive(ServerProfile profile, Bundle data, String[] rawLines) {

    }

    public ChatClientService getService() {
        return mClientService;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    protected void onDestroy() {
        if(mServiceConnection != null && mServiceBound)
            unbindService(mServiceConnection);
        super.onDestroy();
    }

    public String getServerAddress() {
        return mServerAddress;
    }
}
