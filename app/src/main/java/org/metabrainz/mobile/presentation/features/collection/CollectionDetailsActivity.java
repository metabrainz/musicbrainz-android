package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
import org.metabrainz.mobile.presentation.features.adapters.ArtistAdapter;
import org.metabrainz.mobile.presentation.features.adapters.EventAdapter;
import org.metabrainz.mobile.presentation.features.adapters.InstrumentAdapter;
import org.metabrainz.mobile.presentation.features.adapters.LabelAdapter;
import org.metabrainz.mobile.presentation.features.adapters.RecordingAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseGroupAdapter;
import org.metabrainz.mobile.presentation.features.adapters.TypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to display a list of collection results to the user and support intents
 * to info Activity types based on the selection.
 */
public class CollectionDetailsActivity extends MusicBrainzActivity {

    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private TypeAdapter adapter;
    private TextView noRes;
    private String entity, id;
    private final List<Artist> artistCollectionResults = new ArrayList<>();
    private final List<Release> releaseCollectionResults = new ArrayList<>();
    private final List<Label> labelCollectionResults = new ArrayList<>();
    private final List<Recording> recordingCollectionResults = new ArrayList<>();
    private final List<ReleaseGroup> releaseGroupCollectionResults = new ArrayList<>();
    private final List<Instrument> instrumentCollectionResults = new ArrayList<>();
    private final List<Event> eventCollectionResults = new ArrayList<>();
    private ProgressBar progressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        entity = getIntent().getStringExtra(IntentFactory.Extra.TYPE);
        id = getIntent().getStringExtra(IntentFactory.Extra.COLLECTION_MBID);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        initializeCollectionData();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        getCollectionDetails(id);
    }

    private void initializeCollectionData() {
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                viewModel.getReleaseCollectionData().observe(this,
                        (List<Release> releaseCollectionProperties) -> {
                            releaseCollectionResults.clear();
                            releaseCollectionResults.addAll(releaseCollectionProperties);
                            refresh();
                        });
                adapter = new ReleaseAdapter(releaseCollectionResults);
                break;
            case IntentFactory.Extra.LABEL:
                viewModel.getLabelCollectionData().observe(this,
                        (List<Label> labelCollectionProperties) -> {
                            labelCollectionResults.clear();
                            labelCollectionResults.addAll(labelCollectionProperties);
                            refresh();
                        });
                adapter = new LabelAdapter(labelCollectionResults);
                break;
            case IntentFactory.Extra.RECORDING:
                viewModel.getRecordingCollectionData().observe(this,
                        (List<Recording> recordingCollectionProperties) -> {
                            recordingCollectionResults.clear();
                            recordingCollectionResults.addAll(recordingCollectionProperties);
                            refresh();
                        });
                adapter = new RecordingAdapter(recordingCollectionResults);
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                viewModel.getReleaseGroupCollectionData().observe(this,
                        (List<ReleaseGroup> releaseGroupCollectionProperties) -> {
                            releaseGroupCollectionResults.clear();
                            releaseGroupCollectionResults.addAll(releaseGroupCollectionProperties);
                            refresh();
                        });
                adapter = new ReleaseGroupAdapter(releaseGroupCollectionResults);
                break;
            case IntentFactory.Extra.EVENT:
                viewModel.getEventCollectionData().observe(this,
                        (List<Event> eventCollectionProperties) -> {
                            eventCollectionResults.clear();
                            eventCollectionResults.addAll(eventCollectionProperties);
                            refresh();
                        });
                adapter = new EventAdapter(eventCollectionResults);
                break;
            case IntentFactory.Extra.INSTRUMENT:
                viewModel.getInstrumentCollectionData().observe(this,
                        (List<Instrument> instrumentCollectionProperties) -> {
                            instrumentCollectionResults.clear();
                            instrumentCollectionResults.addAll(instrumentCollectionProperties);
                            refresh();
                        });
                adapter = new InstrumentAdapter(instrumentCollectionResults);
                break;
            default:
                viewModel.getArtistCollectionData().observe(this,
                        (List<Artist> artistCollectionProperties) -> {
                            artistCollectionResults.clear();
                            artistCollectionResults.addAll(artistCollectionProperties);
                            refresh();
                        });
                adapter = new ArtistAdapter(artistCollectionResults);
        }
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        checkHasResults();
    }

    private void getCollectionDetails(String id) {
        progressBar.setVisibility(View.VISIBLE);
        adapter.resetAnimation();
        switch (entity) {
            case IntentFactory.Extra.RELEASE:
                viewModel.fetchReleaseCollectionDetails(id);
                break;
            case IntentFactory.Extra.LABEL:
                viewModel.fetchLabelCollectionDetails(id);
                break;
            case IntentFactory.Extra.RECORDING:
                viewModel.fetchRecordingCollectionDetails(id);
                break;
            case IntentFactory.Extra.EVENT:
                viewModel.fetchEventCollectionDetails(id);
                break;
            case IntentFactory.Extra.INSTRUMENT:
                viewModel.fetchInstrumentCollectionDetails(id);
                break;
            case IntentFactory.Extra.RELEASE_GROUP:
                viewModel.fetchReleaseGroupCollectionDetails(id);
                break;
            default:
                viewModel.fetchArtistCollectionDetails(id);
        }
    }

    private void checkHasResults() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            noRes.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noRes.setVisibility(View.GONE);
        }
    }
}
