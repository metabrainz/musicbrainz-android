/*
 * Copyright (C) 2012 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.intent;

import org.musicbrainz.mobile.activity.AboutActivity;
import org.musicbrainz.mobile.activity.CollectionActivity;
import org.musicbrainz.mobile.activity.CollectionListActivity;
import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.DonateActivity;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.activity.SettingsActivity;
import org.musicbrainz.mobile.activity.WebActivity;

import android.content.Context;
import android.content.Intent;

public class IntentFactory {

    public interface Extra {

        public static final String ARTIST_MBID = "artist_mbid";
        public static final String RELEASE_MBID = "release_mbid";
        public static final String RG_MBID = "rg_mbid";
        public static final String COLLECTION_MBID = "collection_mbid";
        public static final String BARCODE = "barcode";

        public static final String TYPE = "type";
        public static final String QUERY = "query";
        public static final String ARTIST = "artist";
        public static final String RELEASE_GROUP = "rg";
        public static final String LABEL = "label";
        public static final String RECORDING = "recording";
        public static final String ALL = "all";

        public static final String MESSAGE = "message";

        public static final String TITLE = "title";
        public static final String TARGET_URL = "target_url";
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
