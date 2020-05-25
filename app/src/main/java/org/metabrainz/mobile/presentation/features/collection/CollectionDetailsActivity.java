package org.metabrainz.mobile.presentation.features.collection;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.databinding.ActivityCollectionBinding;
=======
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
import org.metabrainz.mobile.presentation.MusicBrainzActivity;
<<<<<<< HEAD
<<<<<<< HEAD
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
=======
import org.metabrainz.mobile.presentation.features.adapters.ArtistAdapter;
import org.metabrainz.mobile.presentation.features.adapters.EventAdapter;
import org.metabrainz.mobile.presentation.features.adapters.InstrumentAdapter;
import org.metabrainz.mobile.presentation.features.adapters.LabelAdapter;
import org.metabrainz.mobile.presentation.features.adapters.RecordingAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseAdapter;
import org.metabrainz.mobile.presentation.features.adapters.ReleaseGroupAdapter;
import org.metabrainz.mobile.presentation.features.adapters.TypeAdapter;
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
=======
import org.metabrainz.mobile.presentation.features.adapters.ResultAdapter;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to display a list of collection results to the user and support intents
 * to info Activity types based on the selection.
 */
public class CollectionDetailsActivity extends MusicBrainzActivity {

<<<<<<< HEAD
    private ActivityCollectionBinding binding;

    private CollectionViewModel viewModel;
    private ResultAdapter adapter;
    private String id;
    private MBEntityType entity;
    private List<ResultItem> collectionResults;
=======
    private static CollectionViewModel viewModel;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private TextView noRes;
<<<<<<< HEAD
    private String entity, id;
    private final List<Artist> artistCollectionResults = new ArrayList<>();
    private final List<Release> releaseCollectionResults = new ArrayList<>();
    private final List<Label> labelCollectionResults = new ArrayList<>();
    private final List<Recording> recordingCollectionResults = new ArrayList<>();
    private final List<ReleaseGroup> releaseGroupCollectionResults = new ArrayList<>();
    private final List<Instrument> instrumentCollectionResults = new ArrayList<>();
    private final List<Event> eventCollectionResults = new ArrayList<>();
=======
    private String id;
    private MBEntityType entity;
    private List<ResultItem> collectionResults;
>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
    private ProgressBar progressBar;
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.noResult.setVisibility(View.GONE);
        binding.progressSpinner.setIndeterminate(true);
        binding.progressSpinner.setVisibility(View.GONE);

<<<<<<< HEAD
=======
        noRes = findViewById(R.id.no_result);
        noRes.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

>>>>>>> 70d3b15... Pass MBIDs through Intents using Constants.MBID key only. Delete unneeded IntentFactory.Extra and replace its usage with MBEntities (refactored to MBEntityType to avoid confusion).
        entity = (MBEntityType) getIntent().getSerializableExtra(Constants.TYPE);
        id = getIntent().getStringExtra(Constants.MBID);

        viewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
<<<<<<< HEAD
<<<<<<< HEAD
        collectionResults = new ArrayList<>();
        adapter = new ResultAdapter(collectionResults, entity);
        adapter.resetAnimation();

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setVisibility(View.GONE);

        binding.progressSpinner.setVisibility(View.VISIBLE);
        viewModel.fetchCollectionDetails(entity, id).observe(this,
                results -> {
                    collectionResults.clear();
                    collectionResults.addAll(results);
                    refresh();
                });
=======
        initializeCollectionData();
=======
        // initializeCollectionData();
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        getCollectionDetails(id);
    }

<<<<<<< HEAD
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
>>>>>>> 54c0fbe... Use common adapters for collections and search activity results.
    }
=======
//    private void initializeCollectionData() {
//        switch (entity) {
//            case IntentFactory.Extra.RELEASE:
//                viewModel.getReleaseCollectionData().observe(this,
//                        (List<Release> releaseCollectionProperties) -> {
//                            releaseCollectionResults.clear();
//                            releaseCollectionResults.addAll(releaseCollectionProperties);
//                            refresh();
//                        });
//                adapter = new ReleaseAdapter(releaseCollectionResults);
//                break;
//            case IntentFactory.Extra.LABEL:
//                viewModel.getLabelCollectionData().observe(this,
//                        (List<Label> labelCollectionProperties) -> {
//                            labelCollectionResults.clear();
//                            labelCollectionResults.addAll(labelCollectionProperties);
//                            refresh();
//                        });
//                adapter = new LabelAdapter(labelCollectionResults);
//                break;
//            case IntentFactory.Extra.RECORDING:
//                viewModel.getRecordingCollectionData().observe(this,
//                        (List<Recording> recordingCollectionProperties) -> {
//                            recordingCollectionResults.clear();
//                            recordingCollectionResults.addAll(recordingCollectionProperties);
//                            refresh();
//                        });
//                adapter = new RecordingAdapter(recordingCollectionResults);
//                break;
//            case IntentFactory.Extra.RELEASE_GROUP:
//                viewModel.getReleaseGroupCollectionData().observe(this,
//                        (List<ReleaseGroup> releaseGroupCollectionProperties) -> {
//                            releaseGroupCollectionResults.clear();
//                            releaseGroupCollectionResults.addAll(releaseGroupCollectionProperties);
//                            refresh();
//                        });
//                adapter = new ReleaseGroupAdapter(releaseGroupCollectionResults);
//                break;
//            case IntentFactory.Extra.EVENT:
//                viewModel.getEventCollectionData().observe(this,
//                        (List<Event> eventCollectionProperties) -> {
//                            eventCollectionResults.clear();
//                            eventCollectionResults.addAll(eventCollectionProperties);
//                            refresh();
//                        });
//                adapter = new EventAdapter(eventCollectionResults);
//                break;
//            case IntentFactory.Extra.INSTRUMENT:
//                viewModel.getInstrumentCollectionData().observe(this,
//                        (List<Instrument> instrumentCollectionProperties) -> {
//                            instrumentCollectionResults.clear();
//                            instrumentCollectionResults.addAll(instrumentCollectionProperties);
//                            refresh();
//                        });
//                adapter = new InstrumentAdapter(instrumentCollectionResults);
//                break;
//            default:
//                viewModel.getArtistCollectionData().observe(this,
//                        (List<Artist> artistCollectionProperties) -> {
//                            artistCollectionResults.clear();
//                            artistCollectionResults.addAll(artistCollectionProperties);
//                            refresh();
//                        });
//                adapter = new ArtistAdapter(artistCollectionResults);
//        }
//    }
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

    private void refresh() {
        adapter.notifyDataSetChanged();
        binding.progressSpinner.setVisibility(View.GONE);
        checkHasResults();
    }


    private void checkHasResults() {
        if (adapter.getItemCount() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noResult.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noResult.setVisibility(View.GONE);
        }
    }
}
