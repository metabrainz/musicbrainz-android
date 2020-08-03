package org.metabrainz.mobile.data.sources.api.entities;

import com.google.gson.annotations.SerializedName;

public class WikiSummary {

    @SerializedName("displaytitle")
    private String displayTitle;
    private long pageId;
    @SerializedName("originalimage")
    private OriginalImage image;
    private String extract;

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public OriginalImage getOriginalImage() {
        return image;
    }

    public void setOriginalImage(OriginalImage originalImage) {
        this.image = originalImage;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    private static class OriginalImage {
        private String source;
        private int width, height;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
