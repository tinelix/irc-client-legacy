package dev.tinelix.irc.android.legacy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

@SuppressLint("NewApi")
public class ServerSettingsDialogFragm extends DialogFragment {

    public String current_parameter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences global_prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setLocale(global_prefs);
        current_parameter = ((ProfileSettingsActivity) getActivity()).getCurrentParameter();
        String current_value;
        String server_parameter;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(current_parameter == "setting_server") {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.enter_server_activity, null);
            final Spinner encoding_spinner = (Spinner) view.findViewById(R.id.encoding_spinner);
            Context context = view.getContext();
            ArrayAdapter<?> adapter =
                    ArrayAdapter.createFromResource(context, R.array.encoding_array,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            encoding_spinner.setAdapter(adapter);
            builder.setView(view);
            builder.setTitle(R.string.server_settings);
            builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText server_name = (EditText) view.findViewById(R.id.server_text);
                        EditText port_number = (EditText) view.findViewById(R.id.port_numb);
                        CheckBox hide_ip_cb = (CheckBox) view.findViewById(R.id.hide_ip_checkbox);
                        CheckBox force_ssl_cb = (CheckBox) view.findViewById(R.id.force_ssl_checkbox);
                        String[] encoding_array = getResources().getStringArray(R.array.encoding_array);
                        String encoding = new String();
                        String force_ssl = new String();
                        if(encoding_spinner.getSelectedItemPosition() == 0) {
                            encoding = "utf-8";
                        } else if(encoding_spinner.getSelectedItemPosition() == 1) {
                            encoding = "cp866";
                        } else if(encoding_spinner.getSelectedItemPosition() == 2) {
                            encoding = "windows-1251";
                        } else if(encoding_spinner.getSelectedItemPosition() == 3) {
                            encoding = "koi8_r";
                        } else if(encoding_spinner.getSelectedItemPosition() == 4) {
                            encoding = "koi8_u";
                        };
                        String hide_ip = new String();
                        if(hide_ip_cb.isChecked() == true) {
                            hide_ip = "Enabled";
                        } else {
                            hide_ip = "Disabled";
                        }
                        if(force_ssl_cb.isChecked() == true) {
                            force_ssl = "Enabled";
                        } else {
                            force_ssl = "Disabled";
                        }
                       ((ProfileSettingsActivity) getActivity()).onSettingServer(server_name.getText().toString(),
                               port_number.getText().toString(), encoding, hide_ip, force_ssl);
                    }
            });
            builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
            });
            String[] encoding_array = getResources().getStringArray(R.array.encoding_array);
            server_parameter = "changing_server";
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(server_parameter);
            final EditText server_text = (EditText) view.findViewById(R.id.server_text);
            server_text.setText(current_value);
            server_text.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().contains(":")) {
                        server_text.setError(getResources().getString(R.string.text_field_wrong_characters));
                        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            });
            server_parameter = "changing_port";
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(server_parameter);
            EditText port_numb = (EditText) view.findViewById(R.id.port_numb);
            port_numb.setText(current_value);
            ArrayAdapter<?> encoding_adapter =
                    ArrayAdapter.createFromResource(view.getContext(), R.array.encoding_array,
                            android.R.layout.simple_spinner_item);
            encoding_spinner.setAdapter(encoding_adapter);
            server_parameter = "changing_encoding";
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(server_parameter);
            if(current_value.contains("utf-8")) {
                encoding_spinner.setSelection(0);
            } else if(current_value.contains("cp866")) {
                encoding_spinner.setSelection(1);
            } else if(current_value.contains("windows-1251")) {
                encoding_spinner.setSelection(2);
            } else if(current_value.contains("koi8_r")) {
                encoding_spinner.setSelection(3);
            } else if(current_value.contains("koi8_u")) {
                encoding_spinner.setSelection(4);
            }
            server_parameter = "hide_ip";
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(server_parameter);
            CheckBox hide_ip_cb = (CheckBox) view.findViewById(R.id.hide_ip_checkbox);
            if(current_value.contains("Disabled")) {
                hide_ip_cb.setChecked(false);
            } else {
                hide_ip_cb.setChecked(true);
            }

            server_parameter = "force_ssl";
            current_value = ((ProfileSettingsActivity) getActivity()).getCurrentValue(server_parameter);
            CheckBox force_ssl_cb = (CheckBox) view.findViewById(R.id.force_ssl_checkbox);
            if(current_value.contains("Disabled")) {
                force_ssl_cb.setChecked(false);
            } else {
                force_ssl_cb.setChecked(true);
            }
        };
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
