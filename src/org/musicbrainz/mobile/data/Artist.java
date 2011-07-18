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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.musicbrainz.mobile.ui.util.StringFormat;

/**
 * Class to hold information retrieved about an artist.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class Artist {
	
	private String mbid;
	
	private String name;
	private float rating;
	private Collection<String> tags;
	private ArrayList<ReleaseGroup> releases;
	private ArrayList<WebLink> links;
	
	public Artist() {
		tags = new LinkedList<String>();
		releases = new ArrayList<ReleaseGroup>();
		links = new ArrayList<WebLink>();
	}
	
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
	
	public void setTags(Collection<String> folkTags) {
		this.tags = folkTags;
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public ArrayList<ReleaseGroup> getReleases() {
		return releases;
	}
	
	public void setReleases(ArrayList<ReleaseGroup> releases) {
		this.releases = releases;
	}
	
	public void addRelease(ReleaseGroup release) {
		releases.add(release);
	}
	
	public ArrayList<WebLink> getLinks() {
		Collections.sort(links);
		return links;
	}
	
	public void addLink(WebLink link) {
		links.add(link);
	}
	
	/*
	 * Special purpose artists which can be ignored.
	 */
	public static final String[] IGNORE_LIST = {
		
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
		"49e713ce-c3be-4697-8983-ee7cd0a11ea1"  // News report
		
		};
	
}
