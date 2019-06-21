package org.metabrainz.mobile.test.robotium.search;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.jayway.android.robotium.solo.Solo;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.features.artist.ArtistActivity;
import org.metabrainz.mobile.presentation.features.dashboard.DashboardActivity;
import org.metabrainz.mobile.presentation.features.search.SearchActivity;

public class ArtistSearchTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public ArtistSearchTest() {
        super(DashboardActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @LargeTest
    public void testSearchWithNoQuery() throws Exception {
        clickSearch();
        solo.assertCurrentActivity("Expected Dashboard Activity", DashboardActivity.class);
    }

    @LargeTest
    public void testSearchWithExistingArtist() throws Exception {
        solo.enterText(0, "the beatles");
        clickSearch();
        solo.assertCurrentActivity("Expected Search Activity", SearchActivity.class);
        assertTrue(solo.searchText("Artist"));
        solo.clickInList(0);
        solo.assertCurrentActivity("Expected Artist Activity", ArtistActivity.class);
        assertTrue(solo.searchText("The Beatles"));
    }

    @LargeTest
    public void ignoreTestSearchWithUnknownArtist() throws Exception {
        solo.enterText(0, "nbagroypdnebto");
        clickSearch();
        solo.assertCurrentActivity("Expected Search Activity", SearchActivity.class);
        assertTrue(solo.searchText("No results found"));
    }

    private void clickSearch() {
        solo.clickOnView(getActivity().findViewById(R.id.search_btn));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
