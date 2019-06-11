package org.metabrainz.mobile.intent;

import android.content.Context;
import android.content.Intent;

import org.metabrainz.mobile.activity.DashboardActivity;
import org.metabrainz.mobile.presentation.DonateActivity;
import org.metabrainz.mobile.presentation.features.settings.SettingsActivity;
import org.metabrainz.mobile.presentation.AboutActivity;
import org.metabrainz.mobile.presentation.features.login.LoginActivity;

public class IntentFactory {

    public interface Extra {

        String ARTIST_MBID = "artist_mbid";
        String RELEASE_MBID = "release_mbid";
        String RG_MBID = "rg_mbid";
        String COLLECTION_MBID = "collection_mbid";
        String BARCODE = "barcode";

        String TYPE = "type";
        String QUERY = "query";
        String ARTIST = "artist";
        String RELEASE_GROUP = "rg";
        String RELEASE = "release";
        String LABEL = "label";
        String RECORDING = "recording";
        String INSTRUMENT = "instrument";
        String EVENT = "event";
        String ALL = "all";

    }

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
