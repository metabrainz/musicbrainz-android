package org.metabrainz.mobile.api.data.obsolete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ReleaseGroup extends ReleaseGroupSearchResult {
    
    private int ratingCount;
    private float rating;
    
    private LinkedList<Tag> tags = new LinkedList<Tag>();
    private ArrayList<WebLink> links = new ArrayList<WebLink>();
    
    private ArrayList<ReleaseSearchResult> releases = new ArrayList<ReleaseSearchResult>();
    
    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public LinkedList<Tag> getTags() {
        Collections.sort(tags);
        return tags;
    }
    
    public void setTags(LinkedList<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public ArrayList<WebLink> getLinks() {
        Collections.sort(links);
        return links;
    }

    public void addLink(WebLink link) {
        links.add(link);
    }

    public void setLinks(ArrayList<WebLink> links) {
        this.links = links;
    }

    public ArrayList<ReleaseSearchResult> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<ReleaseSearchResult> releases) {
        this.releases = releases;
    }
    
    public void addRelease(ReleaseSearchResult release) {
        releases.add(release);
    }

}
