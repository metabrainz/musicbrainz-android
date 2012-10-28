package org.musicbrainz.mobile.async.external.result;

import org.musicbrainz.mobile.string.StringFormat;

import android.text.TextUtils;

public class ArtistBio {

    private String lastFmImage;
    private String lastFmBio;
    private String wikipediaBio;
    
    public ArtistBio(String lastFmImage, String lastFmBio, String wikipediaBio) {
        this.lastFmImage = lastFmImage;
        this.lastFmBio = lastFmBio;
        this.wikipediaBio = wikipediaBio;
    }
    
    public ArtistBio(String lastFmImage, String lastFmBio) {
        this.lastFmImage = lastFmImage;
        this.lastFmBio = lastFmBio;
    }

    public String getLastFmImage() {
        return lastFmImage;
    }

    public void setLastFmImage(String lastFmImage) {
        this.lastFmImage = lastFmImage;
    }

    public String getLastFmBio() {
        if (!TextUtils.isEmpty(lastFmBio)) {
            String bio = StringFormat.lineBreaksToHtml(lastFmBio);
            bio = StringFormat.stripFromEnd("<br/>User-contributed", bio);
            return StringFormat.stripLinksAndRefs(bio);
        }
        return null;
    }

    public void setLastFmBio(String lastFmBio) {
        this.lastFmBio = lastFmBio;
    }

    public String getWikipediaBio() {
        if (!TextUtils.isEmpty(wikipediaBio)) {
            String bio = StringFormat.stripTablesAndDivs(wikipediaBio);
            bio = StringFormat.stripImageTags(bio);
            return StringFormat.stripLinksAndRefs(bio);
        }
        return null;
    }

    public void setWikipediaBio(String wikipediaBio) {
        this.wikipediaBio = wikipediaBio;
    }
    
}
