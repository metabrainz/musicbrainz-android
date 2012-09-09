package org.musicbrainz.android.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.musicbrainz.android.api.webservice.Entity;

public class WebServiceUtils {

    public static String sanitise(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

    public static String entityString(Entity entity) {
        switch (entity) {
        case ARTIST:
            return "artist";
        case RELEASE_GROUP:
            return "release-group";
        case LABEL:
            return "label";
        case RECORDING:
            return "recording";
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
