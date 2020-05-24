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
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BarcodeResultActivity extends MusicBrainzActivity {

    private final List<Release> releases = new ArrayList<>();
    private BarcodeViewModel viewModel;
    private TextView noResultView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    // private ReleaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // adapter = new ReleaseAdapter(releases);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        // recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_spinner);
        noResultView = findViewById(R.id.no_result);
        noResultView.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(BarcodeViewModel.class);
        viewModel.getBarcodeLiveData().observe(this, this::handleResult);

        String barcode = getIntent().getStringExtra("barcode");
        if (barcode != null && !barcode.isEmpty()) {
            viewModel.fetchReleasesWithBarcode(barcode);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Unknown barcode error", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void handleResult(List<Release> data) {
        releases.clear();
        releases.addAll(data);

        progressBar.setVisibility(View.GONE);

        if (releases.size() == 0)
            noResultView.setVisibility(View.VISIBLE);
        else if (releases.size() == 1) {
            Intent intent = new Intent(this, ReleaseActivity.class);
            intent.putExtra(IntentFactory.Extra.RELEASE_MBID, releases.get(0).getMbid());
            startActivity(intent);
            finish();
        } else
            showMultipleReleases();
    }

    private void showMultipleReleases() {
        // adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }
}
