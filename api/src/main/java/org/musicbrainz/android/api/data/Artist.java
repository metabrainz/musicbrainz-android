/*
 * Copyright (C) 2010 Jamie McDonald
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

public class Artist {

    private String mbid;
    private String name;
    private String type;
    private String country;
    private String start;
    private String end;

    private int ratingCount;
    private float rating;

    private LinkedList<Tag> tags = new LinkedList<Tag>();
    private ArrayList<WebLink> links = new ArrayList<WebLink>();

    private ArrayList<ReleaseGroupStub> releaseGroupStubs = new ArrayList<ReleaseGroupStub>();

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStart() {
        return start;
    }

    public void setBegin(String begin) {
        this.start = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public ArrayList<ReleaseGroupStub> getReleaseGroups() {
        return releaseGroupStubs;
    }

    public void setLinks(ArrayList<WebLink> links) {
        this.links = links;
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

    public ArrayList<ReleaseGroupStub> getReleases() {
        Collections.sort(releaseGroupStubs);
        return releaseGroupStubs;
    }

    public void setReleaseGroups(ArrayList<ReleaseGroupStub> releases) {
        this.releaseGroupStubs = releases;
    }

    public ArrayList<WebLink> getLinks() {
        Collections.sort(links);
        return links;
    }

    public void addLink(WebLink link) {
        links.add(link);
    }

    /*
     * Special purpose artists, which can often be ignored.
     */
    public static final String[] SPECIAL_PURPOSE = {

    "89ad4ac3-39f7-470e-963a-56509c546377", // Various artists
            "f731ccc4-e22a-43af-a747-64213329e088", // Anonymous
            "33cf029c-63b0-41a0-9855-be2a3665fb3b", // Data
            "314e1c25-dde7-4e4d-b2f4-0a7b9f7c56dc", // Dialogue
            "eec63d3c-3b81-4ad4-b1e4-7c147d4d2b61", // No artist
            "125ec42a-7229-4250-afc5-e057484327fe", // Unknown
            "0187fe48-c87d-4dd8-beca-9c07ef535603", // Christmas music
            "9e44f539-f3fc-4120-bce2-94c8716437fa", // Classical music
            "66ea0139-149f-4a0c-8fbf-5ea9ec4a6e49", // Disney
            "a0ef7e1d-44ff-4039-9435-7d5fefdeecc9", // Musical theater
            "ae636985-40e8-4010-ae02-0f35930f8017", // Religious music
            "d6bd72bc-b1e2-4525-92aa-0f853cbb41bf", // Soundtrack
            "90068d37-bae7-4292-be4a-704c145bd616", // Church chimes
            "80a8851f-444c-4539-892b-ad2a49292aa9", // Language instruction
            "51118c9d-965d-4f9f-89a1-0091837ccf54", // Nature sounds
            "49e713ce-c3be-4697-8983-ee7cd0a11ea1" // News report

    };

}
