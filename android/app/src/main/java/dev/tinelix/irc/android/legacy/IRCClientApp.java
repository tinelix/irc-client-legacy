package dev.tinelix.irc.android.legacy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class IRCClientApp extends Application {

    String prefsDir;

    @Override
    public void onCreate() {
        String package_name = getApplicationContext().getPackageName();
        prefsDir = getFilesDir().getPath() + "/shared_prefs";

        File prefs_directory = new File(prefsDir);
        File[] prefs_files = prefs_directory.listFiles();
        SharedPreferences global_prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = global_prefs.edit();


        if(prefs_files != null) {
            String file_extension;
            Context context = getApplicationContext();

        }

        if(!global_prefs.contains("theme")) {
            editor.putString("theme", "Dark");
        }
        if(!global_prefs.contains("language")) {
            editor.putString("language", "OS dependent");
        }
        if(!global_prefs.contains("show_msg_timestamps")) {
            editor.putBoolean("show_msg_timestamps", false);
        }
        editor.commit();
        super.onCreate();
    }
}
