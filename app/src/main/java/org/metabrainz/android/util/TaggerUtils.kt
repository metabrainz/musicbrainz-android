package org.metabrainz.android.util

import android.util.Pair
import info.debatty.java.stringsimilarity.Levenshtein
import org.metabrainz.android.data.sources.api.entities.ArtistCredit
import org.metabrainz.android.data.sources.api.entities.EntityUtils
import org.metabrainz.android.data.sources.api.entities.Media
import org.metabrainz.android.data.sources.api.entities.acoustid.Result
import org.metabrainz.android.data.sources.api.entities.mbentity.Artist
import org.metabrainz.android.data.sources.api.entities.mbentity.Recording
import org.metabrainz.android.data.sources.api.entities.mbentity.Release
import org.metabrainz.android.data.sources.api.entities.mbentity.ReleaseGroup
import org.metabrainz.android.util.Log.d
import java.util.*
import kotlin.math.min

object TaggerUtils {
    const val LENGTH_SCORE_THRESHOLD_MS: Long = 30000
    const val TITLE = "title"
    const val ARTIST = "artist"
    const val ALBUM = "album"
    const val TRACK_LENGTH = "length"
    const val TOTAL_TRACKS = "totaltracks"
    const val TRACK_NUMBER = "tracknumber"
    const val THRESHOLD = 0.6
    const val UNMATCHED_WORDS_WEIGHT = 0.4
    private val levenshtein = Levenshtein()
    val WEIGHTS = initializeWeights()

    fun initializeWeights(): Map<String, Int> {
        val map: MutableMap<String, Int> = HashMap()
        map[TITLE] = 13
        map[ARTIST] = 4
        map[ALBUM] = 5
        map[TRACK_LENGTH] = 10
        // map.put(TRACK_NUMBER, 7);
        map[TOTAL_TRACKS] = 4
        return Collections.unmodifiableMap(map)
    }

    private fun calculateSimilarity(firstWord: String?, secondWord: String?): Double {
        return levenshtein.distance(firstWord, secondWord)
    }

    // Calculate similarity of multi word strings
    private fun calculateMultiWordSimilarity(first: String?, second: String?): Double {
        return if (first != null && second != null) {
            val firstList = first.toLowerCase(Locale.ROOT).split("\\W+").toTypedArray()
            val secondList: MutableList<String> = ArrayList(listOf(*second.toLowerCase(Locale.ROOT).split("\\W+").toTypedArray()))
            var total = 0.0
            var totalScore = 0.0
            for (firstWord in firstList) {
                var max = 0.0
                var currentScore = 0.0
                var pos = -1
                for (secondWord in secondList) {
                    currentScore = calculateSimilarity(firstWord, secondWord)
                    if (currentScore > max) {
                        max = currentScore
                        pos = secondList.indexOf(secondWord)
                    }
                }
                if (pos != -1) {
                    totalScore += currentScore
                    if (currentScore > THRESHOLD) secondList.removeAt(pos)
                }
                total++
            }
            total += secondList.size * UNMATCHED_WORDS_WEIGHT
            if (total > 0) totalScore / total else 0.0
        } else 0.0
    }

    fun compareTracks(localTrack: Recording?, searchedTrack: Recording): ComparisonResult {
        val scoreList: MutableList<Pair<Double, Int?>> = ArrayList()
        var releaseMbid: String? = ""
        var score = 0.0
        if (localTrack != null) {
            if (localTrack.title != null && localTrack.title!!.isNotEmpty()) scoreList.add(Pair(
                    calculateMultiWordSimilarity(localTrack.title, searchedTrack.title),
                    WEIGHTS[TITLE]))
            scoreList.add(Pair(
                    lengthScore(localTrack.length, searchedTrack.length),
                    WEIGHTS[TRACK_LENGTH]))
            if (EntityUtils.getDisplayArtist(localTrack.artistCredits).isNotEmpty() &&
                    EntityUtils.getDisplayArtist(localTrack.artistCredits).isNotEmpty()) scoreList.add(Pair(
                    calculateMultiWordSimilarity(
                            EntityUtils.getDisplayArtist(localTrack.artistCredits),
                            EntityUtils.getDisplayArtist(localTrack.artistCredits)),
                    WEIGHTS[ARTIST]))
            score = linearCombinationOfWeights(scoreList)
            val localRelease: Release? = if (localTrack.releases.size > 0) localTrack.releases[0] else null
            if (localRelease != null) {
                var max = 0.0

                // Base case if no suitable release found
                if (searchedTrack.releases.isNotEmpty()) releaseMbid = searchedTrack.releases[0].mbid
                for (searchedRelease in searchedTrack.releases) {
                    val releaseScore = compareReleaseParts(localRelease, searchedRelease)
                    if (releaseScore > max) {
                        max = releaseScore
                        releaseMbid = searchedRelease.mbid
                    }
                }
            }
            if (searchedTrack.score != 0) score *= searchedTrack.score / 100.0
        }
        d(searchedTrack.title + " score: " + score)
        return ComparisonResult(score, releaseMbid, searchedTrack.mbid)
    }

    fun lengthScore(firstLength: Long, secondLength: Long): Double {
        return 1.0 - min(firstLength - secondLength, LENGTH_SCORE_THRESHOLD_MS) / LENGTH_SCORE_THRESHOLD_MS.toDouble()
    }

    fun compareReleaseParts(localRelease: Release?, searchedRelease: Release?): Double {
        val scoreList: MutableList<Pair<Double, Int?>> = ArrayList()
        if (localRelease != null && searchedRelease != null) {
            if (localRelease.title != null && localRelease.title!!.isNotEmpty() && searchedRelease.title != null && searchedRelease.title!!.isNotEmpty()) scoreList.add(Pair(
                    calculateMultiWordSimilarity(localRelease.title, searchedRelease.title),
                    WEIGHTS[ALBUM]))
            if (EntityUtils.getDisplayArtist(localRelease.artistCredits).isNotEmpty() && EntityUtils.getDisplayArtist(searchedRelease.artistCredits).isNotEmpty()) scoreList.add(Pair(
                    calculateMultiWordSimilarity(
                            EntityUtils.getDisplayArtist(localRelease.artistCredits),
                            EntityUtils.getDisplayArtist(searchedRelease.artistCredits)),
                    WEIGHTS[ARTIST]
            ))
            if (localRelease.trackCount > 0 && searchedRelease.trackCount > 0) {
                val localTrackCount = localRelease.trackCount
                val searchedTrackCount = searchedRelease.trackCount
                val trackCountScore: Double = if (localTrackCount > searchedTrackCount) 0.0 else if (localTrackCount < searchedTrackCount) 0.3 else 1.0
                scoreList.add(Pair(trackCountScore, WEIGHTS[TOTAL_TRACKS]))
            }

            // TODO : Add release formats, countries and types
        }
        return linearCombinationOfWeights(scoreList)
    }

    fun linearCombinationOfWeights(weightList: List<Pair<Double, Int?>>): Double {
        var weightSum = 0.0
        for (weight in weightList) weightSum += weight.first * weight.second!!
        return weightSum
    }

    fun parseResults(results: List<Result>?): List<Recording> {
        val recordings: MutableList<Recording> = ArrayList()
        if (results != null && !results.isEmpty()) {
            for (result in results) {
                var maxSources = 1
                for (responseRecording in result.recordings) {
                    if (responseRecording.sources > maxSources) maxSources = responseRecording.sources
                    val recording = Recording()
                    val releases = recording.releases
                    for (responseReleaseGroup in responseRecording.releaseGroups) {
                        for (responseRelease in responseReleaseGroup.releases) {
                            val release = Release()
                            release.mbid = responseRelease.id
                            val releaseGroup = ReleaseGroup()
                            releaseGroup.mbid = responseReleaseGroup.id
                            release.releaseGroup = releaseGroup
                            if (responseRelease.title != null && !responseRelease.title!!.isEmpty()) release.title = releaseGroup.title
                            if (responseRelease.country != null && !responseRelease.country!!.isEmpty()) release.country = responseRelease.country
                            for (medium in responseRelease.mediums) {
                                val media = Media()
                                if (medium.format != null && medium.format!!.isNotEmpty()) media.format = medium.format

                                //Track track = new Track();
                                media.trackCount = medium.trackCount
                                release.media!!.add(media)
                            }
                            releases.add(release)
                        }
                    }
                    val artistCredits = recording.artistCredits
                    for (responseArtist in responseRecording.artists) {
                        val artistCredit = ArtistCredit()
                        val artist = Artist()
                        artist.mbid = responseArtist.id
                        if (responseArtist.name != null && responseArtist.name!!.isNotEmpty()) {
                            artistCredit.name = responseArtist.name
                            artist.name = responseArtist.name
                            artist.sortName = responseArtist.name
                        }
                        if (responseArtist.joinphrase != null && responseArtist.joinphrase!!.isNotEmpty()) artistCredit.joinphrase = responseArtist.joinphrase
                        artistCredit.artist = artist
                        artistCredits.add(artistCredit)
                    }
                    recording.mbid = responseRecording.id
                    if (responseRecording.title != null && responseRecording.title!!.isNotEmpty()) recording.title = responseRecording.title
                    recording.length = (responseRecording.duration * 1000).toLong()
                    recording.score = responseRecording.sources / maxSources * 100
                    recordings.add(recording)
                }
            }
        }
        return recordings
    }
}