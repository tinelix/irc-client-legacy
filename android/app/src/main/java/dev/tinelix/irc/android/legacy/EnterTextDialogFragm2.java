package dev.tinelix.irc.android.legacy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

@SuppressLint("NewApi")
public class EnterTextDialogFragm2 extends DialogFragment {

    public String current_parameter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences global_prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setLocale(global_prefs);
        current_parameter = ((ProfileSettingsActivity) getActivity()).getCurrentParameter();
        String current_value;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(current_parameter == "changing_profile_name") {
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(current_parameter);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.enter_text_activity, null);
            final EditText profile_name = (EditText) view.findViewById(R.id.profile_name_text);
            profile_name.setText(current_value);
            profile_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(profile_name.getText().toString().contains("/")) {
                        profile_name.setError(getResources().getString(R.string.text_field_wrong_characters));
                        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            builder.setView(view);
            builder.setTitle(R.string.enter_the_pfn_title);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText profile_name = (EditText) view.findViewById(R.id.profile_name_text);
                        ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, profile_name.getText().toString());
                    }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
            });
        } else if(current_parameter == "changing_auth_method") {
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(current_parameter);
            String[] auth_methods = getResources().getStringArray(R.array.auth_method);
            builder.setTitle(R.string.auth_method);
            if(current_value.contains("NickServ")) {
                builder.setSingleChoiceItems(auth_methods, 1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                String value;
                                if(item == 0) {
                                    value = "Disabled";
                                } else {
                                    value = "NickServ";
                                }
                                ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, value);
                            }
                        });
            } else {
                builder.setSingleChoiceItems(auth_methods, 0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            String value;
                            if(item == 0) {
                                value = "Disabled";
                            } else {
                                value = "NickServ";
                            }
                            ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, value);
                        }
                });
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        } else if(current_parameter == "changing_realname") {
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(current_parameter);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.enter_text_activity, null);
            builder.setView(view);
            builder.setTitle(R.string.enter_the_realname_title);
            TextView realname_label = (TextView) view.findViewById(R.id.profile_name_label);
            realname_label.setText(R.string.realname);
            EditText realname_text = (EditText) view.findViewById(R.id.profile_name_text);
            realname_text.setText(current_value);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText profile_name = (EditText) view.findViewById(R.id.profile_name_text);
                    ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, profile_name.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
        } else if(current_parameter == "changing_hostname") {
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(current_parameter);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.enter_text_activity, null);
            builder.setView(view);
            builder.setTitle(R.string.enter_the_hostname_title);
            TextView realname_label = (TextView) view.findViewById(R.id.profile_name_label);
            realname_label.setText(R.string.hostname);
            EditText realname_text = (EditText) view.findViewById(R.id.profile_name_text);
            realname_text.setText(current_value);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText profile_name = (EditText) view.findViewById(R.id.profile_name_text);
                    ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, profile_name.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
        } else if(current_parameter == "changing_quitmsg") {
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(current_parameter);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.enter_text_activity, null);
            builder.setView(view);
            builder.setTitle(R.string.enter_the_quiting_message);
            TextView quitmsgname_label = (TextView) view.findViewById(R.id.profile_name_label);
            quitmsgname_label.setText(R.string.quit_message);
            EditText quitmsgname_text = (EditText) view.findViewById(R.id.profile_name_text);
            quitmsgname_text.setText(current_value);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText profile_name = (EditText) view.findViewById(R.id.profile_name_text);
                    ((ProfileSettingsActivity) getActivity()).onChangingValues(current_parameter, profile_name.getText().toString());
                }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
        }
        return builder.create();
    }

    private void setLocale(SharedPreferences global_prefs) {
        if(global_prefs.getString("language", "OS dependent").contains("Russian")) {
            if(global_prefs.getBoolean("language_requires_restart", false) == false) {
                Locale locale = new Locale("ru");
                Locale.setDefault(locale);
                Configuration config = getResources().getConfiguration();
                config.locale = locale;
                getActivity().getResources().updateConfiguration(config,
                        getActivity().getResources().getDisplayMetrics());
            } else {
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getResources().updateConfiguration(config,
                        getActivity().getResources().getDisplayMetrics());
            }
        } else if (global_prefs.getString("language", "OS dependent").contains("English")) {
            if(global_prefs.getBoolean("language_requires_restart", false) == false) {
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getResources().updateConfiguration(config,
                        getActivity().getResources().getDisplayMetrics());
            } else {
                Locale locale = new Locale("ru");
                Locale.setDefault(locale);
                Configuration config = getResources().getConfiguration();
                config.locale = locale;
                getActivity().getResources().updateConfiguration(config,
                        getActivity().getResources().getDisplayMetrics());
            }
        }
    }
}
