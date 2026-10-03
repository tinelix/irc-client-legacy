package dev.tinelix.irc.android.legacy.core.activities;

import android.os.Bundle;
import android.view.MenuItem;

import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;

public class ProfileSettingsActivity extends BaseActivity
{
    public String mProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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