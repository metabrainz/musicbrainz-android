/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ReleaseGroup extends ReleaseGroupStub {
    
    private int ratingCount;
    private float rating;
    
    private LinkedList<Tag> tags = new LinkedList<Tag>();
    private ArrayList<WebLink> links = new ArrayList<WebLink>();
    
    private ArrayList<ReleaseStub> releases = new ArrayList<ReleaseStub>();
    
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

    public ArrayList<ReleaseStub> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<ReleaseStub> releases) {
        this.releases = releases;
    }
    
    public void addRelease(ReleaseStub release) {
        releases.add(release);
    }

}
