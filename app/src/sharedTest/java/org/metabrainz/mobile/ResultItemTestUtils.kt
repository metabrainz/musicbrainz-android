package org.metabrainz.mobile

import org.metabrainz.mobile.presentation.features.adapters.ResultItem
import org.metabrainz.mobile.EntityTestUtils.testArtistMBID
import org.metabrainz.mobile.EntityTestUtils.testLabelMBID
import org.metabrainz.mobile.EntityTestUtils.testRecordingMBID
import org.metabrainz.mobile.EntityTestUtils.testReleaseGroupMBID
import org.metabrainz.mobile.EntityTestUtils.testReleaseMBID

object ResultItemTestUtils {
    val testArtistResultItem: ResultItem
        get() = ResultItem(
            testArtistMBID,
            "Ed Sheeran",
            "UK singer-songwriter",
            "Person",
            "GB"
        )
    val testReleaseResultItem: ResultItem
        get() = ResultItem(
            testReleaseMBID,
            "+",
            "",
            "",
            ""
        )
    val testLabelResultItem: ResultItem
        get() = ResultItem(
            testLabelMBID,
            "Speed Records",
            "India",
            "Original Production",
            "IN"
        )
    val testReleaseGroupResultItem: ResultItem
        get() = ResultItem(
            testReleaseGroupMBID,
            "+",
            "plus",
            "",
            "Album"
        )
    val testRecordingResultItem: ResultItem
        get() = ResultItem(
            testRecordingMBID,
            "Plus Plus",
            "no disambiguation",
            "",
            ""
        )
}