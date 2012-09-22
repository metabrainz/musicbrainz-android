package org.musicbrainz.mobile.util;

public class CoverArt {
    
    private static final String CAA = "http://coverartarchive.org";
    
    public enum Size {
        SMALL, LARGE
    }

    public static String getFront(String mbid, Size size) {
        return CAA + "/release/" + mbid + "/front" + sizePostfix(size);
    }
    
    public static String getBack(String mbid, Size size) {
        return CAA + "/release/" + mbid + "/back" + sizePostfix(size);
    }
    
    private static String sizePostfix(Size size) {
        if (size == Size.LARGE) {
            return "-500";
        } else {
            return "-250";
        }
    }

    public static String getThumb(String asin) {
        return "http://ec1.images-amazon.com/images/P/" + asin + ".01.THUMZ";
    }
    
}
