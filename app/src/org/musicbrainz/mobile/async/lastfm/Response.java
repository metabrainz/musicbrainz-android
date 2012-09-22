package org.musicbrainz.mobile.async.lastfm;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Response {

    public Artist artist;
    
    public class Artist {
        public String url;
        public ArrayList<Image> image;
        public Bio bio;
    }
    
    public class Bio {
        @SerializedName("content")
        public String full;
    }
    
    public class Image {
        @SerializedName("#text")
        public String text;
    }
    
}
