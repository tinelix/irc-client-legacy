package dev.tinelix.irc.android.legacy.core.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;

import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private SortedMap<String, Charset> charsets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        charsets = Charset.availableCharsets();
    }

    private void showAboutApplication() {
        Intent intent = new Intent(this, AboutApplicationActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_application_item) {
            showAboutApplication();
        }

        return super.onOptionsItemSelected(item);
    }

}
