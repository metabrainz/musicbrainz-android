/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.data;

import org.musicbrainz.android.api.util.StringFormat;

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
