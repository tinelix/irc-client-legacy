package dev.tinelix.irc.android.legacy.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

import dev.tinelix.holoexperience.library.app.HoloAlertDialog;
import dev.tinelix.irc.android.legacy.Global;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.NicknamesManagerActivity;
import dev.tinelix.irc.android.legacy.core.activities.SettingsActivity;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class IRCProfileManager {
    private static final String DEFAULT_IRC_SERVER = "irc.tinelix.ru:6667";

    public static void showCreateProfileDialog(final Context ctx) {

        float dp = ctx.getResources().getDisplayMetrics().scaledDensity;

        View view = View.inflate(ctx, R.layout.dialog_enter_text, null);

        final EditText editText = view.findViewById(R.id.edittext);

        HoloAlertDialog dialog = new HoloAlertDialog.Builder(ctx)
                .setView(view)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ServerProfile profile = makeProfile(ctx, editText.getText().toString());
                        startProfileSettingsActivity(ctx, profile);
                    }
                })
                .setNegativeButton(R.string.cancel_button, null)
                .show();

        if(dialog.getWindow() != null)
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            );
    }

    @SuppressWarnings("ConstantConditions")
    private static ServerProfile makeProfile(Context ctx, String fullProfileName) {
        Random random = new Random();
        int maxRand = 0xFFFFFF;
        boolean isExist;
        ServerProfile profile;
        String prefsName = fullProfileName.replaceAll("[^a-zA-Z0-9]", "");
        IRCServer server;

        ByteBuffer buffer = ByteBuffer.allocate(4);

        do {    // the 'do {} while ()' cycle must be executed only once
            final byte[] bytes = buffer.putInt(random.nextInt(maxRand)).array();
            final String randNumber = Global.bytesToHex(bytes);
            prefsName = prefsName.length() == 0 ?
                    String.format("profile_%s", randNumber) : String.format("profile_%s", prefsName);

            SharedPreferences prefs = ctx.getSharedPreferences(prefsName, 0);
            isExist = prefs.contains("profileName");

            profile = new ServerProfile(
                    prefsName,
                    fullProfileName.length() > 0 ?
                            fullProfileName :
                            ctx.getResources().getString(R.string.profile_default_name, randNumber),
                    null, false
            );

            if (isExist) {
                buffer.clear();
            } else {
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("profileName", profile.fullName);
                editor.putString("profileId", randNumber);
                editor.putString("authMethod", "none");
                editor.putInt("pingInterval", 60000);

                if(Build.BRAND.equals(Build.DEVICE))
                    editor.putString(
                            "hostName",
                            String.format(
                                    "%s_%s",
                                    Build.DEVICE.replaceAll("[^a-zA-Z0-9]", ""),
                                    Global.generateAndroidCodenames()
                            )
                    );
                else
                    editor.putString(
                            "hostName",
                            String.format(
                                    "%s_%s",
                                    Build.BRAND.replaceAll("[^a-zA-Z0-9]", ""),
                                    Build.DEVICE.replaceAll("[^a-zA-Z0-9]", "")
                            )
                    );

                editor.putString("serverAddress", DEFAULT_IRC_SERVER);
                editor.commit();

                ServersCacheDatabase.initDatabase(ctx, randNumber);

                server = new IRCServer(DEFAULT_IRC_SERVER);
                server.name = DEFAULT_IRC_SERVER.split(":")[0];
                server.tlsEnabled = false;
                server.hideIP = false;
                server.setInputTextEncoding("windows-1251");
                server.setOutputTextEncoding("windows-1251");

                ServersCacheDatabase.putServer(server);

                break;
            }
        } while (isExist);

        return profile;
    }

    public static LinkedHashMap<String, String> getAvailableTextEncodingsMap(Context context) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String[] array = context.getResources().getStringArray(R.array.encoding_array);

        //map.put("auto",             array[0]);
        map.put("utf-8",            array[0]);
        map.put("utf-16be",         array[1]);
        map.put("utf-16le",         array[2]);
        map.put("cp437",            array[3]);
        map.put("windows-1252",     array[4]);
        map.put("cp866",            array[5]);
        map.put("windows-1251",     array[6]);
        map.put("koi8-r",           array[7]);
        map.put("koi8-u",           array[8]);

        return map;
    }

    public static LinkedHashMap<Integer, String> getAvailableTextEncodingsMap2(Context context) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        String[] array = context.getResources().getStringArray(R.array.encoding_array);

        //map.put(IRCServer.TEXT_ENCODING_AUTO,                    array[0]);
        map.put(IRCServer.TEXT_ENCODING_UNICODE_UTF8,            array[0]);
        map.put(IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE,        array[1]);
        map.put(IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE,        array[2]);
        map.put(IRCServer.TEXT_ENCODING_DOS_ASCII,               array[3]);
        map.put(IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE,  array[4]);
        map.put(IRCServer.TEXT_ENCODING_DOS_CYRILLIC,            array[5]);
        map.put(IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC,        array[6]);
        map.put(IRCServer.TEXT_ENCODING_KOI8_RUSSIA,             array[7]);
        map.put(IRCServer.TEXT_ENCODING_KOI8_UKRAINE,            array[8]);

        return map;
    }

    public static LinkedHashMap<String, String> getAvailableAuthMethods(Context context) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String[] array = context.getResources().getStringArray(R.array.auth_method);

        map.put("none", array[0]);
        map.put("nickserv", array[1]);

        return map;
    }

    public static int getTextEncodingCurrentIndex(String value) {
        switch (value) {
            case "utf-8":
                return 0;
            case "utf-16be":
                return 1;
            case "utf-16le":
                return 2;
            case "cp437":
                return 3;
            case "windows-1252":
                return 4;
            case "cp866":
                return 5;
            case "windows-1251":
                return 6;
            case "koi8-r":
                return 7;
            case "koi8-u":
                return 8;
        }
        return 0;
    }

    public static String getTextEncodingCurrentString(int value) {
        switch (value) {
            case 0:
                return "utf-8";
            case 1:
                return "utf-16be";
            case 2:
                return "utf-16le";
            case 3:
                return "cp437";
            case 4:
                return "windows-1252";
            case 5:
                return "cp866";
            case 6:
                return "windows-1251";
            case 7:
                return "koi8-r";
            case 8:
                return "koi8-u";
        }
        return "utf-8";
    }

    public static int getTextEncodingCurrentIndex(int value) {
        switch (value) {
            case IRCServer.TEXT_ENCODING_UNICODE_UTF8:
                return 0;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE:
                return 1;
            case IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE:
                return 2;
            case IRCServer.TEXT_ENCODING_DOS_ASCII:
                return 3;
            case IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                return 4;
            case IRCServer.TEXT_ENCODING_DOS_CYRILLIC:
                return 5;
            case IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC:
                return 6;
            case IRCServer.TEXT_ENCODING_KOI8_RUSSIA:
                return 7;
            case IRCServer.TEXT_ENCODING_KOI8_UKRAINE:
                return 8;
        }
        return 0;
    }

    public static int getAuthMethodsIndex(String value) {

        if(value.equals("nickserv"))
            return 1;

        return 0;
    }

    public static void startProfileSettingsActivity(Context ctx, ServerProfile profile) {
        Intent intent = new Intent(ctx, SettingsActivity.class);
        intent.putExtra("prefsName", profile.name);
        intent.putExtra("fragment", "irc_profile_settings");
        ctx.startActivity(intent);
    }

    public static void showCreateNicknameDialog(final Context ctx, String prefsName) {

        final SharedPreferences prefs = ctx.getSharedPreferences(prefsName, 0);

        View view = View.inflate(ctx, R.layout.dialog_enter_text, null);
        final EditText editText = view.findViewById(R.id.edittext);
        int titleId = R.string.add_nickname_item;

        new HoloAlertDialog.Builder(ctx)
                .setTitle(titleId)
                .setView(view)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UsersCacheDatabase.putNickname(
                                new ServerProfile.Nickname(
                                        editText.getText().toString(), false
                                )
                        );

                        if(ctx instanceof NicknamesManagerActivity)
                            ((NicknamesManagerActivity) ctx).updateData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_button, null)
                .show();

        editText.setSingleLine(true);
        editText.setHint(R.string.your_nickname);
    }

    public static void deleteProfile(Context ctx, ServerProfile profile) {
        SharedPreferences preferences = ctx.getSharedPreferences(profile.name, 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.commit();
    }

    public static String getPingIntervalSummary(Context context, int value) {

        Resources res = context.getResources();

        String[] array = res.getStringArray(R.array.ping_intervals);

        switch (value) {
            case 10000:
                return array[0];
            case 30000:
                return array[1];
            case 60000:
                return array[2];
            case 90000:
                return array[3];
        }

        return null;
    }
}
