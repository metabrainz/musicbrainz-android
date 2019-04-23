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

    public ArrayList<String> getAllImageLinks() {
        ArrayList<String> urls = new ArrayList<>();
        for (Image image : images)
            if (image != null && !image.getImage().isEmpty())
                urls.add(image.getImage());
        return urls;
    }

    public ArrayList<String> getAllThumbnailsLinks() {
        ArrayList<String> urls = new ArrayList<>();
        for (Image image : images)
            if (image != null && image.getThumbnails() != null)
                urls.add(image.getThumbnails().getSmall());
        return urls;
    }

}
