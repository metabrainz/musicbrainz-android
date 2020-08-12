package org.metabrainz.mobile;

import org.metabrainz.mobile.presentation.features.adapters.ResultItem;

import static org.metabrainz.mobile.EntityTestUtils.getTestArtistMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestLabelMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestRecordingMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseGroupMBID;
import static org.metabrainz.mobile.EntityTestUtils.getTestReleaseMBID;

public class ResultItemTestUtils {

    public static ResultItem getTestArtistResultItem() {
        return new ResultItem(
                getTestArtistMBID(),
                "Ed Sheeran",
                "UK singer-songwriter",
                "Person",
                "GB");
    }

    public static ResultItem getTestReleaseResultItem() {
        return new ResultItem(
                getTestReleaseMBID(),
                "+",
                "",
                "",
                "");
    }

    public static ResultItem getTestLabelResultItem() {
        return new ResultItem(
                getTestLabelMBID(),
                "Speed Records",
                "India",
                "Original Production",
                "IN");
    }

    public static ResultItem getTestReleaseGroupResultItem() {
        return new ResultItem(
                getTestReleaseGroupMBID(),
                "+",
                "plus",
                "",
                "Album");
    }

    public static ResultItem getTestRecordingResultItem() {
        return new ResultItem(
                getTestRecordingMBID(),
                "Plus Plus",
                "no disambiguation",
                "",
                ""
        );
    }

}
