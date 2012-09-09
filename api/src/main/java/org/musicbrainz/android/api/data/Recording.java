package org.musicbrainz.android.api.data;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Recording (track) data.
 */
public class Recording {
	
	private String mbid;
	private String title;
	private int length;
	private ArrayList<ArtistNameMbid> artists = new ArrayList<ArtistNameMbid>();

	private int ratingCount;
	private float rating;
	private LinkedList<Tag> tags = new LinkedList<Tag>();
	
	private ArrayList<ReleaseStub> releases = new ArrayList<ReleaseStub>();

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

    public ArrayList<ArtistNameMbid> getArtists() {
        return artists;
    }

    public void addArtist(ArtistNameMbid artist) {
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

	public ArrayList<ReleaseStub> getReleases() {
		return releases;
	}

	public void setReleases(ArrayList<ReleaseStub> releases) {
		this.releases = releases;
	}

}

