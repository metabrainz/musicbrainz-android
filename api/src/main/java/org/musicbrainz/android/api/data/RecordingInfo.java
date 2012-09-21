package org.musicbrainz.android.api.data;

public class RecordingInfo {
	
	private String mbid;
	private String title;
	private ReleaseArtist artist;
	private int length;
	
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
	
	public ReleaseArtist getArtist() {
		return artist;
	}
	
	public void setArtist(ReleaseArtist artist) {
		this.artist = artist;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

}
