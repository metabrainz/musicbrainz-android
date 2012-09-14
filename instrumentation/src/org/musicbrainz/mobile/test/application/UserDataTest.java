package org.musicbrainz.mobile.test.application;

import org.musicbrainz.mobile.user.UserPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class UserDataTest extends AndroidTestCase {
    
    private UserPreferences user;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setContext(new RenamingContext(getContext()));
        user = new UserPreferences();
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull(user);
    }
    
    @SmallTest
    public void testUsernameStored() {
        user.setUsername("jdamcd");
        
        assertEquals("jdamcd", user.getUsername());
    }
    
    @SmallTest
    public void testUserPasswordNotStoredAsPlainText() {
        user.setPassword("secret");
        
        assertTrue(!"secret".equals(getPasswordAsStored()));
    }
    
    @SmallTest
    public void testPasswordRetrievable() {
        user.setPassword("secret");
        
        assertEquals("secret", user.getPassword());
    }
    
    @SmallTest
    public void testUserWithoutNameNotLoggedIn() {
        user.clearData();
        
        assertTrue(!user.isLoggedIn());
    }
    
    @SmallTest
    public void testUserWithNameLoggedIn() {
        user.setUsername("jdamcd");
        assertTrue(user.isLoggedIn());
    }
    
    @SmallTest
    public void testCrashReportsOnByDefault() {
        assertTrue(user.isCrashReportingEnabled());
    }
    
    @SmallTest
    public void testSearchSuggestionsOnByDefault() {
        assertTrue(user.isSearchSuggestionsEnabled());
    }
    
    @SmallTest
    public void testClearUser() {
        user.setUsername("jdamcd");
        user.setPassword("secret");
        
        user.clearData();
        
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    private String getPasswordAsStored() {
        SharedPreferences prefs = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return prefs.getString("password", null);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        user.clearData();
        clearPreferences();
    }
    
    private void clearPreferences() {
        getContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit();
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().commit();
    }

}
