package org.metabrainz.mobile.activity;

import android.os.Bundle;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;

public class CollectionListActivity extends MusicBrainzActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        setUserNameTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUserNameTitle() {
        String userName = App.getUser().getUsername();
        getSupportActionBar().setTitle(userName + getString(R.string.plural) + " " + getString(R.string.collections));
    }

}
