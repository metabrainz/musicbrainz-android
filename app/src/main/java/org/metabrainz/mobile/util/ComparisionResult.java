package org.metabrainz.mobile.util;

public class ComparisionResult {
    private double score;
    private String releaseMbid;
    private String trackMbid;

    public ComparisionResult(double score, String releaseMbid, String trackMbid) {
        this.score = score;
        this.releaseMbid = releaseMbid;
        this.trackMbid = trackMbid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReleaseMbid() {
        return releaseMbid;
    }

    public void setReleaseMbid(String releaseMbid) {
        this.releaseMbid = releaseMbid;
    }

    public String getTrackMbid() {
        return trackMbid;
    }

    public void setTrackMbid(String trackMbid) {
        this.trackMbid = trackMbid;
    }
}
