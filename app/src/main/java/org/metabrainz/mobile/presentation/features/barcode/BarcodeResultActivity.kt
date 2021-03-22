package org.metabrainz.mobile.presentation.features.barcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.ActivityBarcodeResultBinding;
import org.metabrainz.mobile.presentation.features.base.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListAdapter;
import org.metabrainz.mobile.util.Resource;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static org.metabrainz.mobile.util.Resource.Status.SUCCESS;

@AndroidEntryPoint
public class BarcodeResultActivity extends MusicBrainzActivity {

    private ActivityBarcodeResultBinding binding;
    private final List<Release> releases = new ArrayList<>();
    private BarcodeViewModel viewModel;
    private ReleaseListAdapter adapter;
    private String barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarcodeResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar(binding);

        adapter = new ReleaseListAdapter(this, releases);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.GONE);

        binding.noResult.getRoot().setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(BarcodeViewModel.class);
        barcode = getIntent().getStringExtra("barcode");

        if (barcode != null && !barcode.isEmpty()) {
            viewModel.fetchReleasesWithBarcode(barcode).observe(this, this::handleResult);
            binding.progressSpinner.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.progressSpinner.getRoot().setVisibility(View.GONE);
            Toast.makeText(this, "Unknown barcode error", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void handleResult(Resource<List<Release>> resource) {
        releases.clear();
        binding.progressSpinner.getRoot().setVisibility(View.GONE);

        if (resource.getStatus() == SUCCESS) {
            releases.addAll(resource.getData());
            if (releases.size() == 0)
                binding.noResult.getRoot().setVisibility(View.VISIBLE);
            else if (releases.size() == 1) {
                Intent intent = new Intent(this, ReleaseActivity.class);
                intent.putExtra(Constants.MBID, releases.get(0).getMbid());
                startActivity(intent);
                finish();
            } else
                showMultipleReleases();
        }
    }

    private void showMultipleReleases() {
        adapter.notifyDataSetChanged();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected Uri getBrowserURI() {
        return Uri.parse(App.WEBSITE_BASE_URL + "search?type=release&advanced=1&query=barcode:" + barcode);
    }
}
