package org.metabrainz.mobile.presentation.features.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.ActivityBarcodeResultBinding;

import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.http.HEAD;

public class BarcodeResultActivity extends MusicBrainzActivity {

    private ActivityBarcodeResultBinding binding;
    private final List<Release> releases = new ArrayList<>();
    private BarcodeViewModel viewModel;
    // private ReleaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarcodeResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // adapter = new ReleaseAdapter(releases);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);
        // recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.GONE);

        binding.noResult.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(BarcodeViewModel.class);
        viewModel.getBarcodeLiveData().observe(this, this::handleResult);

        String barcode = getIntent().getStringExtra("barcode");
        if (barcode != null && !barcode.isEmpty()) {
            viewModel.fetchReleasesWithBarcode(barcode);
            binding.progressSpinner.setVisibility(View.VISIBLE);
        } else {
            binding.progressSpinner.setVisibility(View.GONE);
            Toast.makeText(this, "Unknown barcode error", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void handleResult(List<Release> data) {
        releases.clear();
        releases.addAll(data);

        binding.progressSpinner.setVisibility(View.GONE);

        if (releases.size() == 0)
            binding.noResult.setVisibility(View.VISIBLE);
        else if (releases.size() == 1) {
            Intent intent = new Intent(this, ReleaseActivity.class);
            intent.putExtra(Constants.MBID, releases.get(0).getMbid());
            startActivity(intent);
            finish();
        } else
            showMultipleReleases();
    }

    private void showMultipleReleases() {
        // adapter.notifyDataSetChanged();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}
