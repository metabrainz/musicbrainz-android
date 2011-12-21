/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.test.robotium.navigation;

import org.musicbrainz.mobile.activity.AboutActivity;
import org.musicbrainz.mobile.activity.DonateActivity;
import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.LoginActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class BasicNavigationTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public BasicNavigationTest() {
        super("org.musicbrainz.mobile", DashboardActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testDashboardNavigation() throws Exception {
        openCloseAboutActivity();
        openCloseDonateActivity();
        openCloseLoginActivity();
    }

    private void openCloseAboutActivity() {
        solo.clickOnButton("About");
        solo.assertCurrentActivity("", AboutActivity.class);
        solo.goBack();
    }

    private void openCloseDonateActivity() {
        solo.clickOnButton("Donate");
        solo.assertCurrentActivity("", DonateActivity.class);
        solo.goBack();
    }

    private void openCloseLoginActivity() {
        solo.clickOnButton("Log In");
        solo.assertCurrentActivity("", LoginActivity.class);
        solo.goBack();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
