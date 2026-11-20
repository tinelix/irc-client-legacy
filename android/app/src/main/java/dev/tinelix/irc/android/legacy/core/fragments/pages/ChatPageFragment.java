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

package dev.tinelix.irc.android.legacy.core.fragments.pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.adapters.BroadcastSignalsAdapter;
import dev.tinelix.irc.android.legacy.ui.list.adapters.ChannelMembersAdapter;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.IRCCommand;
import dev.tinelix.irc.android.support.enitites.LazyEntity;
import dev.tinelix.irc.android.support.enitites.ServerPong;
import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.ServerChannel;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class ChatPageFragment extends BaseFragment {
    private SharedPreferences mAppPrefs;
    private SharedPreferences mProfilePrefs;
    private ServerProfile mProfile;
    private RecyclerView mListView;
    private LinearLayout mLayout;
    private ArrayList<IRCBroadcastSignal> mSignalsList;
    private BroadcastSignalsAdapter mListAdapter;
    private LazyEntity mDestination;
    private RecyclerView mMembersListView;
    private ChannelMembersAdapter mMembersAdapter;
    private String mCurrentNick;
    private Timer mMembersUpdater;

    public ChatPageFragment() {
        mSignalsList = new ArrayList<>();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null) {
            mAppPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            mProfilePrefs = getContext().getSharedPreferences(args.getString("prefsName"), 0);
            mProfile = ServerProfile.from(args.getString("prefsName"), mProfilePrefs);

            mProfile.setServerParams(
                    ServersCacheDatabase.getServer(
                            mProfilePrefs.getString("serverAddress", "")
                    )
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        float dp = getResources().getDisplayMetrics().scaledDensity;

        mLayout = new LinearLayout(getContext());
        mLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        LinearLayout.LayoutParams listViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        listViewParams.weight = 1;

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);

        mListView = new RecyclerView(getContext());
        mListView.setLayoutParams(listViewParams);
        mListView.setLayoutManager(manager);
        mListView.setItemAnimator(null);

        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLayout.addView(mListView);

        if(HoloCompatSupport.isTablet(getContext()) &&
           mDestination != null && mDestination instanceof ServerChannel) {
            View divider = new View(getContext());
            divider.setLayoutParams(
                    new LinearLayout.LayoutParams(
                        (int)(dp), ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );
            divider.setBackgroundColor(getResources().getColor(R.color.holo_gray_twilight));
            mLayout.addView(divider);

            mMembersListView = new RecyclerView(getContext());
            mMembersListView.setLayoutManager(new LinearLayoutManager(getContext()));
            mMembersListView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            (int)(200 * dp), ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );
            mLayout.addView(mMembersListView);
        }

        if(getActivity() instanceof ChatActivity)
            mCurrentNick = ((ChatActivity) getActivity()).getCurrentNickname();

        loadChannelUsers();

        if(mSignalsList != null) setListAdapter();

        return mLayout;
    }


    @Override
    public void loadData(Context context) {
        if(mSignalsList == null)
            mSignalsList = new ArrayList<>();

        if(getView() != null) {
            createAdapter(context);
            createMembersAdapter();
        }
    }

    private void setListAdapter() {

        if(getContext() == null) return;

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        llm.setReverseLayout(true);

        if(mListAdapter == null) {
            mListAdapter = new BroadcastSignalsAdapter(getContext(), mSignalsList);
            mListView.setLayoutManager(llm);
            mListView.setAdapter(mListAdapter);
        } else {
            mListAdapter.setArray(mSignalsList);
            mListAdapter.notifyDataSetChanged();
        }

        /*
         * This code is a workaround for creating a list of items for a nested RecyclerView
         * when LinearLayoutManager does not automatically lay out the items.
         *
         * The bug is reproducible on Android JB and earlier versions.
         *
         * See more: https://stackoverflow.com/a/57675484/24295422
         */
        if(mListView.getChildCount() == 0) {
            mListView.swapAdapter(mListAdapter, false);
            mListView.smoothScrollToPosition(0);
        }
    }

    public void onReceive(ArrayList<IRCBroadcastSignal> signals, long receivedDate) {

        if(mSignalsList == null || signals == null)
            return;

        ChatClientService service = null;
        if(getActivity() != null && getActivity() instanceof ChatActivity)
             service = ((ChatActivity) getActivity()).getService();

        for(int i = 0; i < signals.size(); i++) {
            IRCBroadcastSignal signal = signals.get(i);
            signal.date = receivedDate;

            if(mDestination != null && mDestination instanceof ServerChannel) {
                if(signal.type == IRCBroadcastSignal.IN_CMD_TOPIC)
                    ((ServerChannel) mDestination).topic = signal.body;

                if(signal.type == IRCBroadcastSignal.IN_PART) {

                    if(mCurrentNick == null) return;

                    if(getActivity() instanceof ChatActivity) {
                        if(signal.sender.startsWith(mCurrentNick))
                            ((ChatActivity) getActivity()).removeFragment(mDestination);

                        return;
                    }
                }
            } else if(signal.type == IRCBroadcastSignal.IN_CMD_PONG &&
                      signal instanceof ServerPong) {

                if(service == null) return;

                ServerPong pong = ((ServerPong) signal);

                IRCSocketWrapper wrapper = service.getWrapper(mProfile);

                pong.value = wrapper.getCurrentPing();
                pong.prevValue = wrapper.getPreviousPing();
                pong.uptime = wrapper.getConnectionUptime();
            }

            signals.set(i, signal);
        }

        mSignalsList.addAll(0, signals);

        if(mListAdapter != null) {
            if (mListView.getLayoutManager() == null)
                mListView.setLayoutManager(new LinearLayoutManager(getContext()));

            mListAdapter.setArray(mSignalsList);
            mListAdapter.notifyDataSetChanged();
        } else {
            setListAdapter();
        }
    }

    public static ChatPageFragment createInstance(String prefsName, String tag) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("prefsName", prefsName);
        bundle.putString("tag", tag);

        fragment.setArguments(bundle);

        return fragment;
    }

    public static ChatPageFragment createInstance(String prefsName, String tag, String destName) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("prefsName", prefsName);
        bundle.putString("tag", tag);
        bundle.putString("destName", destName);

        fragment.setArguments(bundle);

        return fragment;
    }

    public static BaseFragment createInstance(String prefsName, String tag, String destName, IRCUsers users) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("prefsName", prefsName);
        bundle.putString("tag", tag);
        bundle.putString("destName", destName);

        fragment.setArguments(bundle);
        fragment.loadChannelUsers(users);

        return fragment;
    }

    public void loadChannelUsers() {

        if(mDestination instanceof ServerChannel) {
            if(!(getActivity() instanceof ChatActivity)) return;
            final ChatClientService service = ((ChatActivity) getActivity()).getService();

            if(service == null) return;
            final IRCSocketWrapper wrapper = service.getWrapper(mProfile);

            if(wrapper.getConnectionStatus() != IRCSocketWrapper.STATUS_RUNNING) return;

            if(mMembersUpdater == null) {
                mMembersUpdater = new Timer();
                mMembersUpdater.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        wrapper.sendCommand(
                                new IRCCommand("/who " + ((ServerChannel) mDestination).name),
                                mProfile.getServerParams().inputEncoding
                        );
                    }
                }, 200, 60000);
            }

            if(HoloCompatSupport.isTablet(getContext()))
                createMembersAdapter();
        }
    }

    private void createMembersAdapter() {
        if(mMembersListView == null) return;

        if(mDestination == null || !(mDestination instanceof ServerChannel)) return;

        ArrayList<ServerUser> users = ((ServerChannel) mDestination).members;

        if(users == null || users.size() == 0) return;

        if(getActivity() == null) return;

        if(mMembersAdapter == null) {
            mMembersAdapter = new ChannelMembersAdapter(
                    getActivity(), ((ServerChannel) mDestination),
                    users
            );
            mMembersListView.setAdapter(mMembersAdapter);
        } else {
            mMembersAdapter.notifyDataSetChanged();
        }

        if(mMembersListView.getChildCount() == 0) {
            mMembersListView.swapAdapter(mMembersAdapter, false);
            mMembersListView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        if(args == null)
            return;

        if(args.containsKey("destName")) {
            String destName = args.getString("destName");
            if(destName != null)
                mDestination = destName.startsWith("#") || destName.startsWith("&") ?
                                new ServerChannel(destName) : new ServerUser(destName);
        }
    }

    public String getDestinationName() {
        if(mDestination == null)
            return null;

        if(mDestination instanceof ServerChannel) {
            return ((ServerChannel) mDestination).name;
        } else if(mDestination instanceof ServerUser)
            return ((ServerUser) mDestination).nickname;

        return null;
    }

    @Override
    public int getItemCount() {
        if(mSignalsList != null)
            return mSignalsList.size();
        else
            return 0;
    }

    public LazyEntity getDestination() {
        return mDestination;
    }

    public void putSignal(IRCBroadcastSignal signal) {

        if(mSignalsList == null)
            return;

        mSignalsList.add(0, signal);
        mListAdapter.setArray(mSignalsList);

        mListAdapter.notifyItemInserted(0);
        if(mSignalsList.size() > 1)
            mListAdapter.notifyItemChanged(1);

        mListView.smoothScrollToPosition(0);
    }

    public void putSignalsList(List<IRCBroadcastSignal> signals) {
        if(mSignalsList == null)
            mSignalsList = (ArrayList<IRCBroadcastSignal>) signals;
        else {
            mSignalsList.addAll(signals);
        }

        if(mListView != null)
            setListAdapter();
    }

    public void loadChannelUsers(IRCUsers users) {

        if(!(mDestination instanceof ServerChannel))
            return;

        if(getDestinationName().equals(users.destination)) {
            if(users.destination.startsWith("#") || users.destination.startsWith("&")) {
                ((ServerChannel) mDestination).members = users.getList();
                createMembersAdapter();
            }
        }
    }
}
