/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Partial release group data.
 */
public class ReleaseGroupStub implements Comparable<ReleaseGroupStub> {

    private String mbid;
    private String title;
    private String type;
    private Calendar firstRelease = Calendar.getInstance();
    private LinkedList<ReleaseArtist> artists = new LinkedList<ReleaseArtist>();
    private LinkedList<String> releaseMbids = new LinkedList<String>();

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getFirstRelease() {
        return firstRelease;
    }

    public void setFirstRelease(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            firstRelease.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            formatWithoutDay(date);
        }
    }

    private void formatWithoutDay(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            firstRelease.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            formatWithoutMonth(date);
        }
    }

    private void formatWithoutMonth(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        try {
            firstRelease.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            firstRelease = null;
        }
    }

    public String getReleaseYear() {
        if (firstRelease == null) {
            return "--";
        } else {
            return "" + firstRelease.get(Calendar.YEAR);
        }
    }

    public LinkedList<ReleaseArtist> getArtists() {
        return artists;
    }

    public void addArtist(ReleaseArtist artist) {
        artists.add(artist);
    }

    public int getNumberOfReleases() {
        return releaseMbids.size();
    }

    public void addReleaseMbid(String mbid) {
        releaseMbids.add(mbid);
    }

    public LinkedList<String> getReleaseMbids() {
        return releaseMbids;
    }

    public int compareTo(ReleaseGroupStub another) {
        if (this.getFirstRelease() == null && another.getFirstRelease() == null) {
            return 0;
        } else if (this.getFirstRelease() == null) {
            return 1;
        } else if (another.getFirstRelease() == null) {
            return -1;
        } else {
            return this.getFirstRelease().compareTo(another.getFirstRelease());
        }
    }

}
