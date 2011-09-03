package org.musicbrainz.android.api.ws;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.musicbrainz.android.api.WebserviceConfig;
import org.musicbrainz.android.api.ws.WebService.MBEntity;

public class QueryBuilder {

	public static final String WEB_SERVICE = WebserviceConfig.WEB_SERVICE;
	
	private static final String LOOKUP_ARTIST = "artist/";
	private static final String LOOKUP_ARTIST_PARAMS = "?inc=url-rels+tags+ratings";
	private static final String LOOKUP_RELEASE = "release/";
	private static final String LOOKUP_RELEASE_PARAMS = "?inc=release-groups+artists+recordings+labels+tags+ratings";
	
	private static final String BROWSE_ARTIST_RGS = "release-group?artist=";
	private static final String BROWSE_ARTIST_RGS_PARAMS = "&limit=100";
	private static final String BROWSE_RG_RELEASES = "release?release-group=";
	private static final String BROWSE_RG_RELEASES_PARAMS = "&inc=artist-credits+labels+mediums";
	
	private static final String SEARCH_ARTIST = "artist?query=";
	private static final String SEARCH_RG = "release-group?query=";
	private static final String SEARCH_RELEASE = "release?query=";
	private static final String SEARCH_BARCODE = "release/?query=barcode:";
	private static final String SEARCH_BARCODE_PARAMS = "&limit=1";
	
	private static final String TAG_PARAMS = "?inc=tags";
	private static final String RATING_PARAMS = "?inc=ratings";
	
	public static URL barcodeLookup(String barcode) throws MalformedURLException {
		return new URL(WEB_SERVICE + SEARCH_BARCODE + barcode + SEARCH_BARCODE_PARAMS);
	}
	
	public static URL releaseLookup(String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + LOOKUP_RELEASE + mbid + LOOKUP_RELEASE_PARAMS);
	}

	public static URL releaseGroupReleaseBrowse(String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + BROWSE_RG_RELEASES + mbid + BROWSE_RG_RELEASES_PARAMS);
	}
	
	public static URL artistLookup(String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + LOOKUP_ARTIST + mbid + LOOKUP_ARTIST_PARAMS);
	}
	
	public static URL artistReleaseGroupBrowse(String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + BROWSE_ARTIST_RGS + mbid + BROWSE_ARTIST_RGS_PARAMS);
	}
	
	public static URL artistSearch(String searchTerm) throws MalformedURLException {
		return new URL(WEB_SERVICE + SEARCH_ARTIST + sanitise(searchTerm));
	}
	
	public static URL releaseGroupSearch(String searchTerm) throws MalformedURLException {
		return new URL(WEB_SERVICE + SEARCH_RG + sanitise(searchTerm));
	}
	
	public static URL releaseSearch(String searchTerm) throws MalformedURLException {
		return new URL(WEB_SERVICE + SEARCH_RELEASE + sanitise(searchTerm));
	}
	
	public static URL tagLookup(MBEntity type, String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + entityString(type) + "/" + mbid + TAG_PARAMS);
	}
	
	public static URL ratingLookup(MBEntity type, String mbid) throws MalformedURLException {
		return new URL(WEB_SERVICE + entityString(type) + "/" + mbid + RATING_PARAMS);
	}
	
	private static String sanitise(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return input;
		}
	}
	
	private static String entityString(MBEntity entity) {
		switch(entity) {
		case ARTIST:
			return "artist";
		case RELEASE_GROUP:
			return "release-group";
		default:
			return "artist";
		}
	}
	
}
