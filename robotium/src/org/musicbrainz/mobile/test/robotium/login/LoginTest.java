/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.test.robotium.login;

import org.musicbrainz.mobile.activity.HomeActivity;
import org.musicbrainz.mobile.activity.LoginActivity;
import org.musicbrainz.mobile.test.robotium.Account;

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
		solo.enterText(0, Account.USERNAME);
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
		try {
			solo.finalize(); 	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	} 

}
