package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;

import java.util.Objects;

public class LabelActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugLabelInfo";

    private LabelViewModel labelViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        labelViewModel = new ViewModelProvider(this).get(LabelViewModel.class);

        mbid = getIntent().getStringExtra(IntentFactory.Extra.LABEL);
        if (mbid != null && !mbid.isEmpty()) labelViewModel.setMBID(mbid);

        labelViewModel.getData().observe(this, this::setLabel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLabel(Label label) {
        if (label != null)
            Objects.requireNonNull(getSupportActionBar()).setTitle(label.getName());
    }
}
