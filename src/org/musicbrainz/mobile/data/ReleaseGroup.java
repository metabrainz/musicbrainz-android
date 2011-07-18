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

package org.musicbrainz.mobile.data;

import org.musicbrainz.mobile.R;

import android.content.Context;
import android.content.res.Resources;

/**
 * Class to represent a release group with one or more releases. If the release
 * group contains a single release, the MBID of that release can be stored in
 * order to skip a browse request on the release group.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseGroup {

	private String mbid;
	
	private String artist;
	private String title;
	private String type;
	private int numberReleases;
	
	// MBID for release, if release group contains a single release.
	private String singleReleaseMbid;

	public String getMbid() {
		return mbid;
	}
	
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
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
	
	public String getFormattedType(Context context) {
		
		Resources res = context.getResources();
		if (type.equalsIgnoreCase("album"))
			return res.getString(R.string.rt_album);
		else if (type.equalsIgnoreCase("single"))
			return res.getString(R.string.rt_single);
		else if (type.equalsIgnoreCase("ep"))
			return res.getString(R.string.rt_ep);
		else if (type.equalsIgnoreCase("compilation"))
			return res.getString(R.string.rt_compilation);
		else if (type.equalsIgnoreCase("non-album tracks"))
			return res.getString(R.string.rt_nat);
		else if (type.equalsIgnoreCase("soundtrack"))
			return res.getString(R.string.rt_soundtrack);
		else if (type.equalsIgnoreCase("spokenword"))
			return res.getString(R.string.rt_spokenword);
		else if (type.equalsIgnoreCase("interview"))
			return res.getString(R.string.rt_interview);
		else if (type.equalsIgnoreCase("audiobook"))
			return res.getString(R.string.rt_audiobook);
		else if (type.equalsIgnoreCase("live"))
			return res.getString(R.string.rt_live);
		else if (type.equalsIgnoreCase("remix"))
			return res.getString(R.string.rt_remix);
		else if (type.equalsIgnoreCase("other"))
			return res.getString(R.string.rt_other);
		else
			return res.getString(R.string.rt_unknown);
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
	
}
