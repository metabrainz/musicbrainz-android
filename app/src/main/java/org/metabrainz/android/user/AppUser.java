package org.metabrainz.android.user;

import org.metabrainz.android.api.User;

public interface AppUser extends User {
    
    void setUsername(String username);
    
    void setPassword(String password);
    
    boolean isLoggedIn();
    
    void clearData();

}
