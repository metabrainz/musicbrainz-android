package org.metabrainz.mobile.presentation.features.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.ActivityBarcodeResultBinding;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BarcodeResultActivity extends MusicBrainzActivity {

    private ActivityBarcodeResultBinding binding;
    private final List<Release> releases = new ArrayList<>();
    private BarcodeViewModel viewModel;
<<<<<<< HEAD
    // private ReleaseAdapter adapter;
=======
    private TextView noResultView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
<<<<<<< HEAD
    private ReleaseAdapter adapter;
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
=======
    // private ReleaseAdapter adapter;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarcodeResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

<<<<<<< HEAD
<<<<<<< HEAD
        // adapter = new ReleaseAdapter(releases);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
=======
        adapter = new ReleaseAdapter(releases);
=======
        // adapter = new ReleaseAdapter(releases);
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
<<<<<<< HEAD
        binding.recyclerView.addItemDecoration(itemDecoration);
        // recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.GONE);
=======
        recyclerView.addItemDecoration(itemDecoration);
        // recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

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
<<<<<<< HEAD
            binding.noResult.setVisibility(View.VISIBLE);
=======
            noResultView.setVisibility(View.VISIBLE);
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
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
<<<<<<< HEAD
        binding.recyclerView.setVisibility(View.VISIBLE);
=======
        recyclerView.setVisibility(View.VISIBLE);
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
    }
}
