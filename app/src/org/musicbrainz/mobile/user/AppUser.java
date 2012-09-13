package org.musicbrainz.mobile.user;

import org.musicbrainz.android.api.User;

public interface AppUser extends User {
    
    void setUsername(String username);
    
    void setPassword(String password);
    
    boolean isLoggedIn();
    
    void clearData();

}
