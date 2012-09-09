package org.musicbrainz.android.api.util;

public class Credentials {

    private String userAgent;
    private String username;
    private String password;
    private String clientId;
    
    public Credentials(String userAgent, String username, String password, String clientId) {
        this.userAgent = userAgent;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
    }
    
    public String getUserAgent() {
        return userAgent;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }
    
}
