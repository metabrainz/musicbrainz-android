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

/**
 * Link URL and type description pair.
 */
public class WebLink implements Comparable<WebLink> {

    private String target;
    private String type;

    public String getUrl() {
        return target;
    }

    public String getPrettyUrl() {
        // Remove "http://"
        String prettyUrl = target.substring(7);
        if (prettyUrl.endsWith("/")) {
            prettyUrl = prettyUrl.substring(0, prettyUrl.length() - 1);
        }
        return prettyUrl;
    }

    public void setUrl(String url) {
        this.target = url;
    }

    public String getType() {
        return type;
    }

    public String getPrettyType() {
        type = type.replace('_', ' ');
        return StringFormat.initialCaps(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int compareTo(WebLink another) {
        return getPrettyType().compareTo(another.getPrettyType());
    }

}
