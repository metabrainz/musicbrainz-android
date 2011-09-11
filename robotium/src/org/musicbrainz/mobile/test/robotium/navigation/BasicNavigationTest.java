package org.musicbrainz.mobile.test.robotium.navigation;

import org.musicbrainz.mobile.ui.activity.AboutActivity;
import org.musicbrainz.mobile.ui.activity.DonateActivity;
import org.musicbrainz.mobile.ui.activity.HomeActivity;
import org.musicbrainz.mobile.ui.activity.LoginActivity;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class BasicNavigationTest extends ActivityInstrumentationTestCase2<HomeActivity> {

	private Solo solo;
	
	public BasicNavigationTest() {
		super("org.musicbrainz.mobile", HomeActivity.class);
	}
	
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
		goHomeViaActionBar();
	}
	
	private void openCloseDonateActivity() {
		solo.clickOnButton("Donate");
		solo.assertCurrentActivity("", DonateActivity.class);
		goHomeViaActionBar();
	}
	
	private void openCloseLoginActivity() {
		solo.clickOnButton("Log In");
		solo.assertCurrentActivity("", LoginActivity.class);
		goHomeViaActionBar();
	}
	
	private void goHomeViaActionBar() {
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("", HomeActivity.class);
	}
	
	public void testMenuNavigation() throws Exception {
		visitLoginMenuItems();
		visitAboutMenuItems();
		visitDonateMenuItems();
	}

	private void visitLoginMenuItems() {
		solo.clickOnButton("Log In");
		solo.pressMenuItem(0);
		solo.assertCurrentActivity("", AboutActivity.class);
		solo.goBack();
		solo.pressMenuItem(1);
		solo.assertCurrentActivity("", DonateActivity.class);
		goHomeViaActionBar();
	}
	
	private void visitAboutMenuItems() {
		solo.clickOnButton("About");
		solo.pressMenuItem(2);
		solo.assertCurrentActivity("", DonateActivity.class);
		goHomeViaActionBar();
	}
	
	private void visitDonateMenuItems() {
		solo.clickOnButton("Donate");
		solo.pressMenuItem(0);
		solo.assertCurrentActivity("", AboutActivity.class);
		solo.goBack();
		solo.goBack();
		solo.assertCurrentActivity("", HomeActivity.class);
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
