package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.QueryUtils;
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.Track;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.ComparisionResult;
import org.metabrainz.mobile.util.Log;
import org.metabrainz.mobile.util.Metadata;
import org.metabrainz.mobile.util.MetadataChange;
import org.metabrainz.mobile.util.TaggerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.metabrainz.mobile.App.AUDIO_FILE_REQUEST_CODE;
import static org.metabrainz.mobile.App.STORAGE_PERMISSION_REQUEST_CODE;
import static org.metabrainz.mobile.presentation.features.tagger.FileSelectActivity.EXTRA_FILE_PATH;

public class TaggerActivity extends AppCompatActivity {

    private TaggerViewModel viewModel;
    private RecyclerView recyclerView;
    private TaggerAdapter adapter;
    private MetadataChangesAdapter metadataChangesAdapter;
    private List<MetadataChange> changes;
    private List<Recording> recordings;
    private AudioFile audioFile;
    private Recording localTrack;
    private TextView fileNameTextView;
    private Button buttonMetadata, buttonFingerprint, buttonSave;
    private ComparisionResult comparisionResult;
    private Track track;
    private Release release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagger);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        buttonMetadata = findViewById(R.id.lookup_metadata);
        buttonFingerprint = findViewById(R.id.lookup_fingerprint);
        buttonSave = findViewById(R.id.save_button);
        buttonMetadata.setEnabled(false);
        buttonMetadata.setOnClickListener(v -> lookupTrackWithMetadata());
        buttonFingerprint.setEnabled(false);
        buttonFingerprint.setOnClickListener(v -> lookupTrackWithFingerprint());
        buttonSave.setEnabled(false);
        buttonSave.setOnClickListener(v -> writeTagsToFile());

       fileNameTextView = findViewById(R.id.selected_file_name);

        recordings = new ArrayList<>();
        changes = new ArrayList<>();
        metadataChangesAdapter = new MetadataChangesAdapter(changes);
        viewModel = ViewModelProviders.of(this).get(TaggerViewModel.class);
        viewModel.getRecordingData().observe(this, this::processResults);
        viewModel.getMatchedReleaseData().observe(this, this::displayMatchedRelease);

        adapter = new TaggerAdapter(recordings);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(metadataChangesAdapter);

        findViewById(R.id.choose_button).setOnClickListener(v -> {
            String[] permissions = TaggerUtils.getPermissionsList(getApplicationContext());
            if (permissions.length > 0) ActivityCompat.requestPermissions(this,
                    permissions, STORAGE_PERMISSION_REQUEST_CODE);
            else {
                Intent intent = new Intent(this, FileSelectActivity.class);
                startActivityForResult(intent, AUDIO_FILE_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean storagePermission = requestCode == STORAGE_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED;
        if (storagePermission)
            chooseAudioFile();
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Storage Permission Denied");
            builder.setMessage("You need to grant MusicBrainz for Android access to your device's " +
                    "storage to tag a file.");
            builder.setNegativeButton("Close", (dialog, which) -> finish());
            builder.setOnCancelListener(dialog -> finish());
            builder.setOnDismissListener(dialog -> finish());
            builder.create().show();
        }
    }

    private void chooseAudioFile() {
        Intent chooserIntent = new Intent();
        chooserIntent.setAction(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("audio/*");
        startActivityForResult(chooserIntent, AUDIO_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUDIO_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("File selected correctly");
            try {
                String filePath = data.getExtras().getString(EXTRA_FILE_PATH);
                audioFile = AudioFileIO.read(new File(filePath));
                fileNameTextView.setText(audioFile.getFile().getName());
                enableLookup();
            } catch (Exception e) {
                Log.e(e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enableLookup() {
        buttonFingerprint.setEnabled(true);
        buttonMetadata.setEnabled(true);
    }

    private void lookupTrackWithMetadata() {
        if (audioFile != null) {
            List<Pair<String,String>> tags = Metadata.getDefaultTagList(audioFile);
            // Add quantized duration separately because there is no direct duration tag field
            tags.add(new Pair<>("qdur", String.valueOf(Metadata.getDuration(audioFile.getFile()) / 2000)));
            String queryArguments = QueryUtils.getQuery(tags);
            Toast.makeText(this, "JAudioTagger " + queryArguments,
                    Toast.LENGTH_LONG).show();
            viewModel.fetchRecordings(queryArguments);
        }
    }

    private void lookupTrackWithFingerprint() {
        if (audioFile != null) {
            String fingerprint = Metadata.getAudioFingerprint(audioFile.getFile());
            long duration = Metadata.getDuration(audioFile.getFile()) / 1000;
            viewModel.fetchRecordingsWithFingerprint(duration, fingerprint);
        }
    }

    private void processResults(List<Recording> data) {
        recordings.clear();
        recordings.addAll(data);
        adapter.notifyDataSetChanged();

        if (recordings.isEmpty()) {
            Toast.makeText(this, "No result found", Toast.LENGTH_LONG).show();
        } else {
            localTrack = Metadata.getRecordingFromFile(audioFile);

            // Find the result with highest similarity score
            double maxScore = 0.0;
            for (Recording searchResult : recordings) {
                ComparisionResult result = TaggerUtils.compareTracks(localTrack, searchResult);
                if (result.getScore() > maxScore) {
                    maxScore = result.getScore();
                    comparisionResult = result;
                }
            }

            if (comparisionResult != null && comparisionResult.getReleaseMbid() != null
                    && !comparisionResult.getReleaseMbid().isEmpty()
                    && comparisionResult.getScore() > TaggerUtils.THRESHOLD)
                viewModel.fetchMatchedRelease(comparisionResult.getReleaseMbid());
            else Toast.makeText(this, "No result found", Toast.LENGTH_LONG).show();
        }
    }

    private void displayMatchedRelease(Release release) {
        track = null;
        this.release = release;
        // Find the matching track with highest similarity score
        if (release != null && release.getMedia() != null && !release.getMedia().isEmpty())
            for (Media media : release.getMedia())
                for (Track search : media.getTracks())
                    if (search.getRecording().getMbid().equalsIgnoreCase(comparisionResult.getTrackMbid()))
                        track = search;
        // Display changes in metadata
        if (track != null && track.getRecording() != null) {
            showDifferenceInMetadata();
            buttonSave.setEnabled(true);
        } else
            Toast.makeText(this, "No result found", Toast.LENGTH_LONG).show();
    }

    private void showDifferenceInMetadata() {
        if (track != null && release != null) {

            changes.clear();

            Tag tag = audioFile.getTag();

            changes.add(createChange(tag, FieldKey.MUSICBRAINZ_TRACK_ID, track.getRecording().getMbid()));
            changes.add(createChange(tag, FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, track.getRecording().getMbid()));

            String artist = EntityUtils.getDisplayArtist(track.getRecording().getArtistCredits());
            changes.add(createChange(tag, FieldKey.ARTIST, artist));
            changes.add(createChange(tag, FieldKey.ARTIST_SORT, artist));
            changes.add(createChange(tag, FieldKey.ALBUM_ARTIST, artist));
            changes.add(createChange(tag, FieldKey.ALBUM_ARTIST_SORT, artist));
            changes.add(createChange(tag, FieldKey.ALBUM, release.getTitle()));
            changes.add(createChange(tag, FieldKey.ORIGINAL_YEAR, release.getDate()));
            changes.add(createChange(tag, FieldKey.TRACK, String.valueOf(track.getPosition())));
            changes.add(createChange(tag, FieldKey.TRACK_TOTAL, String.valueOf(release.getTrackCount())));
            changes.add(createChange(tag, FieldKey.BARCODE, release.getBarcode()));
            changes.add(createChange(tag, FieldKey.COUNTRY, release.getCountry()));
            changes.add(createChange(tag, FieldKey.MUSICBRAINZ_RELEASE_STATUS, release.getStatus()));
            changes.add(createChange(tag, FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, release.getReleaseGroup().getMbid()));
            changes.add(createChange(tag, FieldKey.MUSICBRAINZ_RELEASEID, release.getMbid()));

            metadataChangesAdapter.notifyDataSetChanged();
        }
    }

    private void writeTagsToFile() {
        if (track != null && release != null) {
            try {
                Tag tag = audioFile.getTag();
                for (MetadataChange change : changes) {
                    if (!change.isDiscardChange())
                        tag.setField(change.getTagName(), change.getNewValue());
                }

                audioFile.setTag(tag);
                audioFile.commit();
                resetTagger();
                metadataChangesAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e(e.getMessage());
            }
        }
    }

    private MetadataChange createChange(Tag tag, FieldKey fieldKey, String newValue) {
        return new MetadataChange(fieldKey, tag.getFirst(fieldKey), newValue);
    }

    private void resetTagger() {
        buttonSave.setEnabled(false);
        buttonFingerprint.setEnabled(false);
        buttonMetadata.setEnabled(false);
        fileNameTextView.setText(getString(R.string.no_file_selected));

    }

}
