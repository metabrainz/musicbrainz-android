package org.musicbrainz.mobile.async.external.result;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class LastFmArtist {

    public String url;
    public ArrayList<Image> image;
    public Bio bio;

    public class Bio {
        @SerializedName("content")
        public String full;
    }

    public class Image {
        @SerializedName("#text")
        public String text;
    }

}
