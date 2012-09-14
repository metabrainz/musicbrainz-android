package org.musicbrainz.mobile.test.robotium.navigation;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.AboutActivity;
import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.DonateActivity;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.activity.SettingsActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.jayway.android.robotium.solo.Solo;

public class DashboardNavigationTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public DashboardNavigationTest() {
        super(DashboardActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @LargeTest
    public void testOpenCloseAbout() {
        solo.clickOnView(solo.getView(R.id.dash_about));
        solo.assertCurrentActivity("Expected About Activity", AboutActivity.class);
        solo.goBack();
    }

    @LargeTest
    public void testOpenCloseDonate() {
        solo.clickOnView(solo.getView(R.id.dash_donate));
        solo.assertCurrentActivity("Expected Donate Activity", DonateActivity.class);
        solo.goBack();
    }

    @LargeTest
    public void testOpenCloseCollection() {
        solo.clickOnView(solo.getView(R.id.dash_collections));
        solo.assertCurrentActivity("Expected Login Activity", LoginActivity.class);
        solo.goBack();
    }
    
    @LargeTest
    public void testOpenCloseSettings() {
        solo.clickOnMenuItem("Settings");
        solo.assertCurrentActivity("Expected Settings Activity", SettingsActivity.class);
        solo.goBack();
    }
    
    @LargeTest
    public void testOpenCloseLogIn() {
        solo.clickOnMenuItem("Log In");
        solo.assertCurrentActivity("Expected Login Activity", LoginActivity.class);
        solo.goBack();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
