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

package org.musicbrainz.mobile.test.robotium.search;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ArtistActivity;
import org.musicbrainz.mobile.activity.DashboardActivity;
import org.musicbrainz.mobile.activity.SearchActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;

public class ArtistSearchTest extends ActivityInstrumentationTestCase2<DashboardActivity> {

    private Solo solo;

    public ArtistSearchTest() {
        super("org.musicbrainz.mobile", DashboardActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Smoke
    public void testSearchWithNoQuery() throws Exception {
        clickSearch();
        solo.assertCurrentActivity("", DashboardActivity.class);
    }

    @Smoke
    public void testSearchWithExistingArtist() throws Exception {
        solo.enterText(0, "the beatles");
        clickSearch();
        solo.assertCurrentActivity("", SearchActivity.class);
        assertTrue(solo.searchText("Artist"));
        solo.clickInList(0);
        solo.assertCurrentActivity("", ArtistActivity.class);
        assertTrue(solo.searchText("The Beatles"));
    }

    @Smoke
    public void testSearchWithUnknownArtist() throws Exception {
        solo.enterText(0, "nbagroypdnebto");
        clickSearch();
        solo.assertCurrentActivity("", SearchActivity.class);
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
