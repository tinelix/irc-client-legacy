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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.IRCApplication;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.MainActivity;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.adapters.ProfilesListAdapter;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class HomePageFragment extends BaseFragment {
    private RecyclerView mListView;
    private LinearLayout mPlaceholderLayout;
    private SharedPreferences mAppPrefs;
    private IRCApplication mApp;
    private ArrayList<ServerProfile> mProfilesArray;
    private ProfilesListAdapter mListAdapter;
    private int mPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
            mPosition = getArguments().getInt("pos");
        mAppPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout layout = new LinearLayout(getContext());

        layout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        mPlaceholderLayout = (LinearLayout) View.inflate(getContext(), R.layout.layout_placeholder, null);
        mPlaceholderLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        installPlaceholderLayout();

        mListView = new RecyclerView(getContext());
        mListView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        mListView.setVisibility(View.GONE);

        layout.addView(mListView);
        layout.addView(mPlaceholderLayout);

        return layout;
    }

    private void installPlaceholderLayout() {
        TextView placeholderTitle = mPlaceholderLayout.findViewById(R.id.placeholder_title);
        TextView placeholderDesc  = mPlaceholderLayout.findViewById(R.id.placeholder_description);
        Button placeholderButton  = mPlaceholderLayout.findViewById(R.id.placeholder_button);

        if(mPosition == 0) {
            placeholderTitle.setText(getResources().getString(R.string.empty_placeholder_title));
            placeholderDesc.setText(getResources().getString(R.string.empty_profiles_list_description));
        } else {
            placeholderTitle.setText(getResources().getString(R.string.under_construction_title));
            placeholderDesc.setText(getResources().getString(R.string.under_construction_description));
            placeholderButton.setVisibility(View.GONE);
        }
        placeholderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IRCProfileManager.showCreateProfileDialog(getContext());
            }
        });
    }

    @SuppressLint("SdCardPath")
    public void loadProfilesList() {
        String  profilesPath;
        if(getContext().getFilesDir().getParent() != null && getContext().getFilesDir() != null)
            profilesPath = getContext().getFilesDir().getParent() + "/shared_prefs";
        else
            profilesPath = "/data/data/" + mApp.getPackageName() + "/shared_prefs";

        File    prefsDir = new File(profilesPath);
        File[]  prefsFiles = prefsDir.listFiles();
        String  fileExt;

        mApp = (IRCApplication) getActivity().getApplication();

        if(mProfilesArray == null)
            mProfilesArray = new ArrayList<>();
        else
            mProfilesArray.clear();

        try {
            for (File file : prefsFiles) {
                String filename = file.getName();
                if (file.getName().startsWith("profile") && file.getName().endsWith(".xml")) {
                    SharedPreferences prefs =
                            getContext().getSharedPreferences(
                                    filename.substring(0, filename.length() - 4),
                                    0
                            );


                    if(!prefs.contains("profileId"))
                        continue;

                    ServersCacheDatabase.initDatabase(
                            getContext(), prefs.getString("profileId", "0")
                    );

                    String name = prefs.getString("profileName", "");
                    String serverAddress = prefs.getString("serverAddress", "");

                    if (serverAddress.length() > 0 && name.length() > 0) {
                        IRCServer server = ServersCacheDatabase.getServer(serverAddress);

                        if(server != null) {
                            ServerProfile profile = ServerProfile.from(
                                    filename.substring(0, filename.length() - 4),
                                    prefs
                            );

                            mProfilesArray.add(profile);
                        }
                    }

                    ServersCacheDatabase.freeDatabase();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void createAdapter(Context context) {

        if(mPosition == 0) {
            loadProfilesList();

            if (mListAdapter == null) {
                mListAdapter = new ProfilesListAdapter(
                        getContext(), (BaseActivity) getActivity(), mProfilesArray
                );
                mListView.setLayoutManager(new LinearLayoutManager(getContext()));
                mListView.setAdapter(mListAdapter);
            } else {
                mListAdapter.notifyDataSetChanged();
            }
        }

        if(mListAdapter == null) return;

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

        mPlaceholderLayout.setVisibility(mProfilesArray.size() > 0 ? View.GONE : View.VISIBLE);
        mListView.setVisibility(mProfilesArray.size() > 0 ? View.VISIBLE : View.GONE);
        mListAdapter.setOnItemChangeListener(
                new ProfilesListAdapter.OnItemChangeListener() {
                    @Override
                    public void onItemChange(int position) {

                        if(getActivity() instanceof MainActivity)
                            ((MainActivity) getActivity()).setNewProfileMenuItemVisibility(
                                    mListAdapter.getItemCount() > 0
                            );

                        if(mListAdapter.getItemCount() > 0) {
                            mPlaceholderLayout.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        } else {
                            mPlaceholderLayout.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }

    public static HomePageFragment createInstance(int position) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();

        args.putInt("pos", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return mProfilesArray != null ? mProfilesArray.size() : 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfilesList();
        createAdapter(getContext());
    }
}
