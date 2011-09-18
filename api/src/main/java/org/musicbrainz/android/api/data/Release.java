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
	private String status;
	private String date;
	
	private float releaseGroupRating;
	private int releaseGroupRatingCount;
	private Collection<String> releaseGroupTags;
	
	private Collection<String> labels;
	private ArrayList<RecordingStub> tracks;
	
	public Release() {
		artists = new ArrayList<ReleaseArtist>();
		labels = new LinkedList<String>();
		releaseGroupTags = new LinkedList<String>();
		tracks = new ArrayList<RecordingStub>();
	}
	
	public String getMbid() {
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
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public float getReleaseGroupRating() {
		return releaseGroupRating;
	}
	
	public void setReleaseGroupRating(float releaseGroupRating) {
		this.releaseGroupRating = releaseGroupRating;
	}
	
	public int getReleaseGroupRatingCount() {
		return releaseGroupRatingCount;
	}
	
	public void setReleaseGroupRatingCount(int releaseGroupRatingCount) {
		this.releaseGroupRatingCount = releaseGroupRatingCount;
	}
	
	public Collection<String> getReleaseGroupTagList() {
		return releaseGroupTags;
	}
	
	public String getReleaseGroupTags() {
		return StringFormat.commaSeparate(releaseGroupTags);
	}
	
	public void setReleaseGroupTags(Collection<String> tags) {
		this.releaseGroupTags = tags;
	}
	
	public void addReleaseGroupTag(String tag) {
		releaseGroupTags.add(tag);
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
	
	public ArrayList<RecordingStub> getTrackList() {
		return tracks;
	}
	
	public void setTrackList(ArrayList<RecordingStub> trackList) {
		this.tracks = trackList;
	}
	
	public void addTrack(RecordingStub track) {
		tracks.add(track);
	}

}
