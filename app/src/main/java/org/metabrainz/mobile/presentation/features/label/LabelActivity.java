package org.metabrainz.mobile.presentation.features.label;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
<<<<<<< HEAD
import org.metabrainz.mobile.databinding.ActivityLabelBinding;
=======
import org.metabrainz.mobile.presentation.IntentFactory;
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
<<<<<<< HEAD
import org.metabrainz.mobile.presentation.features.userdata.UserViewModel;
=======
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.

import java.util.Objects;

public class LabelActivity extends MusicBrainzActivity {

    public static final String LOG_TAG = "DebugLabelInfo";

    private ActivityLabelBinding binding;

    private LabelViewModel labelViewModel;
    private UserViewModel userViewModel;

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

        mbid = getIntent().getStringExtra(Constants.MBID);
        if (mbid != null && !mbid.isEmpty()) labelViewModel.setMBID(mbid);

<<<<<<< HEAD
<<<<<<< HEAD
        labelViewModel.getData().observe(this, this::setLabel);
=======
        labelViewModel.initializeData().observe(this, this::setLabel);
        labelViewModel.fetchData();
>>>>>>> de8f646... Refactor lookup repositories to remove redundancy.
=======
        labelViewModel.getData().observe(this, this::setLabel);
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
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
<<<<<<< HEAD
        if (label != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(label.getName());
            userViewModel.setUserData(label);
        }
=======
        if (label != null)
            Objects.requireNonNull(getSupportActionBar()).setTitle(label.getName());
>>>>>>> b793e03... Improve usage of live data and reactive patterns.
    }
}
