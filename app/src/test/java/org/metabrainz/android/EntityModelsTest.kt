package org.metabrainz.android

import com.google.gson.Gson
import org.junit.Test
import org.metabrainz.android.EntityTestUtils.loadResourceAsString
import org.metabrainz.android.EntityTestUtils.testArtist
import org.metabrainz.android.EntityTestUtils.testLabel
import org.metabrainz.android.EntityTestUtils.testRecording
import org.metabrainz.android.EntityTestUtils.testRelease
import org.metabrainz.android.EntityTestUtils.testReleaseGroup
import org.metabrainz.android.model.mbentity.Recording
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.model.mbentity.ReleaseGroup

class EntityModelsTest {
    @Test
    fun testArtistModel() {
        val artist: org.metabrainz.android.model.mbentity.Artist = Gson().fromJson(loadResourceAsString("artist_lookup.json"),
            org.metabrainz.android.model.mbentity.Artist::class.java)
        AssertionUtils.checkArtistAssertions(testArtist, artist)
    }

    @Test
    fun testReleaseModel() {
        val release: Release = Gson().fromJson(
            loadResourceAsString("release_lookup.json"),
            Release::class.java)
        AssertionUtils.checkReleaseAssertions(testRelease, release)
    }

    @Test
    fun testReleaseGroupModel() {
        val releaseGroup: ReleaseGroup = Gson().fromJson(
            loadResourceAsString("release-group_lookup.json"),
            ReleaseGroup::class.java
        )
        AssertionUtils.checkReleaseGroupAssertions(testReleaseGroup, releaseGroup)
    }

    @Test
    fun testLabelModel() {
        val label: org.metabrainz.android.model.mbentity.Label = Gson().fromJson(
            loadResourceAsString("label_lookup.json"),
            org.metabrainz.android.model.mbentity.Label::class.java)
        AssertionUtils.checkLabelAssertions(testLabel, label)
    }

    @Test
    fun testRecordingModel() {
        val recording: Recording = Gson().fromJson(
            loadResourceAsString("recording_lookup.json"),
            Recording::class.java)
        AssertionUtils.checkRecordingAssertions(testRecording, recording)
    }
}