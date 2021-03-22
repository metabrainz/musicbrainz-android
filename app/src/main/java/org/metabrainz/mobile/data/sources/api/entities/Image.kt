package org.metabrainz.mobile.data.sources.api.entities;

import java.util.ArrayList;

public class Image {
    private ArrayList<String> types = new ArrayList<>();
    private boolean front, back;
    private String image;
    private Thumbnail thumbnails;

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Thumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnail thumbnails) {
        this.thumbnails = thumbnails;
    }

    public static class Thumbnail {
        String small;
        String large;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }
}
