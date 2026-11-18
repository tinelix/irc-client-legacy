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

package dev.tinelix.irc.android.legacy.core.activities;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.irc.android.legacy.BuildConfig;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.core.fragments.pages.ChatPageFragment;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.adapters.ChannelMembersAdapter;
import dev.tinelix.irc.android.legacy.ui.list.pagers.ChatPagerAdapter;
import dev.tinelix.irc.android.legacy.ui.views.WriteTextPanel;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;
import dev.tinelix.irc.android.support.enitites.LazyEntity;
import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.filters.IRCOutputFilter;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.ServerChannel;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class ChatActivity extends BaseActivity {
    private Bundle mArguments;
    private ServerProfile mProfile;
    private SharedPreferences mProfilePrefs;
    private ChatPagerAdapter mPagerAdapter;
    private WriteTextPanel mWritePanel;
    private ViewPager mPager;
    private boolean mIsNewTask = false;
    private Menu mActivityMenu;
    private IRCBroadcastSignal mIncompleteSignal;
    private boolean mHistoryLoaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceConnection = new ServiceConnection() {

            public void onServiceDisconnected(ComponentName name) {
                mServiceBound = false;
                mClientService = null;
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                mServiceBound = true;
                ChatClientService.ChatClientBinder binder =
                        (ChatClientService.ChatClientBinder) service;

                mClientService = binder.getService();

                if(mWritePanel != null && mProfile != null) {
                    mWritePanel.setSocketWrapper(mClientService.getWrapper(mProfile));
                }

                mPagerAdapter.loadSocketHistory(mClientService);

                new Handler(Looper.myLooper()).postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mHistoryLoaded = true;
                            }
                        }, 200
                );
            }
        };

        setContentView(R.layout.activity_chat);

        if(getIntent().getExtras() != null) {
            mArguments = getIntent().getExtras();
        } else if(savedInstanceState != null) {
            mArguments = savedInstanceState;
        }

        if(mArguments != null) {
            if(mArguments.containsKey("prefsName")) {

                mProfilePrefs = getSharedPreferences(
                        mArguments.getString("prefsName"), 0
                );

                ServersCacheDatabase.initDatabase(this, mProfilePrefs.getString("profileId", ""));

                loadIRCProfile();

                IRCServer server =  mProfile.getServerParams();

                if(server == null) {
                    finish();
                    return;
                }

                mServerAddress = String.format("%s:%s", server.address, server.port);

                getSupportActionBar().setTitle(mProfile.fullName);
                getSupportActionBar().setSubtitle(mServerAddress);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTaskDescription(
                            new ActivityManager.TaskDescription(mProfile.fullName)
                    );
                }

                installLayouts();

                bindService(mProfile);
                registerReceivers(mProfile);
            }

            if(mArguments.containsKey("isNewTask"))
                mIsNewTask = mArguments.getBoolean("isNewTask");

        } else {
            finish();
        }

        if(mIsNewTask)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadIRCProfile() {
        mProfile = ServerProfile.from(
                mArguments.getString("prefsName"),
                mProfilePrefs
        );

        mProfile.setServerParams(
                ServersCacheDatabase.getServer(
                        mProfilePrefs.getString("serverAddress", "")
                )
        );

        if(mProfile.quitMessage.length() == 0) {
            mProfile.quitMessage = String.format(
                    "Tinelix IRC %s for Android | %s",
                    BuildConfig.VERSION_NAME,
                    BuildConfig.APP_WEBSITE.replace("http://", "")
                            .replace("https://", "")
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.chat, menu);

        mActivityMenu = menu;

        MenuItem item = menu.findItem(R.id.channel_members_item);
        if(item != null) item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_connection_item) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            intent.putExtra("runningFrom", getClass().getSimpleName());
            intent.putExtra("currentServer", getServerAddress());
            startActivity(intent);
            finish();
        } else if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if(item.getItemId() == R.id.disconnect_item) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getService().getWrapper(mProfile).sendCommand(
                            new IRCCommand("/quit " + mProfile.quitMessage),
                            mProfile.getServerParams().inputEncoding
                    );
                }
            }).start();
        }

        return super.onOptionsItemSelected(item);
    }

    private void installLayouts() {
        adjustLayout(getResources().getConfiguration());

        mWritePanel = findViewById(R.id.write_panel);
        mWritePanel.setServerProfile(mProfile);
        mWritePanel.setOnSentListener(new WriteTextPanel.OnSentListener() {
            @Override
            public void onMessageSent(IRCBroadcastSignal signal) {
                mPagerAdapter.putIRCSignal(signal, mPager.getCurrentItem());
            }
        });

        PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);
        mPager = findViewById(R.id.pager);

        mPagerAdapter = new ChatPagerAdapter(this, mProfile, getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        tabStrip.setTextColor(getResources().getColor(android.R.color.white));
        tabStrip.setIndicatorColorResource(R.color.holo_blue_light);
        tabStrip.setDividerColorResource(android.R.color.darker_gray);
        tabStrip.setUnderlineColorResource(R.color.holo_blue_light);
        tabStrip.setViewPager(mPager);
        tabStrip.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if(positionOffset == 0.0f || positionOffset == 0.8f) {

                            LazyEntity destination = mPagerAdapter.getFragmentDestination(position);

                            mWritePanel.setDestination(destination);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mWritePanel.setDestination(mPagerAdapter.getFragmentDestination(position));
                        LazyEntity destination = mPagerAdapter.getFragmentDestination(position);

                        mWritePanel.setDestination(destination);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }

    @Override
    public void onDisconnect(ServerProfile profile, Bundle data) {
        if(mProfile != null) {
            finish();

            if(mIsNewTask) return;

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(ServerProfile profile, Bundle data, String[] rawLines) {

        if(mPagerAdapter == null) return;

        if(!mHistoryLoaded) return;

        if(data.containsKey("server")) {
            IRCServer server = mProfile.getServerParams();

            if(server == null) return;

            if(!data.getString("server").equals(mServerAddress))
                return;
        }

        IRCOutputFilter filter = new IRCOutputFilter(rawLines, true);

        LinkedHashMap<String, List<IRCBroadcastSignal>> signalsMap =
                filter.getFilteredOutputSignals(mIncompleteSignal);

        Object[] destinations = signalsMap.keySet().toArray();

        if(filter.getIncompleteSignal() != null)
            mIncompleteSignal = filter.getIncompleteSignal();

        for (Object destination1 : destinations) {
            final String destination = (String) destination1;

            if (destination.equals("@server") || destination.equals("=")) continue;

            int destIndex = mPagerAdapter.findFragmentIndexByDestinationName(destination);

            if (destIndex < 0) {
                createFragmentByDestination(destination);
                destIndex = mPagerAdapter.findFragmentIndexByDestinationName(destination);
                mPager.setCurrentItem(destIndex);
            }
        }

        mPagerAdapter.onReceive(signalsMap, data.getLong("date"));

        if (mIncompleteSignal != null && mIncompleteSignal instanceof IRCUsers) {
            String destination = mIncompleteSignal.destination;

            if(destination == null) return;

            int index = mPagerAdapter.findFragmentIndexByDestinationName(destination);

            mPagerAdapter.loadChannelUsers(index, (IRCUsers) mIncompleteSignal);
            mIncompleteSignal = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adjustLayout(newConfig);
    }

    private void adjustLayout(Configuration config) {
        float dp = getResources().getDisplayMetrics().scaledDensity;

        boolean isPortaitOrientation = config.orientation == Configuration.ORIENTATION_PORTRAIT;

        if(!HoloCompatSupport.isTablet(this)) {
            PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);

            tabStrip.getLayoutParams().height = isPortaitOrientation ? (int) (50 * dp) : (int) (44 * dp);
        }
    }

    public void createFragmentByDestination(final String destName) {
        PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);

        if(destName == null)
            return;

        final LazyEntity destination = destName.startsWith("#") || destName.startsWith("&") ?
                new ServerChannel(destName) : new ServerUser(destName);

        int index = mPagerAdapter.findFragmentIndexByDestinationName(destName);

        if(index < 0) {
            mPagerAdapter.addPage(destination);
            if(!isFinishing())
                mPagerAdapter.notifyDataSetChanged();
            tabStrip.notifyDataSetChanged();
        }
    }

    public WriteTextPanel getWritePanel() {
        return mWritePanel;
    }

    @Override
    public void onBackPressed() {
        if(!mIsNewTask)
            moveTaskToBack(true);
        else
            finish();
    }

    public void showMembersList(boolean value, String channelName) {
        if(!channelName.startsWith("#") && !channelName.startsWith("&")) return;
        if(value) {
            if(!HoloCompatSupport.isTablet(this)) {
                Intent intent = new Intent(this, ChannelMembersActivity.class);
                intent.putExtra("prefsName", mProfile.name);
                intent.putExtra("channelName", channelName);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void registerReceivers(ServerProfile profile) {
        super.registerReceivers(profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIRCReceiver != null)
            mIRCReceiver.setContext(this);
    }

    public void removeFragment(final LazyEntity destination) {
        if(destination == null) return;

        final PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);

        mPager.setCurrentItem(0);
        new Handler(Looper.myLooper()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mPagerAdapter.removePage(destination);
                        tabStrip.notifyDataSetChanged();
                    }
                },
        200);
    }

    public String getCurrentNickname() {
        if(mClientService == null) return null;
        return mClientService.getWrapper(mProfile).getCurrentNickname();
    }
}