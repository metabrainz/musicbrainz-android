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

    public void addTag(Tag tag) {
        tags.add(tag);
    }
	
	public void setTags(LinkedList<Tag> tags) {
		this.tags = tags;
	}

	public ArrayList<WebLink> getLinks() {
		return links;
	}

    public void addLink(WebLink link) {
        links.add(link);
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

