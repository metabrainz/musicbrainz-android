package org.metabrainz.mobile.presentation.features.label;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.databinding.ActivityLabelBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.links.LinksViewModel;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListViewModel;
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
import org.metabrainz.mobile.util.Resource;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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

        setupToolbar(binding);

        labelViewModel = new ViewModelProvider(this).get(LabelViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        linksViewModel = new ViewModelProvider(this).get(LinksViewModel.class);
        releaseListViewModel = new ViewModelProvider(this).get(ReleaseListViewModel.class);

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) labelViewModel.setMBID(mbid);

        binding.noResult.getRoot().setVisibility(View.GONE);
        binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);
        binding.dataFragments.setVisibility(View.GONE);
        labelViewModel.getData().observe(this, this::setLabel);
    }

    private void setLabel(Resource<Label> resource) {
        binding.progressSpinner.getRoot().setVisibility(View.GONE);
        if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
            binding.noResult.getRoot().setVisibility(View.GONE);
            binding.dataFragments.setVisibility(View.VISIBLE);

            Label label = resource.getData();
            Objects.requireNonNull(getSupportActionBar()).setTitle(label.getName());
            userViewModel.setUserData(label);
            linksViewModel.setData(label.getRelations());
            releaseListViewModel.setData(label.getReleases());
        } else
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "label/" + mbid);
    }
}
