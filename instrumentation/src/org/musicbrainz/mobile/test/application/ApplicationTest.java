package org.musicbrainz.mobile.test.application;

import org.musicbrainz.mobile.App;

import static android.test.MoreAsserts.assertMatchesRegex;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.ApplicationTestCase;

public class ApplicationTest extends ApplicationTestCase<App> {
    
    private static final String EXPECTED_VERSION_FORMAT = "\\d\\.\\d\\.\\d";
    private static final String EXPECTED_USER_AGENT_FORMAT = "MBAndroid/\\d\\.\\d\\.\\d";
    private static final String EXPECTED_CLIENT_ID_FORMAT = "musicbrainz.android-\\d\\.\\d\\.\\d";
    
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
    
    public void testPreconditions() {
        assertNotNull(application);
    }

    public void testCorrectVersionNameFormat() throws NameNotFoundException {
        PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);

        assertMatchesRegex(EXPECTED_VERSION_FORMAT, info.versionName);
    }
    
    public void testGetVersion() {
        assertMatchesRegex(EXPECTED_VERSION_FORMAT, App.getVersion());
    }
    
    public void testRobotoLightLoaded() {
        assertNotNull(App.getRobotoLight());
    }
    
    public void testApplicationInstanceExists() {
        assertNotNull(App.getContext());
    }
    
    public void testUserExists() {
        assertNotNull(App.getUser());
    }
    
    public void testValidUserAgent() {
        assertMatchesRegex(EXPECTED_USER_AGENT_FORMAT, App.getUserAgent());
    }
    
    public void testValidClientId() {
        assertMatchesRegex(EXPECTED_CLIENT_ID_FORMAT, App.getClientId());
    }

}
