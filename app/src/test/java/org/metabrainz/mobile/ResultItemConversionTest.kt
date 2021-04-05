package org.metabrainz.mobile

import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils.getEntityAsResultItem
import org.junit.Test
import org.metabrainz.mobile.EntityTestUtils.testArtist
import org.metabrainz.mobile.EntityTestUtils.testLabel
import org.metabrainz.mobile.EntityTestUtils.testRecording
import org.metabrainz.mobile.EntityTestUtils.testRelease
import org.metabrainz.mobile.EntityTestUtils.testReleaseGroup

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