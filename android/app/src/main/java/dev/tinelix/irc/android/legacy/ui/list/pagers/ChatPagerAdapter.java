package dev.tinelix.irc.android.legacy.ui.list.pagers;

import android.content.Context;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Handler;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.ChatActivity;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.core.fragments.pages.ChatPageFragment;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.enitites.LazyEntity;
import dev.tinelix.irc.android.support.enitites.ServerUser;
import dev.tinelix.irc.android.support.filters.IRCOutputFilter;
import dev.tinelix.irc.android.support.models.IRCUsers;
import dev.tinelix.irc.android.support.models.ServerChannel;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class ChatPagerAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    private final ArrayList<BaseFragment> mFragments;
    private final ServerProfile mProfile;
    private final FragmentManager mFragmentManager;
    private boolean mHistoryLoaded;

    public ChatPagerAdapter(Context ctx, ServerProfile profile, FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mContext = ctx;
        mFragments = new ArrayList<>();
        mProfile = profile;

        mFragments.add(ChatPageFragment.createInstance(profile.name, "chat"));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return mContext.getResources().getStringArray(R.array.channel_tabs)[0];
        else {
            Fragment fragment = getItem(position);

            String destName = null;
            if(fragment != null && fragment instanceof ChatPageFragment)
                destName = ((ChatPageFragment) fragment).getDestinationName();

            if(destName != null)
                return destName;
        }

        return mContext.getResources().getStringArray(R.array.channel_tabs)[2];
    }

    public Fragment findFragmentByDestinationName(String destination) {

        synchronized (mFragments) {
            for (BaseFragment fragment : mFragments) {
                if (fragment instanceof ChatPageFragment) {
                    String channelName = ((ChatPageFragment) fragment).getDestinationName();

                    if (channelName != null && channelName.equals(destination))
                        return fragment;
                }
            }
        }
        return null;
    }

    public int findFragmentIndexByDestinationName(String destination) {

        if(destination == null)
            return -2;

        for(int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if(fragment instanceof ChatPageFragment) {
                String fragmentDestName = ((ChatPageFragment) fragment).getDestinationName();

                if(fragmentDestName == null)
                    continue;

                if (fragmentDestName.equals(destination))
                    return i;
            }
        }
        return -1;
    }

    public void loadSocketHistory(ChatClientService service) {

        HashMap<String, List<IRCBroadcastSignal>> signalsMap =
                service.createSignalsHistory(mProfile, true);

        if (signalsMap == null)
            return;

        Object[] destinations = signalsMap.keySet().toArray();

        for (int i = 0; i < signalsMap.size(); i++) {

            String destName = (String) destinations[i];

            if(destName.equals("@server")) {
                Fragment fragment = getItem(0);

                if(fragment instanceof ChatPageFragment)
                    ((ChatPageFragment) fragment).putSignalsList(signalsMap.get(destName));

                continue;
            }

            if (findFragmentIndexByDestinationName(destName) < 0) {
                Fragment fragment = addPage(
                        destName.startsWith("#") || destName.startsWith("&") ?
                                new ServerChannel(destName) : new ServerUser(destName)
                );

                if(fragment instanceof ChatPageFragment)
                    ((ChatPageFragment) fragment).putSignalsList(signalsMap.get(destName));
            }
        }
    }

    public void onReceive(LinkedHashMap<String, List<IRCBroadcastSignal>> signalsMap, long receivedDate) {

        for (Fragment fragment : mFragments) {
            if(fragment != null && fragment instanceof ChatPageFragment) {
                ChatPageFragment chatFragment = ((ChatPageFragment) fragment);

                String destinationName = chatFragment.getDestinationName();

                if(destinationName == null)
                    destinationName = "@server";

                boolean conditions = chatFragment.getItemCount() >= 0 &&
                                     signalsMap.containsKey(destinationName);

                if(conditions) {
                    ((ChatPageFragment) fragment).onReceive(
                            (ArrayList<IRCBroadcastSignal>) signalsMap.get(destinationName),
                            receivedDate
                    );
                }
            }
        }
    }

    public Fragment addPage(LazyEntity destination) {

        Fragment fragment = null;

        if(destination instanceof ServerChannel) {
            ServerChannel channel = (ServerChannel) destination;
            fragment = ChatPageFragment.createInstance(mProfile.name, "channel", channel.name);
            mFragments.add((BaseFragment) fragment);
        } else if(destination instanceof ServerUser) {
            ServerUser user = (ServerUser) destination;
            fragment = ChatPageFragment.createInstance(mProfile.name, "user", user.nickname);
            mFragments.add((BaseFragment) fragment);
        }

        return fragment;
    }

    public void loadChannelUsers(int position, IRCUsers users) {

        if(position <= 0) return;

        LazyEntity destination = getFragmentDestination(position);

        if(users == null)
            return;

        if(destination != null && destination instanceof ServerChannel) {
            ((ChatPageFragment) mFragments.get(position)).loadChannelUsers(users);
        }
    }

    public void removePage(LazyEntity destination) {
        int index = -1;

        if(destination instanceof ServerChannel) {
            ServerChannel channel = (ServerChannel) destination;
            index = findFragmentIndexByDestinationName(channel.name);
        } else if(destination instanceof ServerUser) {
            ServerUser user = (ServerUser) destination;
            index = findFragmentIndexByDestinationName(user.nickname);
        }

        if(index == -1)
            return;

        mFragments.remove(index);
        notifyDataSetChanged();
    }

    public LazyEntity getFragmentDestination(int position) {
        Fragment fragment = getItem(position);

        if(fragment instanceof ChatPageFragment)
            return ((ChatPageFragment) fragment).getDestination();

        return null;
    }

    public void putIRCSignal(IRCBroadcastSignal signal, int position) {
        Fragment fragment = getItem(position);

        if(fragment instanceof ChatPageFragment) {
            ((ChatPageFragment) fragment).putSignal(signal);
        }
    }
}
