package dev.tinelix.irc.android.legacy.core.activities.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

public class BaseFragmentActivity extends FragmentActivity {

    private SharedPreferences appPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences getAppPreferences() {
        return appPrefs;
    }
}
