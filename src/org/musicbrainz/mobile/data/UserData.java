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

import java.util.LinkedList;

import org.musicbrainz.mobile.ui.util.StringFormat;

/**
 * Class to holds user tags and rating data for a MusicBrainz entity.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class UserData {
	
	private LinkedList<String> tags;
	private int rating;
	
	public UserData() {
		tags = new LinkedList<String>();
	}

	public LinkedList<String> getTags() {
		return tags;
	}
	
	public String getTagString() {
		return StringFormat.commaSeparate(tags);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
