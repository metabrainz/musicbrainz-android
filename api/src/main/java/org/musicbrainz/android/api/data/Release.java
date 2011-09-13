/*
 * Copyright (C) 2010 Jamie McDonald
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.musicbrainz.android.api.util.StringFormat;

/**
 * Full release data.
 */
public class Release {
	
	private String releaseMbid;
	private String releaseGroupMbid;
	private String barcode;
	private ArrayList<ReleaseArtist> artists;
	
	private String title;
	private String date;
	private float rating;
	private Collection<String> tags;
	private Collection<String> labels;
	private ArrayList<Track> tracks;
	
	public Release() {
		artists = new ArrayList<ReleaseArtist>();
		labels = new LinkedList<String>();
		tags = new LinkedList<String>();
		tracks = new ArrayList<Track>();
	}
	
	public String getReleaseMbid() {
		return releaseMbid;
	}

	public void setReleaseMbid(String releaseMbid) {
		this.releaseMbid = releaseMbid;
	}
	
	public String getReleaseGroupMbid() {
		return releaseGroupMbid;
	}
	
	public void setReleaseGroupMbid(String releaseGroupMbid) {
		this.releaseGroupMbid = releaseGroupMbid;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public ArrayList<ReleaseArtist> getArtists() {
		return artists;
	}
	
	public void addArtist(ReleaseArtist artist) {
		artists.add(artist);
	}
	
	public String getFormattedArtist() {
		String formatted = "";
		for (ReleaseArtist artist : artists) {
			formatted += artist.getName() + ", ";
		}
		return formatted.substring(0, formatted.length()-2);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public float getRating() {
		return rating;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public Collection<String> getTagList() {
		return tags;
	}
	
	public String getTags() {
		return StringFormat.commaSeparate(tags);
	}
	
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public Collection<String> getLabels() {
		return labels;
	}
	
	public String getFormattedLabels() {
		return StringFormat.commaSeparate(labels);
	}
	
	public void setLabels(LinkedList<String> labels) {
		this.labels = labels;
	}
	
	public void addLabel(String label) {
		labels.add(label);
	}
	
	public ArrayList<Track> getTrackList() {
		return tracks;
	}
	
	public void setTrackList(ArrayList<Track> trackList) {
		this.tracks = trackList;
	}
	
	public void addTrack(Track track) {
		tracks.add(track);
	}

}
