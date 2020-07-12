package org.metabrainz.mobile.presentation.features.label;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.databinding.ActivityLabelBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;

import java.util.Objects;

public class LabelActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugLabelInfo";

    private ActivityLabelBinding binding;

    private LabelViewModel labelViewModel;
    private UserViewModel userViewModel;
    private LinksViewModel linksViewModel;
    private ReleaseListViewModel releaseListViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLabelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        labelViewModel = new ViewModelProvider(this).get(LabelViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
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
        if (label != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(label.getName());
            userViewModel.setUserData(label);
            linksViewModel.setData(label.getRelations());
            releaseListViewModel.setData(label.getReleases());
        }
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "label/" + mbid);
    }
}
