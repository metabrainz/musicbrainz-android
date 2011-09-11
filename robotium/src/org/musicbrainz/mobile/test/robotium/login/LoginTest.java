package org.musicbrainz.mobile.test.robotium.login;

import org.musicbrainz.mobile.test.robotium.Secrets;
import org.musicbrainz.mobile.ui.activity.HomeActivity;
import org.musicbrainz.mobile.ui.activity.LoginActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class LoginTest extends ActivityInstrumentationTestCase2<HomeActivity> {
	
	private static final int DIALOG_TIMEOUT = 2 * 1000;
	
	private Solo solo;
	
	public LoginTest() {
		super("org.musicbrainz.mobile", HomeActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testWithInvalidPassword() throws Exception {
		solo.clickOnButton("Log In");
		solo.assertCurrentActivity("", LoginActivity.class);
		enterInvalidCredentials();
		solo.waitForDialogToClose(DIALOG_TIMEOUT);
		solo.assertCurrentActivity("", LoginActivity.class);
	}
	
	private void enterInvalidCredentials() {
		solo.enterText(0, "jdamcd");
		solo.enterText(1, "blah");
		solo.clickOnButton("Log In");
	}
	
	public void ignore_testWithValidPassword() throws Exception {
		solo.clickOnButton("Log In");
		solo.assertCurrentActivity("", LoginActivity.class);
		enterValidCredentials();
		solo.waitForDialogToClose(DIALOG_TIMEOUT);
		solo.assertCurrentActivity("", HomeActivity.class);
	}
	
	private void enterValidCredentials() {
		solo.enterText(0, "jdamcd");
		solo.enterText(1, Secrets.PASSWORD);
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
		try {
			solo.finalize(); 	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	} 

}
