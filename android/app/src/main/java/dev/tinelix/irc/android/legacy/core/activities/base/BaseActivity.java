package dev.tinelix.irc.android.legacy.core.activities.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;

public class BaseActivity extends Activity {

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
