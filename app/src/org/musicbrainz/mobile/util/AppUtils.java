package org.musicbrainz.mobile.util;

/**
 * Utility methods specific to this app.
 */
public class AppUtils {
    
    public static String makeCoverUrl(String asin) {
        return "http://ec1.images-amazon.com/images/P/" + asin;
    }
    
    public static String makeThumbUrl(String asin) {
        return "http://ec1.images-amazon.com/images/P/" + asin + ".01.THUMZ";
    }

}
