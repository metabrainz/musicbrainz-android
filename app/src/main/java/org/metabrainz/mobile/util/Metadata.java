package org.metabrainz.mobile.util;

import android.media.MediaMetadataRetriever;
import android.util.Pair;

import com.geecko.fpcalc.FpCalc;
import com.simplecityapps.ktaglib.AudioFile;

import org.jaudiotagger.tag.FieldKey;
import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.Track;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Metadata {

    public static List<Pair<String, String>> getDefaultTagList(AudioFile audioFile) {
        List<Pair<String, String>> tags = new ArrayList<>();
        if (audioFile.getTitle() != null)
            tags.add(new Pair<>("title", audioFile.getTitle()));
        if (audioFile.getArtist() != null)
            tags.add(new Pair<>("artist", audioFile.getArtist()));
        if (audioFile.getAlbum() != null)
            tags.add(new Pair<>("release", audioFile.getAlbum()));
        if (audioFile.getTrack() != null)
            tags.add(new Pair<>("tnum", audioFile.getTrack().toString()));
        if (audioFile.getTrackTotal() != null)
            tags.add(new Pair<>("tracks", audioFile.getTrackTotal().toString()));
        Log.d(tags.toString());
        return tags;
    }

    public static Recording getRecordingFromFile(AudioFile file) {
        if (file != null) {
            String title = file.getTitle();
            String artist = file.getArtist();
            String release = file.getAlbum();
            String trackNumber = String.valueOf(file.getTrack());
            String totalTracks = String.valueOf(file.getTrackTotal());

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
                try {
                    tempRelease.setTrackCount(Integer.parseInt(totalTracks));
                    recording.setTrackCount(Integer.parseInt(totalTracks));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            recording.getReleases().add(tempRelease);
            if (file.getDuration() != null)
                recording.setLength(file.getDuration() * 1000);
            // TODO: Set track number

            return recording;
        }
        return null;
    }

    public static String getAudioFingerprint(File file) {
        String[] args = {"-length", "120", "-plain", file.getAbsolutePath()};
        String fingerprint = FpCalc.fpCalc(args).trim();
        Log.d(fingerprint);
        return fingerprint;
    }

    public static AudioFile getAudioFileFromTrack(Track track) {
        String title = track.getTitle() != null ? track.getTitle() : "";
        int duration = (int) track.getLength();
        int trackNum = track.getPosition();
        String albumArtist = "", artist = "", album = "";
        int trackTotal = 0;
        Recording recording = track.getRecording();
        if (recording != null) {
            trackTotal = recording.getTrackCount();
            if (recording.getReleases() != null && recording.getReleases().size() > 0) {
                if (recording.getReleases().get(0).getArtistCredits() != null)
                    artist = EntityUtils.getDisplayArtist(recording.getReleases()
                            .get(0).getArtistCredits());
                album = recording.getReleases().get(0).getTitle();
            }
        }

        return new AudioFile("", 0, 0, title, albumArtist, artist, album,
                trackNum, trackTotal, 0,0, duration, "", "");
    }
}