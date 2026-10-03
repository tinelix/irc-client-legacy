package dev.tinelix.irc.android.legacy.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.tinelix.irc.android.legacy.BuildConfig;
import dev.tinelix.irc.android.legacy.Global;
import dev.tinelix.irc.android.legacy.R;
import dev.tinelix.irc.android.legacy.core.activities.base.BaseActivity;

public class AboutApplicationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
        Button repoButton = findViewById(R.id.repo_button);
        Button websiteButton = findViewById(R.id.website_button);

        repoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(BuildConfig.SOURCE_CODE);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://web1.tinelix.ru/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        
        TextView license_label = findViewById(R.id.license_label);
        license_label.setMovementMethod(LinkMovementMethod.getInstance());

        TextView version_label = findViewById(R.id.version_label);
        version_label.setText(
                getResources().getString(
                        R.string.version_str,
                        BuildConfig.VERSION_NAME,
                        Global.formatTimestamp(this, BuildConfig.BUILD_DATE)
                )
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
