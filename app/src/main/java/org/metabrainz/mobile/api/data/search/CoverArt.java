package org.metabrainz.mobile.api.data.search;

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

    public class Image {
        private ArrayList<String> types= new ArrayList<>();
        private boolean front,back;
        private String image;

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
    }

}
