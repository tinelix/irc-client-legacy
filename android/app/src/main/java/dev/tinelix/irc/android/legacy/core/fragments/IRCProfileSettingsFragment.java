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

package dev.tinelix.irc.android.legacy.core.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.tinelix.holoexperience.library.app.HoloAlertDialog;
import dev.tinelix.holoexperience.library.preference.HoloCompatPreferenceFragment;
import dev.tinelix.irc.android.legacy.BuildConfig;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.NicknamesManagerActivity;
import dev.tinelix.irc.android.legacy.databases.ServersCacheDatabase;
import dev.tinelix.irc.android.legacy.databases.UsersCacheDatabase;
import dev.tinelix.irc.android.legacy.utils.AESCipher;
import dev.tinelix.irc.android.legacy.utils.IRCProfileManager;
import dev.tinelix.irc.android.support.models.IRCServer;
import dev.tinelix.irc.android.support.models.ServerProfile;

public class IRCProfileSettingsFragment extends HoloCompatPreferenceFragment {

    private String mPrefsName;
    private SharedPreferences mPreferences;
    private IRCServer mIRCServer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.profile_settings);

        if(getArguments() != null) {
            Bundle data = getArguments();

            if(data.containsKey("prefsName")) {
                mPrefsName = getArguments().getString("prefsName");
                mPreferences = getContext().getSharedPreferences(mPrefsName, 0);
            }
        }

        ServersCacheDatabase.initDatabase(
                getContext(), mPreferences.getString("profileId", "0")
        );
        UsersCacheDatabase.initDatabase(
                getContext(), mPreferences.getString("profileId", "0")
        );

        setupPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setupPreferences() {
        final String profileName        = mPreferences.getString("profileName", "");
        final String realName           = mPreferences.getString("realName", "Tinelix IRC User");
        final String hostName           = mPreferences.getString("hostName", "");
        final String authMethod         = mPreferences.getString("authMethod", "None");
        final String serverAddress      = mPreferences.getString("serverAddress", "");
        final String quitMessage        = mPreferences.getString(
                "quitMessage", String.format(
                        "Tinelix IRC %s for Android | %s",
                        BuildConfig.VERSION_NAME,
                        BuildConfig.APP_WEBSITE.replace("http://", "")
                                               .replace("https://", "")
                )
        );
        final CheckBoxPreference useTlsConnectionPref = (CheckBoxPreference) findPreference("tls_only");

        mIRCServer = ServersCacheDatabase.getServer(serverAddress);

        final Preference profileNamePref = findPreference("profile_name");
        profileNamePref.setSummary(profileName);
        profileNamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);
                String value = mPreferences.getString("profileName", "");
                createTextEditDialog(
                        preference, view, "profileName", value, false, false
                );
                return true;
            }
        });

        final Preference realNamePref = findPreference("realname");
        realNamePref.setSummary(realName);
        realNamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);
                String value = mPreferences.getString("realName", "");
                createTextEditDialog(
                        preference, view, "realName", value, false, false
                );
                return true;
            }
        });

        final Preference hostNamePref = findPreference("hostname");
        hostNamePref.setSummary(hostName);
        hostNamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);
                String value = mPreferences.getString("hostName", "");
                createTextEditDialog(
                        preference, view, "hostName", value, false, false
                );
                return true;
            }
        });

        final Preference nicknamesManagementPref = findPreference("nicknames_management");

        nicknamesManagementPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), NicknamesManagerActivity.class);
                intent.putExtra("prefsName", mPrefsName);
                intent.putExtra("fragment", "nicknames_manager");
                startActivity(intent);
                return false;
            }
        });

        final Preference authPasswordPref = findPreference("auth_password");

        final Preference authMethodPref = findPreference("auth_method");

        if(authMethodPref != null) {
            final LinkedHashMap<String, String> authMethodsMap = IRCProfileManager.getAvailableAuthMethods(getContext());

            if (authMethodsMap.containsKey(authMethod)) {
                authMethodPref.setSummary(authMethodsMap.get(authMethod));
                if(!authMethod.equals("none"))
                    authPasswordPref.setEnabled(true);
            }

            authMethodPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    openAuthMethodsListDialog(preference, authPasswordPref, useTlsConnectionPref, authMethodsMap);
                    return true;
                }
            });
        }

        if(authPasswordPref != null) {
            authPasswordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);

                    createTextEditDialog(
                            preference, view, "authCipherPasswd", "", false, true
                    );
                    return true;
                }
            });
        }

        final Preference serverAddressPref = findPreference("server_address");
        serverAddressPref.setSummary(serverAddress);
        serverAddressPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                String value = mPreferences.getString("serverAddress", "");

                final View view = View.inflate(getContext(), R.layout.dialog_enter_server_address, null);

                final EditText serverDomainEdit = view.findViewById(R.id.server_domain_edit);
                final EditText serverPortEdit = view.findViewById(R.id.server_port_edit);

                final HoloAlertDialog dialog = new HoloAlertDialog.Builder(getContext())
                        .setTitle(R.string.pref_server_address)
                        .setView(view)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = mPreferences.edit();

                                String address = String.format(
                                        "%s:%s",
                                        serverDomainEdit.getText().toString(),
                                        serverPortEdit.getText().toString()
                                );

                                editor.putString("serverAddress", address);

                                mIRCServer.address = serverDomainEdit.getText().toString();
                                mIRCServer.port = Integer.parseInt(serverPortEdit.getText().toString());
                                mIRCServer.setInputTextEncoding("windows-1251");
                                mIRCServer.setOutputTextEncoding("windows-1251");

                                if(!ServersCacheDatabase.ifExist(address))
                                    ServersCacheDatabase.putServer(mIRCServer);

                                editor.commit();
                                dialog.dismiss();

                                serverAddressPref.setSummary(address);
                            }
                        })
                        .setNegativeButton(R.string.cancel_button, null)
                        .show();

                String[] serverAddressArray = value.split(":");

                serverDomainEdit.setText(serverAddressArray[0]);
                serverPortEdit.setText(serverAddressArray[1]);

                serverDomainEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String text = serverDomainEdit.getText().toString();

                        if(dialog.getDialogView() == null)
                            dialog.findViewById(android.R.id.button1).setEnabled(
                                    text.length() > 0 && isValidCharactersInAddress(text)
                            );
                        else
                            dialog.getDialogView().findViewById(android.R.id.button1).setEnabled(
                                    text.length() > 0 && isValidCharactersInAddress(text)
                            );
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                serverPortEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = serverPortEdit.getText().toString();

                        if(dialog.getDialogView() == null)
                            dialog.findViewById(android.R.id.button1).setEnabled(text.length() > 1);
                        else
                            dialog.getDialogView().findViewById(android.R.id.button1).setEnabled(text.length() > 1);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                CheckBox passwordCheckBox = view.findViewById(R.id.access_by_password_cb);

                passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        EditText passwordEdit = view.findViewById(R.id.server_password_edit);
                        passwordEdit.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                });

                boolean isOkButtonEnabled =
                        (serverAddressArray[0].length() > 0 && serverAddressArray[1].length() > 1 &&
                         isValidCharactersInAddress(serverAddressArray[0]));

                if(dialog.getDialogView() == null)
                    dialog.findViewById(android.R.id.button1).setEnabled(isOkButtonEnabled);
                else
                    dialog.getDialogView().findViewById(android.R.id.button1).setEnabled(isOkButtonEnabled);
                return true;
            }
        });

        final Preference pingIntervalPref = findPreference("ping_interval");

        int pingIntervalValue = mPreferences.getInt("pingInterval", 60000);

        pingIntervalPref.setSummary(
                IRCProfileManager.getPingIntervalSummary(getContext(), pingIntervalValue)
        );

        pingIntervalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int value = mPreferences.getInt("pingInterval", 60000);

                final View view = View.inflate(getContext(), R.layout.dialog_ping_interval, null);

                final SeekBar seekBar = view.findViewById(R.id.ping_interval_seek);
                final TextView intervalValueTv = view.findViewById(R.id.ping_interval_label);

                final HoloAlertDialog dialog = new HoloAlertDialog.Builder(getContext())
                        .setTitle(R.string.pref_ping_interval)
                        .setView(view)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = mPreferences.edit();
                                switch (seekBar.getProgress()) {
                                    case 0:
                                        editor.putInt("pingInterval", 10000);
                                        break;
                                    case 1:
                                        editor.putInt("pingInterval", 30000);
                                        break;
                                    case 2:
                                        editor.putInt("pingInterval", 60000);
                                        break;
                                    case 3:
                                        editor.putInt("pingInterval", 90000);
                                        break;
                                }

                                editor.commit();

                                pingIntervalPref.setSummary(
                                        IRCProfileManager.getPingIntervalSummary(
                                                getContext(), mPreferences.getInt("pingInterval", 60000)
                                        )
                                );

                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.cancel_button, null)
                          .create();

                dialog.show();

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int value = 60000;

                        switch (progress) {
                            case 0:
                                value = 10000;
                                break;
                            case 1:
                                value = 30000;
                                break;
                            case 2:
                                value = 60000;
                                break;
                            case 3:
                                value = 90000;
                                break;
                        }

                        intervalValueTv.setText(
                                IRCProfileManager.getPingIntervalSummary(
                                        getContext(), value
                                )
                        );
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                int progress = 2;

                switch (value) {
                    case 10000:
                        progress = 0;
                        break;
                    case 30000:
                        progress = 1;
                        break;
                    case 60000:
                        progress = 2;
                        break;
                    case 90000:
                        progress = 3;
                        break;
                }

                seekBar.setProgress(progress);
                seekBar.setMax(3);

                return false;
            }
        });

        final Preference textEncodingPref = findPreference("text_encoding");
        final LinkedHashMap<Integer, String> textEncodingMap =
                IRCProfileManager.getAvailableTextEncodingsMap2(getContext());

        if(textEncodingMap.containsKey(mIRCServer.inputEncoding))
            textEncodingPref.setSummary(textEncodingMap.get(mIRCServer.inputEncoding));

        textEncodingPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                int defPosition = IRCProfileManager.getTextEncodingCurrentIndex(mIRCServer.inputEncoding);
                final String[] currentValueStr = {IRCProfileManager.getTextEncodingCurrentString(defPosition)};

                new HoloAlertDialog.Builder(getContext())
                        .setTitle(R.string.pref_text_encoding)
                        .setSingleChoiceItems(R.array.encoding_array, defPosition, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 currentValueStr[0] = IRCProfileManager.getTextEncodingCurrentString(which);
                            }
                        })
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                int index = IRCProfileManager.getTextEncodingCurrentIndex(currentValueStr[0]);

                                preference.setSummary(textEncodingMap.get(index));
                                mIRCServer.setOutputTextEncoding(currentValueStr[0]);
                                mIRCServer.setInputTextEncoding(currentValueStr[0]);
                                updateServerInfoDb();
                            }
                        })
                        .setNegativeButton(R.string.cancel_button, null)
                        .show();
                return true;
            }
        });

        useTlsConnectionPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mIRCServer.tlsEnabled = (boolean) o;
                updateServerInfoDb();
                return false;
            }
        });

        final CheckBoxPreference hideIPAddressPref = (CheckBoxPreference) findPreference("hide_ip");

        if(hideIPAddressPref != null) {
            hideIPAddressPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    mIRCServer.hideIP = (boolean) o;
                    updateServerInfoDb();
                    return false;
                }
            });
        }

        final Preference quitMessagePref = findPreference("quit_message");
        quitMessagePref.setSummary(quitMessage);
        quitMessagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                View view = View.inflate(getContext(), R.layout.dialog_enter_text, null);
                String value = mPreferences.getString(
                        "quitMessage", String.format(
                                "Tinelix IRC %s for Android | %s",
                                BuildConfig.VERSION_NAME,
                                BuildConfig.APP_WEBSITE
                                        .replace("http://", "")
                                        .replace("https://", "")
                        )
                );
                createTextEditDialog(
                        preference, view, "quitMessage", value,
                        true,
                        false);
                return true;
            }
        });
    }

    private void openAuthMethodsListDialog(
            final Preference authMethodsPref,
            final Preference authPasswordPref,
            final CheckBoxPreference useTlsPref,
            final LinkedHashMap<String, String> map
     ) {
        String value = mPreferences.getString("authMethod", "");
        int defPosition = IRCProfileManager.getAuthMethodsIndex(value);
        final String[] currentValue = {(String) map.keySet().toArray()[defPosition]};

        new HoloAlertDialog.Builder(getContext())
                .setTitle(R.string.pref_login_method)
                .setSingleChoiceItems(R.array.auth_method, defPosition,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentValue[0] = (String) map.keySet().toArray()[which];
                            }
                        }
                )
                .setPositiveButton(R.string.ok_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if(currentValue[0].equals("nickserv") && !useTlsPref.isChecked())
                                    createNickServWarningDialog(authMethodsPref, authPasswordPref, map, currentValue);
                                else {
                                    SharedPreferences.Editor editor = mPreferences.edit();
                                    editor.putString("authMethod", currentValue[0]);
                                    editor.commit();
                                    authMethodsPref.setSummary(map.get(currentValue[0]));
                                    authPasswordPref.setEnabled(!currentValue[0].equals("none"));
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.cancel_button, null)
                .show();
    }

    private void createNickServWarningDialog(
            final Preference authMethodsPref,
            final Preference authPasswordPref,
            final LinkedHashMap<String, String> map,
            final String[] values
    ) {
        HoloAlertDialog dialog =
                new HoloAlertDialog.Builder(getContext())
                                   .setTitle("NickServ")
                                   .setMessageText(R.string.dialog_non_secured_nickserv)
                                   .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                           SharedPreferences.Editor editor = mPreferences.edit();
                                           editor.putString("authMethod", "nickserv");
                                           editor.commit();
                                           authMethodsPref.setSummary(map.get(values[0]));
                                           authPasswordPref.setEnabled(true);
                                       }
                                   })
                                   .setNegativeButton(android.R.string.cancel, null)
                                   .create();

        dialog.show();
    }

    private void updateServerInfoDb() {
        if(mIRCServer != null)
            ServersCacheDatabase.updateServer(mIRCServer);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if(preference instanceof CheckBoxPreference) {
            boolean value = !((CheckBoxPreference) preference).isChecked();
            ((CheckBoxPreference) preference).setChecked(value);
        }

        return super.onPreferenceTreeClick(preference);
    }

    private void createTextEditDialog(
            final Preference preference, View view,
            final String key, String value,
            boolean multiLine, final boolean passwordType) {

        final EditText editText = view.findViewById(R.id.edittext);

        int titleId = 0;

        switch (key) {
            case "profileName":
                titleId = R.string.profile_name;
                break;
            case "realName":
                titleId = R.string.pref_realname;
                break;
            case "hostName":
                titleId = R.string.pref_hostname;
                break;
            case "authMethod":
                titleId = R.string.pref_login_method;
                break;
            case "authCipherPasswd":
                titleId = R.string.pref_password;
                break;
            case "serverAddress":
                titleId = R.string.pref_server_address;
                break;
            case "quitMessage":
                titleId = R.string.pref_quit_message;
                break;
        }

        new HoloAlertDialog.Builder(getContext())
                .setTitle(titleId)
                .setView(view)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = mPreferences.edit();

                        if(!passwordType) {
                            editor.putString(key, editText.getText().toString());
                            editor.commit();
                        } else {
                            ArrayList<ServerProfile.Nickname> nicknames =
                                    UsersCacheDatabase.getNicknameList();

                            for(int i = 0; i < nicknames.size(); i++) {
                                UsersCacheDatabase.encryptPassword(
                                        nicknames.get(i), editText.getText().toString()
                                );
                            }
                        }

                        if(!passwordType)
                            preference.setSummary(editText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel_button, null)
                .show();

        if(passwordType)
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        else if(multiLine) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            editText.setSingleLine(false);
            editText.setMinLines(4);
            editText.setMaxLines(4);
            editText.setGravity(Gravity.TOP);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            editText.setSingleLine(true);
        }

        editText.setHint(titleId);
        editText.setText(value);
    }

    public boolean isValidCharactersInAddress(String address) {
        String regex = "[^a-zA-Zа-яА-ЯёЁ0-9.-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        return !matcher.find() &&
                !address.startsWith("-") &&
                !address.startsWith(".") &&
                !address.endsWith(".") &&
                !address.endsWith("-");
    }

    @Override
    public void onDestroy() {
        ServersCacheDatabase.freeDatabase();
        UsersCacheDatabase.freeDatabase();
        super.onDestroy();
    }
}
