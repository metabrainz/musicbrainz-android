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
import org.metabrainz.mobile.util.PathUtils;
import org.metabrainz.mobile.util.TaggerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaggerActivity extends AppCompatActivity {

    public static final int AUDIO_FILE_REQUEST_CODE = 0;
    private final int READ_STORAGE_PERMISSION = 1;
    private final int WRITE_STORAGE_PERMISSION = 2;
    private boolean readStoragePermission;
    private TaggerViewModel viewModel;
    private RecyclerView recyclerView;
    private TaggerAdapter adapter;
    private List<Recording> recordings;
    private AudioFile audioFile;
    private Recording localTrack;
    private Button buttonMetadata, buttonFingerprint;
    private ComparisionResult comparisionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagger);

        buttonMetadata = findViewById(R.id.lookup_metadata);
        buttonFingerprint = findViewById(R.id.lookup_fingerprint);
        buttonMetadata.setEnabled(false);
        buttonMetadata.setOnClickListener(v -> lookupTrackWithMetadata());
        buttonFingerprint.setEnabled(false);
        buttonFingerprint.setOnClickListener(v -> lookupTrackWithFingerprint());

        recordings = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(TaggerViewModel.class);
        viewModel.getRecordingData().observe(this, this::processResults);
        viewModel.getMatchedReleaseData().observe(this, this::displayMatchedRelease);

        adapter = new TaggerAdapter(recordings);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.choose_button).setOnClickListener(v -> {
            readStoragePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (!readStoragePermission)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION);
            else chooseAudioFile();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        readStoragePermission = requestCode == READ_STORAGE_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (readStoragePermission)
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
        Log.d("Activity Result received");
        if (requestCode == AUDIO_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("File selected correctly");
            try {
                Uri uri = data.getData();
                String path = PathUtils.getRealPathFromURI(getApplicationContext(), uri);
                audioFile = AudioFileIO.read(new File(path));
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

    private void displayMatchedRelease(Release release) {
        Track track = null;
        if (release != null && release.getMedia() != null && !release.getMedia().isEmpty())
            for (Media media : release.getMedia())
                for (Track search : media.getTracks())
                    if (search.getRecording().getMbid().equalsIgnoreCase(comparisionResult.getTrackMbid()))
                        track = search;

        Recording recording = null;
        if (track != null) recording = track.getRecording();

        if (localTrack != null) {
            View itemView = findViewById(R.id.local_track);
            TextView localRecordingName = itemView.findViewById(R.id.recording_name);
            TextView localRecordingRelease = itemView.findViewById(R.id.recording_release);
            TextView localRecordingDisambiguation = itemView.findViewById(R.id.recording_disambiguation);
            TextView localRecordingArtist = itemView.findViewById(R.id.recording_artist);
            localRecordingName.setText(localTrack.getTitle());

            if (localTrack.getReleases() != null && !localTrack.getReleases().isEmpty())
                setViewVisibility(localTrack.getReleases().get(0).getTitle(), localRecordingRelease);
            setViewVisibility(localTrack.getDisplayArtist(), localRecordingArtist);
            setViewVisibility(localTrack.getDisambiguation(), localRecordingDisambiguation);
        }
        if (recording != null) {
            View itemView2 = findViewById(R.id.searched_track);
            TextView searchedRecordingName = itemView2.findViewById(R.id.recording_name);
            TextView searchedRecordingRelease = itemView2.findViewById(R.id.recording_release);
            TextView searchedRecordingDisambiguation = itemView2.findViewById(R.id.recording_disambiguation);
            TextView searchedRecordingArtist = itemView2.findViewById(R.id.recording_artist);
            searchedRecordingName.setText(recording.getTitle());

            if (recording.getReleases() != null && !recording.getReleases().isEmpty())
                setViewVisibility(recording.getReleases().get(0).getTitle(), searchedRecordingRelease);
            setViewVisibility(recording.getDisplayArtist(), searchedRecordingArtist);
            setViewVisibility(recording.getDisambiguation(), searchedRecordingDisambiguation);

            writeTagsToFile(track, release);
        }
    }

    private void writeTagsToFile(Track track, Release release) {
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

            Log.d(audioFile.getFile().getAbsolutePath());

        } catch (Exception e) {
            Log.e(e.getMessage());
        }
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }

}
