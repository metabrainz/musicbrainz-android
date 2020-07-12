package org.metabrainz.mobile.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import org.metabrainz.mobile.data.sources.api.entities.ArtistCredit;
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.Media;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.Medium;
import org.metabrainz.mobile.data.sources.api.entities.acoustid.Result;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.debatty.java.stringsimilarity.Levenshtein;

public class TaggerUtils {


    public static final long LENGTH_SCORE_THRESHOLD_MS = 30000;
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String TRACK_LENGTH = "length";
    public static final String TOTAL_TRACKS = "totaltracks";
    public static final String TRACK_NUMBER = "tracknumber";
    public static final double THRESHOLD = 0.6;
    public static final double UNMATCHED_WORDS_WEIGHT = 0.4;
    private static final Levenshtein levenshtein = new Levenshtein();
    public static final Map<String, Integer> WEIGHTS = initializeWeights();

    public static Map<String, Integer> initializeWeights() {
        Map<String, Integer> map = new HashMap<>();
        map.put(TITLE, 13);
        map.put(ARTIST, 4);
        map.put(ALBUM, 5);
        map.put(TRACK_LENGTH, 10);
        // map.put(TRACK_NUMBER, 7);
        map.put(TOTAL_TRACKS, 4);
        return Collections.unmodifiableMap(map);
    }

    public static double calculateSimilarity(String firstWord, String secondWord) {
        return levenshtein.distance(firstWord, secondWord);
    }

    // Calculate similarity of multi word strings
    public static double calculateMultiWordSimilarity(String first, String second) {
        if (first != null && second != null) {
            String[] firstList = first.toLowerCase().split("\\W+");
            List<String> secondList = new ArrayList<>(Arrays.asList(second.toLowerCase().split("\\W+")));
            double total = 0.0, totalScore = 0.0;

            for (String firstWord : firstList) {
                double max = 0.0, currentScore = 0.0;
                int pos = -1;

                for (String secondWord : secondList) {
                    currentScore = calculateSimilarity(firstWord, secondWord);

                    if (currentScore > max) {
                        max = currentScore;
                        pos = secondList.indexOf(secondWord);
                    }
                }

                if (pos != -1) {
                    totalScore += currentScore;
                    if (currentScore > THRESHOLD)
                        secondList.remove(pos);
                }
                total++;
            }

            total += secondList.size() * UNMATCHED_WORDS_WEIGHT;

            if (total > 0)
                return totalScore / total;
            else
                return 0.0;
        } else return 0.0;
    }

    public static ComparisionResult compareTracks(Recording localTrack, Recording searchedTrack) {
        List<Pair<Double, Integer>> scoreList = new ArrayList<>();
        String releaseMbid = "";
        double score = 0.0;
        if (localTrack != null) {
            if (localTrack.getTitle() != null && !localTrack.getTitle().isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(localTrack.getTitle(), searchedTrack.getTitle()),
                        WEIGHTS.get(TITLE)));

            scoreList.add(new Pair<>(
                    lengthScore(localTrack.getLength(), searchedTrack.getLength()),
                    WEIGHTS.get(TRACK_LENGTH)));

            if (!EntityUtils.getDisplayArtist(localTrack.getArtistCredits()).isEmpty() &&
                    !EntityUtils.getDisplayArtist(localTrack.getArtistCredits()).isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(
                                EntityUtils.getDisplayArtist(localTrack.getArtistCredits()),
                                EntityUtils.getDisplayArtist(localTrack.getArtistCredits())),
                        WEIGHTS.get(ARTIST)));

            score = linearCombinationOfWeights(scoreList);

            Release localRelease;
            if (localTrack.getReleases() != null && localTrack.getReleases().size() > 0)
                localRelease = localTrack.getReleases().get(0);
            else localRelease = null;

            if (localRelease != null) {
                double max = 0.0;

                // Base case if no suitable release found
                if (!searchedTrack.getReleases().isEmpty())
                    releaseMbid = searchedTrack.getReleases().get(0).getMbid();
                for (Release searchedRelease : searchedTrack.getReleases()) {
                    double releaseScore = compareReleaseParts(localRelease, searchedRelease);
                    if (releaseScore > max) {
                        max = releaseScore;
                        releaseMbid = searchedRelease.getMbid();
                    }
                }
            }

            if (searchedTrack.getScore() != 0)
                score *= searchedTrack.getScore() / 100.0;

        }
        Log.d(searchedTrack.getTitle() + " score: " + score);
        return new ComparisionResult(score, releaseMbid, searchedTrack.getMbid());
    }

    public static double lengthScore(long firstLength, long secondLength) {
        return 1.0 - Math.min(firstLength - secondLength, LENGTH_SCORE_THRESHOLD_MS) / (double) LENGTH_SCORE_THRESHOLD_MS;
    }

    public static double compareReleaseParts(Release localRelease, Release searchedRelease) {
        List<Pair<Double, Integer>> scoreList = new ArrayList<>();
        if (localRelease != null && searchedRelease != null) {
            if (localRelease.getTitle() != null && !localRelease.getTitle().isEmpty() &&
                    searchedRelease.getTitle() != null && !searchedRelease.getTitle().isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(localRelease.getTitle(), searchedRelease.getTitle()),
                        WEIGHTS.get(ALBUM)));

            if (EntityUtils.getDisplayArtist(localRelease.getArtistCredits()) != null && !EntityUtils.getDisplayArtist(localRelease.getArtistCredits()).isEmpty()
                    && EntityUtils.getDisplayArtist(searchedRelease.getArtistCredits()) != null && !EntityUtils.getDisplayArtist(searchedRelease.getArtistCredits()).isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(
                                EntityUtils.getDisplayArtist(localRelease.getArtistCredits()),
                                EntityUtils.getDisplayArtist(searchedRelease.getArtistCredits())),
                        WEIGHTS.get(ARTIST)
                ));

            if (localRelease.getTrackCount() > 0 && searchedRelease.getTrackCount() > 0) {
                int localTrackCount = localRelease.getTrackCount();
                int searchedTrackCount = searchedRelease.getTrackCount();
                double trackCountScore;
                if (localTrackCount > searchedTrackCount)
                    trackCountScore = 0.0;
                else if (localTrackCount < searchedTrackCount)
                    trackCountScore = 0.3;
                else
                    trackCountScore = 1.0;
                scoreList.add(new Pair<>(trackCountScore, WEIGHTS.get(TOTAL_TRACKS)));
            }

            // TODO : Add release formats, countries and types
        }
        return linearCombinationOfWeights(scoreList);
    }

    public static double linearCombinationOfWeights(List<Pair<Double, Integer>> weightList) {
        double weightSum = 0.0;
        for (Pair<Double, Integer> weight : weightList)
            weightSum += weight.first * weight.second;
        return weightSum;
    }

    public static List<Recording> parseResults(List<Result> results) {
        List<Recording> recordings = new ArrayList<>();
        if (results != null && !results.isEmpty()) {

            for (Result result : results) {

                int maxSources = 1;
                for (org.metabrainz.mobile.data.sources.api.entities.acoustid.Recording
                        responseRecording : result.getRecordings()) {

                    if (responseRecording.getSources() > maxSources)
                        maxSources = responseRecording.getSources();

                    Recording recording = new Recording();

                    List<Release> releases = recording.getReleases();

                    for (org.metabrainz.mobile.data.sources.api.entities.acoustid.ReleaseGroup
                            responseReleaseGroup : responseRecording.getReleaseGroups()) {

                        for (org.metabrainz.mobile.data.sources.api.entities.acoustid.Release
                                responseRelease : responseReleaseGroup.getReleases()) {

                            Release release = new Release();

                            release.setMbid(responseRelease.getId());

                            ReleaseGroup releaseGroup = new ReleaseGroup();
                            releaseGroup.setMbid(responseReleaseGroup.getId());
                            release.setReleaseGroup(releaseGroup);

                            if (responseRelease.getTitle() != null && !responseRelease.getTitle().isEmpty())
                                release.setTitle(releaseGroup.getTitle());

                            if (responseRelease.getCountry() != null && !responseRelease.getCountry().isEmpty())
                                release.setCountry(responseRelease.getCountry());

                            if (responseRelease.getMediums() != null) {
                                for (Medium medium : responseRelease.getMediums()) {
                                    Media media = new Media();

                                    if (medium.getFormat() != null && !medium.getFormat().isEmpty())
                                        media.setFormat(medium.getFormat());

                                    //Track track = new Track();

                                    media.setTrackCount(medium.getTrackCount());

                                    release.getMedia().add(media);
                                }
                            }
                            releases.add(release);
                        }
                    }

                    List<ArtistCredit> artistCredits = recording.getArtistCredits();

                    for (org.metabrainz.mobile.data.sources.api.entities.acoustid.Artist
                            responseArtist : responseRecording.getArtists()) {

                        ArtistCredit artistCredit = new ArtistCredit();
                        Artist artist = new Artist();

                        artist.setMbid(responseArtist.getId());

                        if (responseArtist.getName() != null && !responseArtist.getName().isEmpty()) {
                            artistCredit.setName(responseArtist.getName());
                            artist.setName(responseArtist.getName());
                            artist.setSortName(responseArtist.getName());
                        }

                        if (responseArtist.getJoinphrase() != null && !responseArtist.getJoinphrase().isEmpty())
                            artistCredit.setJoinphrase(responseArtist.getJoinphrase());

                        artistCredit.setArtist(artist);

                        artistCredits.add(artistCredit);
                    }

                    recording.setMbid(responseRecording.getId());

                    if (responseRecording.getTitle() != null && !responseRecording.getTitle().isEmpty())
                        recording.setTitle(responseRecording.getTitle());

                    recording.setLength(responseRecording.getDuration() * 1000);

                    recording.setScore(responseRecording.getSources() / maxSources * 100);
                    recordings.add(recording);
                }
            }
        }
        return recordings;
    }

    public static String[] getPermissionsList(Context context) {
        boolean readStoragePermission, writeStoragePermission;
        readStoragePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        writeStoragePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        ArrayList<String> permissionsList = new ArrayList<>();
        if (!readStoragePermission)
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!writeStoragePermission)
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String[] permissions = new String[permissionsList.size()];
        permissions = permissionsList.toArray(permissions);
        return permissions;
    }
}
