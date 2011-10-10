/*
 * Copyright (C) 2011 Jamie McDonald
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

package org.musicbrainz.android.api.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

public class WebServiceUtils {

    static String sanitise(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

    static String entityString(MBEntity entity) {
        switch (entity) {
        case ARTIST:
            return "artist";
        case RELEASE_GROUP:
            return "release-group";
        default:
            return "artist";
        }
    }

    public static LinkedList<String> sanitiseCommaSeparatedTags(String tags) {

        LinkedList<String> tagList = new LinkedList<String>();
        String[] split = tags.split(",");

        for (String tag : split) {
            tag = tag.toLowerCase();
            tag = tag.trim();
            tagList.add(tag);
        }
        return tagList;
    }

}
