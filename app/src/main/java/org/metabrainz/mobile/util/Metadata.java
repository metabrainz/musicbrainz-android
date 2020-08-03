package org.metabrainz.mobile.util;

import android.media.MediaMetadataRetriever;
import android.util.Pair;

import com.geecko.fpcalc.FpCalc;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Metadata {

    public static List<Pair<String, String>> getDefaultTagList(AudioFile audioFile) {
        List<Pair<String, String>> tags = new ArrayList<>();
        addTagToList(tags, "track", FieldKey.TITLE, audioFile);
        addTagToList(tags, "artist", FieldKey.ARTIST, audioFile);
        addTagToList(tags, "release", FieldKey.ALBUM, audioFile);
        addTagToList(tags, "tnum", FieldKey.TRACK, audioFile);
        addTagToList(tags, "tracks", FieldKey.TRACK_TOTAL, audioFile);
        return tags;
    }

    private static void addTagToList(List<Pair<String, String>> tags, String queryKey, FieldKey key, AudioFile audioFile) {
        String queryValue = audioFile.getTag().getFirst(key);
        tags.add(new Pair<>(queryKey, queryValue));
    }

    public static Recording getRecordingFromFile(AudioFile file) {
        if (file != null) {
            String title = file.getTag().getFirst(FieldKey.TITLE);
            String artist = file.getTag().getFirst(FieldKey.ARTIST);
            String release = file.getTag().getFirst(FieldKey.ALBUM);
            String trackNumber = file.getTag().getFirst(FieldKey.TRACK);
            String totalTracks = file.getTag().getFirst(FieldKey.TRACK_TOTAL);

            Recording recording = new Recording();
            recording.setTitle(title);

            Artist tempArtist = new Artist();
            tempArtist.setName(artist);
            ArtistCredit artistCredit = new ArtistCredit();
            artistCredit.setArtist(tempArtist);
            recording.getArtistCredits().add(artistCredit);

            Release tempRelease = new Release();
            tempRelease.setTitle(release);
            if (!totalTracks.isEmpty()) {
                tempRelease.setTrackCount(Integer.parseInt(totalTracks));
                recording.setTrackCount(Integer.parseInt(totalTracks));
            }
            recording.getReleases().add(tempRelease);
            recording.setLength(getDuration(file.getFile()));
            // TODO: Set track number

            return recording;
        }
        return null;
    }

    public static long getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Long.parseLong(duration);
    }

    public static String getAudioFingerprint(File file) {
        String[] args = {"-length", "120", "-plain", file.getAbsolutePath()};
        String fingerprint = FpCalc.fpCalc(args).trim();
        Log.d(fingerprint);
        return fingerprint;
    }
}