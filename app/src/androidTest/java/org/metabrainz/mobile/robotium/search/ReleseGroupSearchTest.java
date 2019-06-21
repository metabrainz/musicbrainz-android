package org.metabrainz.mobile.test.robotium.search;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.jayway.android.robotium.solo.Solo;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.presentation.features.search.SearchActivity;

public class ReleseGroupSearchTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public ReleseGroupSearchTest() {
        super(DashboardActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @LargeTest
    public void testSearchWithExistingRG() throws Exception {
        solo.pressSpinnerItem(0, 1);
        solo.enterText(0, "codes and keys");
        clickSearch();
        solo.assertCurrentActivity("Expected Search Activity", SearchActivity.class);
        assertTrue(solo.searchText("Release"));
        solo.clickInList(0);
        solo.assertCurrentActivity("Expected Release Activity", ReleaseActivity.class);
        solo.clickInList(0);
        assertTrue(solo.searchText("Death Cab for Cutie"));
    }

    private void clickSearch() {
        solo.clickOnView(getActivity().findViewById(R.id.search_btn));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}