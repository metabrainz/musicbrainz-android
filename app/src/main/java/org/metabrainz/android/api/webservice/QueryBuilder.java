package org.metabrainz.android.api.webservice;

import org.metabrainz.android.api.util.WebServiceUtils;

public class QueryBuilder {

    private static final String WEB_SERVICE = "http://metabrainz.org/ws/2/";

    private static final String LOOKUP_ARTIST = "artist/";
    private static final String LOOKUP_ARTIST_PARAMS = "?inc=url-rels+artist-rels+label-rels+tags+ratings";
    private static final String LOOKUP_RELEASE = "release/";
    private static final String LOOKUP_RELEASE_PARAMS = "?inc=release-groups+artists+recordings+labels+tags+ratings+url-rels+artist-rels";
    private static final String LOOKUP_LABEL = "label/";
    private static final String LOOKUP_LABEL_PARAMS = "?inc=tags+ratings+url-rels";
    private static final String LOOKUP_RECORDING = "recording/";
    private static final String LOOKUP_RECORDING_PARAMS = "?inc=artists+tags+ratings";
    private static final String LOOKUP_RELEASE_GROUP = "release-group/";
    private static final String LOOKUP_RELEASE_GROUP_PARAMS = "?inc=artist-credits+tags+ratings+url-rels";

    private static final String BROWSE_ARTIST_RGS = "release-group?artist=";
    private static final String BROWSE_ARTIST_RGS_PARAMS = "&limit=100";
    private static final String BROWSE_RG_RELEASES = "release?release-group=";
    private static final String BROWSE_RG_RELEASES_PARAMS = "&inc=artist-credits+labels+mediums&limit=100";
    private static final String BROWSE_OFFSET = "&offset=";

    private static final String SEARCH_ARTIST = "artist?query=";
    private static final String SEARCH_RG = "release-group?query=";
    private static final String SEARCH_RELEASE = "release?query=";
    private static final String SEARCH_BARCODE = "release/?query=barcode:";
    private static final String SEARCH_BARCODE_PARAMS = "&limit=1";
    private static final String SEARCH_LABEL = "label?query=";
    private static final String SEARCH_RECORDING = "recording?query=";
    
    private static final String LOOKUP_USER_COLLECTIONS = "collection?limit=100";
    private static final String COLLECTION = "collection/";
    private static final String COLLECTION_LIST = "/releases?inc=releases+artist-credits&limit=100";
    private static final String COLLECTION_EDIT = "/releases/";;

    private static final String TAG_PARAMS = "?inc=tags";
    private static final String RATING_PARAMS = "?inc=ratings";

    // MBID for Various Artists always exists.
    private static final String AUTH_TEST = "artist/89ad4ac3-39f7-470e-963a-56509c546377?inc=user-tags";
    private static final String CLIENT = "?client=";

    private static final String USER_PARAMS = "?inc=user-tags+user-ratings";
    private static final String TAG = "tag";
    private static final String RATING = "rating";
    private static final String BARCODE = "release/";

    public static String barcodeSearch(String barcode) {
        return buildQuery(SEARCH_BARCODE + barcode + SEARCH_BARCODE_PARAMS);
    }

    public static String releaseLookup(String mbid) {
        return buildQuery(LOOKUP_RELEASE + mbid + LOOKUP_RELEASE_PARAMS);
    }

    public static String releaseGroupReleaseBrowse(String mbid) {
        return buildQuery(BROWSE_RG_RELEASES + mbid + BROWSE_RG_RELEASES_PARAMS);
    }

    public static String artistLookup(String mbid) {
        return buildQuery(LOOKUP_ARTIST + mbid + LOOKUP_ARTIST_PARAMS);
    }

    public static String artistReleaseGroupBrowse(String mbid, int offset) {
        return buildQuery(BROWSE_ARTIST_RGS + mbid + BROWSE_ARTIST_RGS_PARAMS + BROWSE_OFFSET + offset);
    }

    public static String artistSearch(String searchTerm) {
        return buildQuery(SEARCH_ARTIST + WebServiceUtils.sanitise(searchTerm));
    }

    public static String releaseGroupSearch(String searchTerm) {
        return buildQuery(SEARCH_RG + WebServiceUtils.sanitise(searchTerm));
    }

    public static String releaseSearch(String searchTerm) {
        return buildQuery(SEARCH_RELEASE + WebServiceUtils.sanitise(searchTerm));
    }
    
    public static String releaseGroupLookup(String mbid) {
        return buildQuery(LOOKUP_RELEASE_GROUP + mbid + LOOKUP_RELEASE_GROUP_PARAMS);
    }
    
    public static String labelLookup(String mbid) {
        return buildQuery(LOOKUP_LABEL + mbid + LOOKUP_LABEL_PARAMS);
    }
    
    public static String labelSearch(String searchTerm) {
        return buildQuery(SEARCH_LABEL + WebServiceUtils.sanitise(searchTerm));
    }
    
    public static String recordingLookup(String mbid) {
        return buildQuery(LOOKUP_RECORDING + mbid + LOOKUP_RECORDING_PARAMS);
    }
    
    public static String recordingSearch(String searchTerm) {
        return buildQuery(SEARCH_RECORDING + searchTerm);
    }

    public static String tagLookup(Entity type, String mbid) {
        return buildQuery(WebServiceUtils.entityString(type) + "/" + mbid + TAG_PARAMS);
    }

    public static String ratingLookup(Entity type, String mbid) {
        return buildQuery(WebServiceUtils.entityString(type) + "/" + mbid + RATING_PARAMS);
    }

    public static String authenticationCheck() {
        return buildQuery(AUTH_TEST);
    }

    public static String userData(Entity entity, String mbid) {
        return buildQuery(WebServiceUtils.entityString(entity) + "/" + mbid + USER_PARAMS);
    }

    public static String tagSubmission(String clientId) {
        return buildQuery(TAG + CLIENT + clientId);
    }

    public static String ratingSubmission(String clientId) {
        return buildQuery(RATING + CLIENT + clientId);
    }

    public static String barcodeSubmission(String clientId) {
        return buildQuery(BARCODE + CLIENT + clientId);
    }
    
    public static String collectionList() {
        return buildQuery(LOOKUP_USER_COLLECTIONS);
    }
    
    public static String collectionLookup(String collectionMbid) {
        return buildQuery(COLLECTION + collectionMbid + COLLECTION_LIST);
    }
    
    public static String collectionEdit(String collectionMbid, String releaseMbid, String clientId) {
        return buildQuery(COLLECTION + collectionMbid + COLLECTION_EDIT + releaseMbid + ";" + CLIENT + clientId);
    }
    
    private static String buildQuery(String path) {
        return new String(WEB_SERVICE + path);
    }

}
