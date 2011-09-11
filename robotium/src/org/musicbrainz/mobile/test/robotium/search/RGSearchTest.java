package org.musicbrainz.mobile.test.robotium.search;

import org.musicbrainz.mobile.ui.activity.HomeActivity;
import org.musicbrainz.mobile.ui.activity.ReleaseActivity;
import org.musicbrainz.mobile.ui.activity.SearchActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class RGSearchTest extends ActivityInstrumentationTestCase2<HomeActivity> {
	
	private Solo solo;
	
	public RGSearchTest() {
		super("org.musicbrainz.mobile", HomeActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testSearchWithNoQuery() throws Exception {
		solo.pressSpinnerItem(0, 1);
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", HomeActivity.class);
	}
	
	public void testSearchWithExistingRG() throws Exception {
		solo.pressSpinnerItem(0, 1);
		solo.enterText(0, "codes and keys");
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", SearchActivity.class);
		assertTrue(solo.searchText("Release"));
		solo.clickInList(0);
		solo.assertCurrentActivity("", ReleaseActivity.class);
		solo.clickInList(0);
		assertTrue(solo.searchText("Death Cab for Cutie"));
	}
	
	public void testSearchWithUnknownRG() throws Exception {
		solo.pressSpinnerItem(0, 1);
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