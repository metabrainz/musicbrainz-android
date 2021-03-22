package org.metabrainz.mobile.presentation.features.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.databinding.ActivityPreferencesBinding;
import org.metabrainz.mobile.databinding.LayoutToolbarBinding;
import org.metabrainz.mobile.presentation.UserPreferences;

import java.util.Objects;

import static org.metabrainz.mobile.presentation.UserPreferences.PREFERENCE_LISTENING_ENABLED;

public class SettingsActivity extends AppCompatActivity {

    Preference.OnPreferenceChangeListener preferenceChangeListener;
    private static String ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPreferencesBinding binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LayoutToolbarBinding toolbarBinding = LayoutToolbarBinding.bind(binding.getRoot());
        setSupportActionBar(toolbarBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            ACTION_NOTIFICATION_LISTENER_SETTINGS = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

        preferenceChangeListener = (preference, newValue) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (preference.getKey().equals(PREFERENCE_LISTENING_ENABLED)) {
                    Boolean enabled = (Boolean) newValue;
                    if (enabled && !App.getInstance().isNotificationServiceAllowed()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Grant Media Control Permissions");
                        builder.setMessage("The listen service requires the special Notification " +
                                "Listener Service Permission to run. Please grant this permission to" +
                                " MusicBrainz for Android if you want to use the service.");
                        builder.setPositiveButton("Proceed", (dialog, which) ->
                                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)));
                        builder.setNegativeButton("Cancel", ((dialog, which) -> {
                            UserPreferences.setPreferenceListeningEnabled(false);
                            ((SwitchPreference) preference).setChecked(false);
                        }));
                        builder.create().show();
                    } else if (!enabled)
                        App.getInstance().stopListenService();
                    return true;
                }
            }
            return false;
        };
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SettingsFragment) {
            SettingsFragment settingsFragment = (SettingsFragment) fragment;
            settingsFragment.setPreferenceChangeListener(preferenceChangeListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
