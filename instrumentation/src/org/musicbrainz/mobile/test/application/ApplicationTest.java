package org.musicbrainz.mobile.test.application;

import org.musicbrainz.mobile.App;

import static android.test.MoreAsserts.assertMatchesRegex;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class ApplicationTest extends ApplicationTestCase<App> {
    
    private static final String EXPECTED_VERSION_FORMAT = "\\d\\.\\d\\.\\d";
    private static final String EXPECTED_USER_AGENT_FORMAT = "MBAndroid/" + EXPECTED_VERSION_FORMAT;
    private static final String EXPECTED_CLIENT_ID_FORMAT = "musicbrainz.android-" + EXPECTED_VERSION_FORMAT;
    
    private App application;

    public ApplicationTest() {
        super(App.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }
    
    @SmallTest
    public void testPreconditions() {
        assertNotNull(application);
    }

    @SmallTest
    public void testCorrectVersionNameFormat() throws NameNotFoundException {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);

        assertMatchesRegex(EXPECTED_VERSION_FORMAT, info.versionName);
    }
    
    @SmallTest
    public void testGetVersion() {
        assertMatchesRegex(EXPECTED_VERSION_FORMAT, App.getVersion());
    }
    
    @SmallTest
    public void testRobotoLightLoaded() {
        assertNotNull(App.getRobotoLight());
    }
    
    @SmallTest
    public void testApplicationInstanceExists() {
        assertNotNull(App.getContext());
    }
    
    @SmallTest
    public void testUserExists() {
        assertNotNull(App.getUser());
    }
    
    @SmallTest
    public void testValidUserAgent() {
        assertMatchesRegex(EXPECTED_USER_AGENT_FORMAT, App.getUserAgent());
    }
    
    @SmallTest
    public void testValidClientId() {
        assertMatchesRegex(EXPECTED_CLIENT_ID_FORMAT, App.getClientId());
    }

}
