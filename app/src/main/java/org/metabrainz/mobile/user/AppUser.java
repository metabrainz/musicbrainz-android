package org.metabrainz.mobile.user;

import org.metabrainz.mobile.api.User;

public interface AppUser extends User {

    void setUsername(String username);

    boolean isLoggedIn();

    void clearData();

}
