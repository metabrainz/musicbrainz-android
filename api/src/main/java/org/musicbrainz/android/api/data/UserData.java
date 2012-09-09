package org.musicbrainz.android.api.data;

import java.util.LinkedList;

/**
 * User tags and rating data for a MusicBrainz entity.
 */
public class UserData {

    private LinkedList<String> tags = new LinkedList<String>();
    private float rating;

    public LinkedList<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
