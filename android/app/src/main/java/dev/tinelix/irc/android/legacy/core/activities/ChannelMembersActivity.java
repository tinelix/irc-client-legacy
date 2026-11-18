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

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.adapters.ChannelMembersAdapter;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.ServerChannel;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class ChannelMembersActivity extends BaseActivity {
    private ServerProfile mProfile;
    private Bundle mArguments;
    private SharedPreferences mProfilePrefs;
    private String mChannelName;
    private RecyclerView mListView;
    private IRCUsers mChannelMembers;
    private ChannelMembersAdapter mListAdapter;
    private View mPlaceholderLayout;

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

                if(mProfile != null) {
                    loadSocketHistory();
                }
            }
        };

        mArguments = getIntent().getExtras();

        if(mArguments == null) {
            finish();
            return;
        }

        if (mArguments.containsKey("prefsName") && mArguments.containsKey("channelName")) {
            mProfilePrefs = getSharedPreferences(
                    mArguments.getString("prefsName"), 0
            );
            ServersCacheDatabase.initDatabase(
                    this, mProfilePrefs.getString("profileId", "")
            );
            mChannelName = mArguments.getString("channelName");

            if(mChannelName == null || !mChannelName.startsWith("#") && !mChannelName.startsWith("&")) {
                finish();
                return;
            }

            loadIRCProfile();
            installLayouts();
            bindService(mProfile);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void installLayouts() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        mListView = new RecyclerView(this);
        mListView.setLayoutParams(
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        );

        mListView.setLayoutManager(new LinearLayoutManager(this));

        mPlaceholderLayout = View.inflate(this, R.layout.layout_placeholder, null);
        mPlaceholderLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        mPlaceholderLayout.setVisibility(View.GONE);

        layout.addView(mListView);
        layout.addView(mPlaceholderLayout);

        setContentView(layout);
    }

    private void loadIRCProfile() {
        String serverAddr = mProfilePrefs.getString("serverAddress", "");

        mProfile = ServerProfile.from(
                mArguments.getString("prefsName"),
                mProfilePrefs
        );

        mProfile.setServerParams(ServersCacheDatabase.getServer(serverAddr));

        getSupportActionBar().setSubtitle(String.format("%s, %s", serverAddr, mChannelName));
    }

    private void loadSocketHistory() {
        mChannelMembers = mClientService.getChannelMembersFromHistory(mProfile, mChannelName);

        if(mChannelMembers == null) {
            setPlaceholderLayout();
            return;
        }

        mListAdapter = new ChannelMembersAdapter(
                this, new ServerChannel(mChannelName), mChannelMembers.getList()
        );

        mListView.setAdapter(mListAdapter);
    }

    private void setPlaceholderLayout() {
        TextView titleTv = mPlaceholderLayout.findViewById(R.id.placeholder_title);
        TextView descriptionTv = mPlaceholderLayout.findViewById(R.id.placeholder_description);

        titleTv.setText(R.string.no_channel_members_title);
        descriptionTv.setText(R.string.no_channel_members_description);

        findViewById(R.id.placeholder_button).setVisibility(View.GONE);

        boolean hasMembers = mListAdapter != null && mListAdapter.getItemCount() == 0;

        mPlaceholderLayout.setVisibility(hasMembers ? View.GONE : View.VISIBLE);
        mListView.setVisibility(hasMembers ? View.VISIBLE : View.GONE);
    }
}
