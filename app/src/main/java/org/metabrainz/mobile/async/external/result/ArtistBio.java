package org.metabrainz.mobile.async.external.result;

import android.text.TextUtils;

import org.metabrainz.mobile.string.StringFormat;

public class ArtistBio {

    private String wikipediaBio;

    public ArtistBio(String lastFmImage, String lastFmBio, String wikipediaBio) {
        this.wikipediaBio = wikipediaBio;
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
