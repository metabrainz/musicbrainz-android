package org.metabrainz.mobile.user;

import org.metabrainz.mobile.api.User;

public interface AppUser extends User {

    void setUsername(String username);

    void setPassword(String password);

    boolean isLoggedIn();

    void clearData();

}
