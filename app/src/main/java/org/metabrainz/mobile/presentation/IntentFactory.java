package org.metabrainz.mobile.presentation;

import android.content.Context;
import android.content.Intent;

import org.metabrainz.mobile.presentation.features.about.AboutActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DonateActivity;
import org.metabrainz.mobile.presentation.features.login.LoginActivity;
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity;

public class IntentFactory {

    public static Intent getDashboard(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getLogin(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getDonate(Context context) {
        return new Intent(context, DonateActivity.class);
    }

    public static Intent getAbout(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    public static Intent getSettings(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
