package org.musicbrainz.android.api.data;

public class RecordingStub {
	
	private String mbid;
	private String title;
	private ArtistNameMbid artist;
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
	
	public ArtistNameMbid getArtist() {
		return artist;
	}
	
	public void setArtist(ArtistNameMbid artist) {
		this.artist = artist;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

}
