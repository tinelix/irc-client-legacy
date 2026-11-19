/*
 * Copyleft © 2022, 2026 Dmitry Tretyakov (aka. Tinelix)
 *
 * This file is part of Tinelix IRC Client for Android.
 *
 * Tinelix IRC Client is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see https://www.gnu.org/licenses/.
 *
 * Source code: https://github.com/tinelix/irc-client-legacy
 */

package dev.tinelix.irc.android.legacy.core.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.SortedMap;

import dev.tinelix.holoexperience.library.HoloCompatSupport;
import dev.tinelix.holoexperience.library.app.HoloAlertDialog;
import dev.tinelix.holoexperience.library.test.HoloCompatTestActivity;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;
import dev.tinelix.irc.android.legacy.core.services.ChatClientService;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.ui.list.pagers.HomePagerAdapter;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;
import dev.tinelix.irc.android.support.filters.IRCOutputFilter;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;
import dev.tinelix.irc.android.support.wrappers.IRCSocketWrapper;

public class MainActivity extends BaseActivity {

    private HomePagerAdapter mPagerAdapter;
    private HoloAlertDialog mProgressDialog;
    private Menu mMenu;
    private boolean mCreateNewTask;
    private String mCurrentServerAddress;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isTaskRoot()) {
            Intent intent = getIntent();

            /* Prevents launching a new MainActivity from the launcher if a task
             * is already running in ChatActivity.
             */
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
               intent.getAction().equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras() != null) {
            Bundle arguments = getIntent().getExtras();

            if(arguments != null && arguments.containsKey("runningFrom")) {
                String runningFrom = arguments.getString("runningFrom");

                mCreateNewTask = runningFrom.equals(ChatActivity.class.getSimpleName());
                mCurrentServerAddress = arguments.getString("currentServer");
            }
        }

        preparePopupMenu(R.menu.main);
        installLayout();
        setActionBar();
    }

    @SuppressWarnings("deprecation")
    private void installLayout() {
        adjustLayout(getResources().getConfiguration());
        PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);
        ViewPager pager = findViewById(R.id.pager);

        mPagerAdapter = new HomePagerAdapter(this, getSupportFragmentManager());

        pager.setAdapter(mPagerAdapter);

        tabStrip.setTextColor(getResources().getColor(android.R.color.white));
        tabStrip.setIndicatorColorResource(R.color.holo_blue_light);
        tabStrip.setDividerColorResource(android.R.color.darker_gray);
        tabStrip.setUnderlineColorResource(R.color.holo_blue_light);
        tabStrip.setViewPager(pager);
        tabStrip.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if(positionOffset == 0.0f || positionOffset == 0.8f) {
                            mPagerAdapter.createListAdapter(position);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        mPagerAdapter.createListAdapter(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null) {
            Bundle arguments = getIntent().getExtras();

            if(arguments != null && arguments.containsKey("runningFrom")) {
                String runningFrom = arguments.getString("runningFrom");

                if(runningFrom != null) {
                    mCreateNewTask = runningFrom.equals(ChatActivity.class.getSimpleName());
                    mCurrentServerAddress = arguments.getString("currentServer");
                }
            }
        }
    }

    /*public void setPlaceholderVisibillity(boolean value) {
        findViewById(R.id.layout_placeholder).setVisibility(value ? View.VISIBLE : View.GONE);
        findViewById(R.id.fragment_container).setVisibility(value ? View.GONE : View.VISIBLE);
    }*/

    private void showAboutApplication() {
        Intent intent = new Intent(this, AboutApplicationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnect(ServerProfile profile, Bundle data) {

        if(profile == null) {
            mProgressDialog.dismiss();
            return;
        }

        if(mProgressDialog != null) {

            IRCSocketWrapper wrapper = getService().getWrapper(profile);

            if(wrapper == null)
                return;

            if(wrapper.getConnectionStatus() == IRCSocketWrapper.STATUS_CONNECTED) {
                TextView textView = mProgressDialog.findViewById(android.R.id.message);
                textView.setText(
                        getResources().getString(R.string.progress_profile_establishing)
                );
            } else {
                mProgressDialog.dismiss();
                startChatActivity(profile, wrapper, false);
            }
        }
    }

    @Override
    public void onReceive(ServerProfile profile, Bundle data, String[] rawLines) {

        if(mProgressDialog != null) {
            TextView textView = mProgressDialog.findViewById(android.R.id.message);
            IRCSocketWrapper wrapper = mClientService.getWrapper(profile);

            IRCOutputFilter filter = new IRCOutputFilter(rawLines, false);
            ArrayList<IRCBroadcastSignal> signals = (ArrayList<IRCBroadcastSignal>)
                            filter.getFilteredOutputSignals().get("@server");

            for(IRCBroadcastSignal signal : signals) {
                if (signal.type == IRCBroadcastSignal.IN_ERR_NICKNAMEINUSE)
                    textView.setText(
                            getResources().getString(
                                    R.string.progress_profile_nick_is_busy,
                                    wrapper.getCurrentNickname()
                            )
                    );
                else if (signal.type == IRCBroadcastSignal.IN_CMD_MOTD ||
                         signal.type == IRCBroadcastSignal.IN_ERR_NOMOTD) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;

                    startChatActivity(profile, wrapper, false);
                    break;
                }
            }
        }
    }

    private void startChatActivity(
            ServerProfile profile, IRCSocketWrapper wrapper, boolean forceSkip
    ) {

        IRCServer server = profile.getServerParams();

        if(mServerAddress.equals(server.address + ":" + server.port)) {

            if (mCreateNewTask && forceSkip) {
                Toast.makeText(this, R.string.connection_is_already_established, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        if(!mCreateNewTask) finish();

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("prefsName", profile.name);

        if(mCreateNewTask) {
            intent.putExtra("isNewTask", mCreateNewTask);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        startActivity(intent);
    }

    @Override
    public void onDisconnect(ServerProfile profile, Bundle data) {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            if(data.containsKey("error_code")) {
                switch (data.getInt("error_code")) {
                    case IRCSocketWrapper.STATUS_SERVER_NOT_FOUND:
                        Toast.makeText(this, R.string.no_connection_msg, Toast.LENGTH_LONG).show();
                        break;
                    case IRCSocketWrapper.STATUS_CONNECTION_TIMEOUT:
                        Toast.makeText(this, R.string.connection_timeout_msg, Toast.LENGTH_LONG).show();
                        break;
                }
                return;
            }
            Toast.makeText(this, R.string.connection_reset_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void startService(ServerProfile profile) {

        SharedPreferences prefs = getSharedPreferences(profile.name, 0);
        ServersCacheDatabase.initDatabase(this, prefs.getString("profileId", ""));
        UsersCacheDatabase.initDatabase(this, prefs.getString("profileId", ""));

        registerReceivers(profile);
        bindService(profile);

        if(mProgressDialog != null) {
            TextView textView = mProgressDialog.findViewById(android.R.id.message);
            textView.setText(
                    getResources().getString(R.string.progress_profile_initializing)
            );

            if(!mProgressDialog.isShowing())
                mProgressDialog.show();
            return;
        }

        mServerAddress = profile.getServerParams().address + ":" + profile.getServerParams().port;

        View view = View.inflate(this, R.layout.holo4ux_progress_dialog, null);

        mProgressDialog = new HoloAlertDialog.Builder(this)
                                                    .setView(view)
                                                    .create();

        TextView textView = view.findViewById(android.R.id.message);
        textView.setText(
                getResources().getString(R.string.progress_profile_initializing)
        );

        mProgressDialog.setCancelable(false);
        if(!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_item) {
            showAboutApplication();
        } else if (id == R.id.create_item) {
            IRCProfileManager.showCreateProfileDialog(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustLayout(newConfig);
    }

    private void adjustLayout(Configuration config) {
        float dp = getResources().getDisplayMetrics().scaledDensity;

        boolean isPortaitOrientation = config.orientation == Configuration.ORIENTATION_PORTRAIT;

        if(!HoloCompatSupport.isTablet(this)) {
            PagerSlidingTabStrip tabStrip = findViewById(R.id.tab_strip);

            tabStrip.getLayoutParams().height = isPortaitOrientation ? (int) (50 * dp) : (int) (44 * dp);
        }
    }

    public void setNewProfileMenuItemVisibility(boolean value) {
        if(mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.create_item);
            if(item != null)
                item.setVisible(value);
        }
    }
}
