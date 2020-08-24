package org.metabrainz.mobile.presentation.features.release;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.ActivityReleaseBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
import org.metabrainz.mobile.util.Resource;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity that retrieves and displays information about an artist given an
 * artist MBID.
 */
@AndroidEntryPoint
public class ReleaseActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugReleaseInfo";

    private ActivityReleaseBinding binding;

    private ReleaseViewModel releaseViewModel;
    private LinksViewModel linksViewModel;
    private UserViewModel userViewModel;

    private String mbid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        releaseViewModel = new ViewModelProvider(this).get(ReleaseViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) releaseViewModel.setMBID(mbid);

        binding.noResult.getRoot().setVisibility(View.GONE);
        binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);
        binding.dataFragments.setVisibility(View.GONE);
        releaseViewModel.getData().observe(this, this::setRelease);
    }

    private void setRelease(Resource<Release> resource) {
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.dataFragments.setVisibility(View.VISIBLE);

            Release release = resource.getData();
            Objects.requireNonNull(getSupportActionBar()).setTitle(release.getTitle());
            userViewModel.setUserData(release);
            linksViewModel.setData(release.getRelations());
        } else
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "release/" + mbid);
    }
}
