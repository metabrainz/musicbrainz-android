package org.metabrainz.mobile.api.data.obsolete;

/**
 * Record label info for search results.
 */
public class LabelSearchResult {
	
	private String mbid;
	private String name;
	private String country;
	
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

}
