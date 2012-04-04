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
import java.util.Collection;
import java.util.LinkedList;

/**
 * Partial release data to differentiate between similar releases (e.g. part of
 * the same release group).
 */
public class ReleaseStub {

    private String releaseMbid;

    private String title;
    private ArrayList<ArtistNameMbid> artists = new ArrayList<ArtistNameMbid>();
    private String date;
    private int tracksNum;
    private String countryCode;
    private Collection<String> labels = new LinkedList<String>();
    private Collection<String> formats = new LinkedList<String>();

    public String getReleaseMbid() {
        return releaseMbid;
    }

    public void setReleaseMbid(String releaseMbid) {
        this.releaseMbid = releaseMbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ArtistNameMbid> getArtists() {
        return artists;
    }

    public void addArtist(ArtistNameMbid artist) {
        artists.add(artist);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getTracksNum() {
        return tracksNum;
    }

    public void setTracksNum(int tracks) {
        this.tracksNum = tracks;
    }

    public Collection<String> getLabels() {
        return labels;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public Collection<String> getFormats() {
        return formats;
    }

    public void addFormat(String format) {
        formats.add(format);
    }

}
