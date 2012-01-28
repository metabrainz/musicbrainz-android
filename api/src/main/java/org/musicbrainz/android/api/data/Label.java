/*
 * Copyright (C) 2012 Jamie McDonald
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
import java.util.LinkedList;

/**
 * Record label data.
 */
public class Label {

	private String mbid;
	private String name;
	private String country;
	private String start;
	private String end;

	private int ratingCount;
	private float rating;

	private LinkedList<Tag> tags = new LinkedList<Tag>();
	private ArrayList<WebLink> links = new ArrayList<WebLink>();

	private ArrayList<ReleaseStub> releaseStubs = new ArrayList<ReleaseStub>();

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
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

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public LinkedList<Tag> getTags() {
		return tags;
	}

	public void setTags(LinkedList<Tag> tags) {
		this.tags = tags;
	}

	public ArrayList<WebLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<WebLink> links) {
		this.links = links;
	}

	public ArrayList<ReleaseStub> getReleaseStubs() {
		return releaseStubs;
	}

	public void setReleaseStubs(ArrayList<ReleaseStub> releaseStubs) {
		this.releaseStubs = releaseStubs;
	}

}

