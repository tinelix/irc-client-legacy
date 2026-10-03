package dev.tinelix.irc.android.legacy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

public class IRCApplication extends Application {

    String mPrefsDir;

    @Override
    public void onCreate() {
        super.onCreate();

        String package_name = getApplicationContext().getPackageName();
        mPrefsDir = getFilesDir().getPath() + "/shared_prefs";

        File prefs_directory = new File(mPrefsDir);
        File[] prefs_files = prefs_directory.listFiles();
        SharedPreferences global_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = global_prefs.edit();


        if(prefs_files != null) {
            String file_extension;
            Context context = getApplicationContext();
        }

        if(!global_prefs.contains("uiTheme")) {
            editor.putString("theme", "Dark");
        }
        if(!global_prefs.contains("uiLanguage")) {
            editor.putString("language", "System");
        }
        if(!global_prefs.contains("showMsgTimestamps")) {
            editor.putBoolean("showMsgTimestamps", false);
        }
        editor.commit();
    }
}
