package org.metabrainz.mobile.presentation.features.tagger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.Track;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.util.ComparisionResult;
import org.metabrainz.mobile.util.Log;
import org.metabrainz.mobile.util.Metadata;
import org.metabrainz.mobile.util.MetadataChange;
import org.metabrainz.mobile.util.PathUtils;
import org.metabrainz.mobile.util.TaggerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaggerActivity extends AppCompatActivity {

    public static final int AUDIO_FILE_REQUEST_CODE = 0;
    private final int STORAGE_PERMISSION = 1;
    private boolean readStoragePermission, writeStoragePermission, storagePermission;
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

            readStoragePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            writeStoragePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            ArrayList<String> permissionsList = new ArrayList<>();
            if (!readStoragePermission)
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (!writeStoragePermission)
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            if (!permissionsList.isEmpty()) ActivityCompat.requestPermissions(this,
                    permissions, STORAGE_PERMISSION);
            else chooseAudioFile();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        storagePermission = requestCode == STORAGE_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
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
                Uri uri = data.getData();
                String path = PathUtils.getRealPathFromURI(getApplicationContext(), uri);
                audioFile = AudioFileIO.read(new File(path));
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
            String queryArguments = QueryUtils.getQuery(Metadata.getDefaultTagList(audioFile));
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

            double maxScore = 0.0;
            for (Recording searchResult : recordings) {
                ComparisionResult result = TaggerUtils.compareTracks(localTrack, searchResult);
                if (result.getScore() > maxScore) {
                    maxScore = result.getScore();
                    comparisionResult = result;
                }
            }

            if (comparisionResult != null && comparisionResult.getReleaseMbid() != null
                    && !comparisionResult.getReleaseMbid().isEmpty())
                viewModel.fetchMatchedRelease(comparisionResult.getReleaseMbid());
        }
    }

    private void displayMatchedRelease(Release release) {
        track = null;
        this.release = release;
        if (release != null && release.getMedia() != null && !release.getMedia().isEmpty())
            for (Media media : release.getMedia())
                for (Track search : media.getTracks())
                    if (search.getRecording().getMbid().equalsIgnoreCase(comparisionResult.getTrackMbid()))
                        track = search;

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

            String artist = track.getRecording().getDisplayArtist();
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
                tag.setField(FieldKey.MUSICBRAINZ_TRACK_ID, track.getRecording().getMbid());
                tag.setField(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, track.getMbid());

                String artist = track.getRecording().getDisplayArtist();
                tag.setField(FieldKey.ARTIST, artist);
                tag.setField(FieldKey.ARTIST_SORT, artist);
                tag.setField(FieldKey.ALBUM_ARTIST, artist);
                tag.setField(FieldKey.ALBUM_ARTIST_SORT, artist);

                tag.setField(FieldKey.ALBUM, release.getTitle());
                tag.setField(FieldKey.ORIGINAL_YEAR, release.getDate());
                tag.setField(FieldKey.TRACK, String.valueOf(track.getPosition()));
                tag.setField(FieldKey.TRACK_TOTAL, String.valueOf(release.getTrackCount()));
                tag.setField(FieldKey.BARCODE, release.getBarcode());
                tag.setField(FieldKey.COUNTRY, release.getCountry());
                tag.setField(FieldKey.MUSICBRAINZ_RELEASE_STATUS, release.getStatus());
                tag.setField(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, release.getReleaseGroup().getMbid());
                tag.setField(FieldKey.MUSICBRAINZ_RELEASEID, release.getMbid());

                audioFile.setTag(tag);
                audioFile.commit();
                metadataChangesAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e(e.getMessage());
            }
        }
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }

    private MetadataChange createChange(Tag tag, FieldKey fieldKey, String newValue) {
        return new MetadataChange(fieldKey, tag.getFirst(fieldKey), newValue);
    }

}
