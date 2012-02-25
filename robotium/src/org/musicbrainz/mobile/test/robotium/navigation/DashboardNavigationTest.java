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
import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.DonateActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class DashboardNavigationTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public DashboardNavigationTest() {
        super("org.musicbrainz.mobile", DashboardActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testDashboardNavigation() throws Exception {
        openCloseAbout();
        openCloseDonate();
        openCloseCollection();
        openCloseSettings();
        openCloseLogIn();
    }

    private void openCloseAbout() {
        solo.clickOnButton("About");
        solo.assertCurrentActivity("Expected About Activity", AboutActivity.class);
        solo.goBack();
    }

    private void openCloseDonate() {
        solo.clickOnButton("Donate");
        solo.assertCurrentActivity("Expected Donate Activity", DonateActivity.class);
        solo.goBack();
    }

    private void openCloseCollection() {
        solo.clickOnButton("Collection");
    }
    
    private void openCloseSettings() {
        // TODO
    }
    
    private void openCloseLogIn() {
        // TODO
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
