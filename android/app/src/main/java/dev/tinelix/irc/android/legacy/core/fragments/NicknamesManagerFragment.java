package dev.tinelix.irc.android.legacy.core.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import dev.tinelix.holoexperience.library.app.HoloAlertDialog;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.NicknamesManagerActivity;
import dev.tinelix.irc.android.legacy.core.activities.SettingsActivity;
import dev.tinelix.irc.android.legacy.core.fragments.base.BaseFragment;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.adapters.NicknamesListAdapter;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class NicknamesManagerFragment extends BaseFragment {
    private String mPrefsName;
    private SharedPreferences mPreferences;
    private RecyclerView mListView;
    private ArrayList<ServerProfile.Nickname> mNicknames;
    private NicknamesListAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            Bundle data = getArguments();

            if(data.containsKey("prefsName")) {
                mPrefsName = getArguments().getString("prefsName");
                mPreferences = getContext().getSharedPreferences(mPrefsName, 0);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        float dp = getResources().getDisplayMetrics().scaledDensity;

        LinearLayout layout = new LinearLayout(getContext());

        FrameLayout createItemButton =
                (FrameLayout) View.inflate(getContext(), R.layout.layout_create_item, null);

        createItemButton.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)(56.0 * dp)
                )
        );
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);
                createTextEditDialog();
            }
        });

        mListView = new RecyclerView(getContext());
        mListView.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(createItemButton);
        layout.addView(mListView);

        connectDatabase();
        return layout;
    }

    public void createTextEditDialog() {
        IRCProfileManager.showCreateNicknameDialog(getContext(), mPrefsName);
    }

    public void setNicknamesArray(ArrayList<ServerProfile.Nickname> nicknames) {
        mNicknames = nicknames;
        mListAdapter.setArray(nicknames);
        mListAdapter.notifyDataSetChanged();
    }

    private void connectDatabase() {
        UsersCacheDatabase.initDatabase(
                getContext(), mPreferences.getString("profileId", "0")
        );
        mNicknames = UsersCacheDatabase.getNicknameList();
        setListAdapter();
    }

    private void setListAdapter() {

        if(mListAdapter == null) {
            mListAdapter = new NicknamesListAdapter(getContext(), mNicknames);
            mListAdapter.setOnItemChangeListener(
                    new NicknamesListAdapter.OnItemChangeListener() {
                        @Override
                        public void onItemChange(int position) {
                            if(getActivity() instanceof NicknamesManagerActivity)
                                ((NicknamesManagerActivity) getActivity()).setPlaceholderVisibillity(
                                        getItemCount() == 0
                                );
                        }
                    }
            );
        } else {
            mListAdapter.notifyDataSetChanged();
        }

        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListView.setAdapter(mListAdapter);

        if(getActivity() instanceof NicknamesManagerActivity) {
            ((NicknamesManagerActivity) getActivity()).setPlaceholderVisibillity(getItemCount() == 0);
        }
    }

    @Override
    public int getItemCount() {
        return mNicknames.size();
    }

    @Override
    public void onResume() {
        super.onResume();
        mNicknames = UsersCacheDatabase.getNicknameList();
        mListAdapter.setArray(mNicknames);
        setListAdapter();
    }

    @Override
    public void onDestroy() {
        UsersCacheDatabase.freeDatabase();
        super.onDestroy();
    }
}
