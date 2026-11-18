package dev.tinelix.irc.android.legacy.core.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.fragments.IRCProfileSettingsFragment;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;

public class ProfileSettingsActivity extends BaseActivity
{
    public String mProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installLayouts();
    }

    private void installLayouts() {
        IRCProfileSettingsFragment fragment = new IRCProfileSettingsFragment();

        if(getIntent().getExtras() != null)
            fragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment, "irc_profile_settings");
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
}