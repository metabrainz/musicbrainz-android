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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class to represent a release group with one or more releases. If the release
 * group contains a single release, the MBID of that release can be stored in
 * order to skip a browse request on the release group.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseGroup implements Comparable<Object> {

	private String mbid;
	private String singleReleaseMbid; // MBID for release, if release group contains a single release.
	
	private ArrayList<ReleaseArtist> artists = new ArrayList<ReleaseArtist>();
	private String title;
	private String type;
	private Calendar firstRelease = Calendar.getInstance();
	private int numberReleases;

	public String getMbid() {
		return mbid;
	}
	
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	
	public ArrayList<ReleaseArtist> getArtists() {
		return artists;
	}
	
	public String getFormattedArtist() {
		String formatted = "";
		for (ReleaseArtist artist : artists) {
			formatted += artist.getName() + ", ";
		}
		return formatted.substring(0, formatted.length()-2);
	}
	
	public void addArtist(ReleaseArtist artist) {
		artists.add(artist);
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
	
	public int getNumberReleases() {
		return numberReleases;
	}
	
	public void setNumberReleases(int numberReleases) {
		this.numberReleases = numberReleases;
	}
	
	public boolean isSingleRelease() {
		return numberReleases == 1;
	}
	
	public String getSingleReleaseMbid() {
		return singleReleaseMbid;
	}
	
	public void setSingleReleaseMbid(String singleReleaseMbid) {
		this.singleReleaseMbid = singleReleaseMbid;
	}
	
	public void setFirstRelease(String date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			firstRelease.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			formatNoDay(date);
		}
	}
	
	private void formatNoDay(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		try {
			firstRelease.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			formatNoMonth(date);
		}
	}
	
	private void formatNoMonth(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		try {
			firstRelease.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			firstRelease = null;
		}
	}
	
	public Calendar getFirstRelease() {
		return firstRelease;
	}
	
	public String getReleaseYear() {
		if (firstRelease == null) {
			return "--";
		} else {
			return "" + firstRelease.get(Calendar.YEAR);
		}
	}

	public int compareTo(Object another) {
		ReleaseGroup rg = (ReleaseGroup) another;
		if (this.getFirstRelease() == null && rg.getFirstRelease() == null) {
			return 0;
		} else if (this.getFirstRelease() == null) {
			return 1;
		} else if (rg.getFirstRelease() == null) {
			return -1;
		} else {
			return this.getFirstRelease().compareTo(rg.getFirstRelease());
		}
	}
	
}
