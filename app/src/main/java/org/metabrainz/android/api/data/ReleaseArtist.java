package org.metabrainz.android.api.data;

/**
 * Artist name and MBID pair for release.
 */
public class ReleaseArtist implements Comparable<ReleaseArtist> {

    private String mbid;
    private String name;
    private String sortName;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortName() {
        return sortName;
    }
    
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    @Override
    public int compareTo(ReleaseArtist another) {
        return getSortName().compareTo(another.getSortName());
    }
    
}