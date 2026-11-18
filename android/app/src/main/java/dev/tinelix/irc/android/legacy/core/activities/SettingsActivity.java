package dev.tinelix.irc.android.legacy.core.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.fragments.IRCProfileSettingsFragment;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;

public class SettingsActivity extends BaseActivity
{
    public String mPrefsName;
    private Bundle mArguments;

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

        installLayouts();
    }

    @SuppressWarnings("ConstantConditions")
    private void installLayouts() {

        if(mArguments == null || !mArguments.containsKey("fragment"))
            return;

        Fragment fragment = null;
        String tag = mArguments.getString("fragment");
        mPrefsName = mArguments.getString("prefsName");

        switch (tag) {
            case "irc_profile_settings":
                fragment = new IRCProfileSettingsFragment();
                break;
        }

        if(fragment == null)
            return;

        fragment.setArguments(mArguments);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment, tag);
        ft.commit();

        findViewById(R.id.layout_placeholder).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackPressed() {
        if(mArguments != null && mArguments.containsKey("fragment") &&
            mArguments.getString("fragment").equals("irc_profile_settings")) {
            SharedPreferences prefs = getSharedPreferences(mPrefsName, 0);
            UsersCacheDatabase.initDatabase(this, prefs.getString("profileId", "0"));

            if(UsersCacheDatabase.getNicknameList().size() == 0) {
                Toast.makeText(this, R.string.add_nickname_first, Toast.LENGTH_LONG).show();
                UsersCacheDatabase.freeDatabase();
                return;
            }
        }
        super.onBackPressed();
    }
}