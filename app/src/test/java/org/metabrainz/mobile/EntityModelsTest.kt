package org.metabrainz.mobile

import com.google.gson.Gson
import org.junit.Test
import org.metabrainz.mobile.EntityTestUtils.loadResourceAsString
import org.metabrainz.mobile.EntityTestUtils.testArtist
import org.metabrainz.mobile.EntityTestUtils.testLabel
import org.metabrainz.mobile.EntityTestUtils.testRecording
import org.metabrainz.mobile.EntityTestUtils.testRelease
import org.metabrainz.mobile.EntityTestUtils.testReleaseGroup
import org.metabrainz.mobile.data.sources.api.entities.mbentity.*

class EntityModelsTest {
    @Test
    fun testArtistModel() {
        val artist: Artist = Gson().fromJson(loadResourceAsString("artist_lookup.json"),Artist::class.java)
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
        val label: Label = Gson().fromJson(
            loadResourceAsString("label_lookup.json"),
            Label::class.java)
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