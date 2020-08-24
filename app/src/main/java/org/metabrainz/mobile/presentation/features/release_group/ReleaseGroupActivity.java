package org.metabrainz.mobile.presentation.features.release_group;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.databinding.ActivityReleaseGroupBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
import org.metabrainz.mobile.util.Resource;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReleaseGroupActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugReleaseGroupInfo";

    private ActivityReleaseGroupBinding binding;

    private ReleaseGroupViewModel releaseGroupViewModel;
    private UserViewModel userViewModel;
    private LinksViewModel linksViewModel;
    private ReleaseListViewModel releaseListViewModel;

    private String mbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        releaseGroupViewModel = new ViewModelProvider(this).get(ReleaseGroupViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) releaseGroupViewModel.setMBID(mbid);

        binding.noResult.getRoot().setVisibility(View.GONE);
        binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);
        binding.dataFragments.setVisibility(View.GONE);
        releaseGroupViewModel.getData().observe(this, this::setReleaseGroup);
    }

    private void setReleaseGroup(Resource<ReleaseGroup> resource) {
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.dataFragments.setVisibility(View.VISIBLE);

            ReleaseGroup releaseGroup = resource.getData();
            Objects.requireNonNull(getSupportActionBar()).setTitle(releaseGroup.getTitle());
            userViewModel.setUserData(releaseGroup);
            linksViewModel.setData(releaseGroup.getRelations());
            releaseListViewModel.setData(releaseGroup.getReleases());
        } else
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "release-group/" + mbid);
    }
}
