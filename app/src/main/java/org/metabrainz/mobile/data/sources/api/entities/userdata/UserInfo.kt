package org.metabrainz.mobile.data.sources.api.entities.userdata;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("metabrainz_user_id")
    private String userId;
    private String profile;
    @SerializedName("sub")
    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
