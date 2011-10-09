/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.data;

/**
 * Basic recording data for tracks in release.
 */
public class RecordingStub {

    private String title;
    private int position;
    private int duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return formatDuration(duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static String formatDuration(int duration) {

        // TODO: Would be much cleaner using String.format().

        if (duration == 0) {
            return "";
        }

        int s = duration / 1000;
        int secs = s % 60;
        int mins = (s % 3600) / 60;
        int hrs = s / 3600;

        String mS = "" + mins;
        String sS = "" + secs;
        if (secs < 10)
            sS = "0" + secs;

        if (hrs == 0) {
            return mS + ':' + sS;
        } else {
            if (mins < 10)
                mS = "0" + mins;
            return hrs + ":" + mS + ":" + sS;
        }
    }

}
