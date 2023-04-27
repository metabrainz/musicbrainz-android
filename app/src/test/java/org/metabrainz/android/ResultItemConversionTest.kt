package org.metabrainz.android

import org.metabrainz.android.ui.adapters.ResultItemUtils.getEntityAsResultItem
import org.junit.Test
import org.metabrainz.android.EntityTestUtils.testArtist
import org.metabrainz.android.EntityTestUtils.testLabel
import org.metabrainz.android.EntityTestUtils.testRecording
import org.metabrainz.android.EntityTestUtils.testRelease
import org.metabrainz.android.EntityTestUtils.testReleaseGroup

class ResultItemConversionTest {
    @Test
    fun testConvertArtist() {
        val testItem = ResultItemTestUtils.testArtistResultItem
        val item = getEntityAsResultItem(testArtist)
        AssertionUtils.checkResultItemAssertions(testItem, item)
    }

    @Test
    fun testConvertRelease() {
        val testItem = ResultItemTestUtils.testReleaseResultItem
        val item = getEntityAsResultItem(testRelease)
        AssertionUtils.checkResultItemAssertions(testItem, item)
    }

    @Test
    fun testConvertLabel() {
        val testItem = ResultItemTestUtils.testLabelResultItem
        val item = getEntityAsResultItem(testLabel)
        AssertionUtils.checkResultItemAssertions(testItem, item)
    }

    @Test
    fun testConvertRecording() {
        val testItem = ResultItemTestUtils.testRecordingResultItem
        val item = getEntityAsResultItem(testRecording)
        AssertionUtils.checkResultItemAssertions(testItem, item)
    }

    @Test
    fun testConvertReleaseGroup() {
        val testItem = ResultItemTestUtils.testReleaseGroupResultItem
        val item = getEntityAsResultItem(testReleaseGroup)
        AssertionUtils.checkResultItemAssertions(testItem, item)
    }
}