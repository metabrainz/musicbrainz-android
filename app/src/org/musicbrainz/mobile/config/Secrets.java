package org.musicbrainz.mobile.config;

/**
 * These values are required to build the app but they are simply debug/sandbox
 * values. Release builds use a file with a different set of values for obvious
 * reasons.
 */
public class Secrets {

    public static final String BUGSENSE_API_KEY = "99f3740e";
    public static final String PAYPAL_APP_ID = "APP-80W284485P519543T";
    public static final String LASTFM_API_KEY = "b25b959554ed76058ac220b7b2e0a026";

    public String getKey() {
        return "aplaceholderstring";
    }

}