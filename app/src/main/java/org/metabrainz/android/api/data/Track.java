package org.metabrainz.android.api.data;

import org.metabrainz.android.api.util.StringFormat;

public class Track {

    private String title;
    private String recordingMbid;
    private int position;
    private int duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getRecordingMbid() {
        return recordingMbid;
    }
    
    public void setRecordingMbid(String recordingMbid) {
        this.recordingMbid = recordingMbid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        return StringFormat.formatDuration(duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
