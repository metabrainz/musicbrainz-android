package org.metabrainz.mobile.util;

import android.util.Pair;

import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

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
    private static Levenshtein levenshtein = new Levenshtein();
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

        String[] firstList = first.toLowerCase().split("\\W+");
        List<String> secondList = Arrays.asList(second.toLowerCase().split("\\W+"));
        double total = 0.0, totalScore = 0.0;

        for (String firstWord : firstList) {
            double max = 0, currentScore = 0;
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
    }

    public static double compareTracks(Recording localTrack, Recording searchedTrack) {
        List<Pair<Double, Integer>> scoreList = new ArrayList<>();
        double score = 0.0;
        if (localTrack != null) {
            if (localTrack.getTitle() != null && !localTrack.getTitle().isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(localTrack.getTitle(), searchedTrack.getTitle()),
                        WEIGHTS.get(TITLE)));

            scoreList.add(new Pair<>(
                    lengthScore(localTrack.getLength(), searchedTrack.getLength()),
                    WEIGHTS.get(TRACK_LENGTH)));

            if (!localTrack.getDisplayArtist().isEmpty() && !searchedTrack.getDisplayArtist().isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(localTrack.getDisplayArtist(), searchedTrack.getDisplayArtist()),
                        WEIGHTS.get(ARTIST)));

            score = linearCombinationOfWeights(scoreList);

            Release localRelease;
            if (localTrack.getReleases() != null && localTrack.getReleases().size() > 0)
                localRelease = localTrack.getReleases().get(0);
            else localRelease = null;

            if (localRelease != null) {
                double releaseScore, max = 0.0;
                for (Release searchedRelease : searchedTrack.getReleases()) {
                    releaseScore = compareReleaseParts(localRelease, searchedRelease);
                    if (releaseScore > max)
                        max = releaseScore;
                }
                if (searchedTrack.getScore() != 0)
                    score *= searchedTrack.getScore() / 100;
            }

        }
        return score;
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

            if (localRelease.getDisplayArtist() != null && !localRelease.getDisplayArtist().isEmpty()
                    && searchedRelease.getDisplayArtist() != null && !searchedRelease.getDisplayArtist().isEmpty())
                scoreList.add(new Pair<>(
                        calculateMultiWordSimilarity(
                                localRelease.getDisplayArtist(), searchedRelease.getDisplayArtist()),
                        WEIGHTS.get(ARTIST)
                ));

            if (localRelease.getTrackCount() > 0 && searchedRelease.getTrackCount() > 0) {
                int localTrackCount = localRelease.getTrackCount();
                int searchedTrackCount = searchedRelease.getTrackCount();
                double trackCountScore = 0.0;
                if (localTrackCount > searchedTrackCount)
                    trackCountScore = 0.0;
                else if (localTrackCount < searchedTrackCount)
                    trackCountScore = 0.3;
                else
                    trackCountScore = 1.0;
                scoreList.add(new Pair<>(trackCountScore, WEIGHTS.get(TOTAL_TRACKS)));
            }

        }
        return linearCombinationOfWeights(scoreList);
    }

    public static double linearCombinationOfWeights(List<Pair<Double, Integer>> weightList) {
        double weightSum = 0.0;
        for (Pair<Double, Integer> weight : weightList)
            weightSum += weight.first * weight.second;
        return weightSum;
    }
}
