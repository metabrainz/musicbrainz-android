package org.metabrainz.mobile.data.sources.api.entities.mbentity;

import com.google.gson.annotations.SerializedName;

import org.metabrainz.mobile.data.sources.api.entities.userdata.Rating;
import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserRating;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserTag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MBEntity implements Serializable {

    @SerializedName("id")
    String mbid;
    String disambiguation;

    @SerializedName("user-rating")
    private UserRating userRating;
    private Rating rating;
    @SerializedName("user-tags")
    private List<UserTag> userTags = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    @SerializedName("user-genres")
    private List<UserTag> userGenres = new ArrayList<>();
    private List<Tag> genres = new ArrayList<>();

    public UserRating getUserRating() {
        return userRating;
    }

    public void setUserRating(UserRating userRating) {
        this.userRating = userRating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<UserTag> getUserGenres() {
        return userGenres;
    }

    public void setUserGenres(List<UserTag> userGenres) {
        this.userGenres = userGenres;
    }

    public List<Tag> getGenres() {
        return genres;
    }

    public void setGenres(List<Tag> genres) {
        this.genres = genres;
    }

    public List<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(List<UserTag> userTags) {
        this.userTags = userTags;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MBEntity)) return false;
        MBEntity mbEntity = (MBEntity) o;
        return getMbid().equals(mbEntity.getMbid());
    }

    @Override
    public int hashCode() {
        return getMbid().hashCode();
    }
}
