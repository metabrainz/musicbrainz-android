package org.metabrainz.android.api.data;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Recording (track) data.
 */
public class Recording {
	
	private String mbid;
	private String title;
	private int length;
	private ArrayList<ReleaseArtist> artists = new ArrayList<ReleaseArtist>();

	private int ratingCount;
	private float rating;
	private LinkedList<Tag> tags = new LinkedList<Tag>();
	
	private ArrayList<ReleaseSearchResult> releases = new ArrayList<ReleaseSearchResult>();

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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

    public ArrayList<ReleaseArtist> getArtists() {
        return artists;
    }

    public void addArtist(ReleaseArtist artist) {
        artists.add(artist);
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
	
    public void addTag(Tag tag) {
        tags.add(tag);
    }

	public void setTags(LinkedList<Tag> tags) {
		this.tags = tags;
	}

	public ArrayList<ReleaseSearchResult> getReleases() {
		return releases;
	}

	public void setReleases(ArrayList<ReleaseSearchResult> releases) {
		this.releases = releases;
	}

}

