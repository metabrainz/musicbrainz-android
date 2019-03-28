package org.metabrainz.mobile.data.sources.api.entities;

import java.util.ArrayList;

public class CoverArt {

    private ArrayList<Image> images = new ArrayList<>();
    private String release;

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

}
