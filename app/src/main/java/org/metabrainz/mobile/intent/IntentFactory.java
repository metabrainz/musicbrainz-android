package org.metabrainz.mobile.intent;

import android.content.Context;
import android.content.Intent;

import org.metabrainz.mobile.presentation.AboutActivity;
import org.metabrainz.mobile.activity.CollectionActivity;
import org.metabrainz.mobile.activity.CollectionListActivity;
import org.metabrainz.mobile.activity.DashboardActivity;
import org.metabrainz.mobile.activity.DonateActivity;
import org.metabrainz.mobile.presentation.LoginActivity;
import org.metabrainz.mobile.activity.SettingsActivity;
import org.metabrainz.mobile.activity.WebActivity;

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

        String TITLE = "title";
        String TARGET_URL = "target_url";
    }

    public static Intent getDashboard(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getWebView(Context context, int titleId, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Extra.TITLE, titleId);
        intent.putExtra(Extra.TARGET_URL, url);
        return intent;
    }

    public static Intent getCollectionList(Context context) {
        Intent intent = new Intent(context, CollectionListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getCollection(Context context, String title, String mbid) {
        Intent intent = new Intent(context, CollectionActivity.class);
        intent.putExtra(Extra.TITLE, title);
        intent.putExtra(Extra.COLLECTION_MBID, mbid);
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
