package org.musicbrainz.mobile.test.robotium.search;

import org.musicbrainz.mobile.ui.activity.ArtistActivity;
import org.musicbrainz.mobile.ui.activity.HomeActivity;
import org.musicbrainz.mobile.ui.activity.SearchActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class ArtistSearchTest extends ActivityInstrumentationTestCase2<HomeActivity> {
	
	private Solo solo;
	
	public ArtistSearchTest() {
		super("org.musicbrainz.mobile", HomeActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testSearchWithNoQuery() throws Exception {
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", HomeActivity.class);
	}
	
	public void testSearchWithExistingArtist() throws Exception {
		solo.enterText(0, "the beatles");
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", SearchActivity.class);
		assertTrue(solo.searchText("Artist"));
		solo.clickInList(0);
		solo.assertCurrentActivity("", ArtistActivity.class);
		assertTrue(solo.searchText("The Beatles"));
	}
	
	public void testSearchWithUnknownArtist() throws Exception {
		solo.enterText(0, "nbagroypdnebto");
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", SearchActivity.class);
		assertTrue(solo.searchText("No results found"));
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
