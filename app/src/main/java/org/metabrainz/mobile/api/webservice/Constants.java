package org.metabrainz.mobile.api.webservice;

public class Constants {
    public static final String LOOKUP_ARTIST_PARAMS =
            "url-rels+artist-rels+label-rels+tags+ratings";
    public static final String LOOKUP_RELEASE_PARAMS =
            "release-groups+artists+recordings+labels+tags+ratings+url-rels+artist-rels";
    public static final String LOOKUP_LABEL_PARAMS =
            "tags+ratings+url-rels";
    public static final String LOOKUP_RECORDING_PARAMS =
            "artists+tags+ratings";
    public static final String LOOKUP_RELEASE_GROUP_PARAMS =
            "artist-credits+tags+ratings+url-rels";

    public static final String LIMIT = "100";
    public static final String OFFSET = "0";
}
