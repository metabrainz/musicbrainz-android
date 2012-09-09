package org.musicbrainz.mobile.test.robotium.login;

import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.test.robotium.Account;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class LoginTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private static final int DIALOG_TIMEOUT = 2 * 1000;

    private Solo solo;

    public LoginTest() {
        super(DashboardActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void ignore_testWithInvalidPassword() throws Exception {
        solo.clickOnButton("Log In");
        solo.assertCurrentActivity("Expected Login Activity", LoginActivity.class);
        enterInvalidCredentials();
        solo.waitForDialogToClose(DIALOG_TIMEOUT);
        solo.assertCurrentActivity("Expected Login Activity", LoginActivity.class);
    }

    private void enterInvalidCredentials() {
        solo.enterText(0, Account.USERNAME);
        solo.enterText(1, "blah");
        solo.clickOnButton("Log In");
    }

    public void ignore_testWithValidPassword() throws Exception {
        solo.clickOnButton("Log In");
        solo.assertCurrentActivity("Expected Login Activity", LoginActivity.class);
        enterValidCredentials();
        solo.waitForDialogToClose(DIALOG_TIMEOUT);
        solo.assertCurrentActivity("Expected Dashboard Activity", DashboardActivity.class);
    }

    private void enterValidCredentials() {
        solo.enterText(0, Account.USERNAME);
        solo.enterText(1, Account.PASSWORD);
        solo.clickOnButton("Log In");
    }

    public void ignore_testLoginStatusMessage() throws Exception {
        assertTrue(solo.searchText("Welcome to MusicBrainz!"));
        solo.clickOnButton("Log In");
        enterValidCredentials();
        assertTrue(solo.searchText("Logged in as"));
        solo.clickOnButton("Log Out");
        assertTrue(solo.searchText("Welcome to MusicBrainz!"));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
