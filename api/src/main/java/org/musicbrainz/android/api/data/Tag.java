package org.musicbrainz.android.api.data;

/**
 * Tag name and count pair. Count can be used for weighting.
 */
public class Tag implements Comparable<Tag> {

    private String text;
    private int count;

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int compareTo(Tag another) {
        Integer a = (Integer) getCount();
        Integer b = (Integer) another.getCount();
        return a.compareTo(b) * -1;
    }

}
