package dev.tinelix.irc.android.legacy.core.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;

public class CustomNicknamesActivity extends BaseActivity {

    String[] mNicknamesArray;
    String mNicknamesString;
    String mProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences profilePrefs =
                getApplicationContext().getSharedPreferences(mProfileName, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(getActionBar() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    getActionBar().setHomeButtonEnabled(true);
                }
                getActionBar().setTitle(getResources().getString(R.string.nicknames_manager_title));
            }
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            mProfileName = extras == null ? null : extras.getString("profile_name");
        } else {
            mProfileName = (String) savedInstanceState.getSerializable("profile_name");
        }

        mNicknamesArray = profilePrefs.getString("nicknames", "").split(", ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nicknames_manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.add_nickname_item) {

        } else if (id == R.id.clear_nicknames_item) {
            Context context = getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences(mProfileName, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nicknames", "");
            editor.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreatingNicknames(String value) {

    }
}
