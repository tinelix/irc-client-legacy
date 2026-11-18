package dev.tinelix.irc.android.legacy.core.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.fragments.IRCProfileSettingsFragment;
import dev.tinelix.irc.android.legacy.core.fragments.NicknamesManagerFragment;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class NicknamesManagerActivity extends BaseActivity {

    String[] mNicknamesArray;
    String mProfileName;
    private Bundle mArguments;
    private NicknamesManagerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        if(getIntent().getExtras() != null) {
            mArguments = getIntent().getExtras();
        } else {
            finish();
            return;
        }

        installFragment();
        installLayouts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nicksman, menu);
        return true;
    }

    private void installFragment() {
        if(mArguments == null || !mArguments.containsKey("fragment"))
            return;

        String tag = mArguments.getString("fragment");

        switch (tag) {
            case "nicknames_manager":
                mFragment = new NicknamesManagerFragment();
                break;
        }

        if(mFragment == null)
            return;

        mFragment.setArguments(mArguments);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, mFragment, tag);
        ft.commit();
    }

    public void setPlaceholderVisibillity(boolean value) {
        findViewById(R.id.layout_placeholder).setVisibility(value ? View.VISIBLE : View.GONE);
        findViewById(R.id.fragment_container).setVisibility(value ? View.GONE : View.VISIBLE);
    }

    private void installLayouts() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView placeholderTitle = findViewById(R.id.placeholder_title);
        TextView placeholderDesc = findViewById(R.id.placeholder_description);
        Button placeholderButton = findViewById(R.id.placeholder_button);

        placeholderTitle.setText(getResources().getString(R.string.empty_placeholder_title));
        placeholderDesc.setText(getResources().getString(R.string.empty_nicknames_list_description));
        placeholderButton.setText(getResources().getString(R.string.add_nickname_item));
        placeholderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IRCProfileManager.showCreateNicknameDialog(NicknamesManagerActivity.this, mProfileName);
            }
        });
    }

    public void updateData() {
        ArrayList<ServerProfile.Nickname> nicknames = UsersCacheDatabase.getNicknameList();
        mFragment.setNicknamesArray(nicknames);
        setPlaceholderVisibillity(nicknames.size() == 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_nicknames_item) {
            Context context = getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences(mProfileName, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nicknames", "");
            editor.commit();
            return true;
        } else if(id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
