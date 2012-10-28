package org.musicbrainz.mobile.string;

import java.util.Collection;

import org.musicbrainz.android.api.data.ReleaseArtist;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.mobile.R;

import android.content.Context;

public class StringFormat {

    public static String commaSeparate(Collection<String> collection) {
        StringBuilder sb = new StringBuilder();
        if (collection.isEmpty()) {
            return sb.toString();
        }
        for (String item : collection) {
            sb.append(item + ", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public static String commaSeparateArtists(Collection<ReleaseArtist> artists) {
        StringBuilder sb = new StringBuilder();
        for (ReleaseArtist artist : artists) {
            sb.append(artist.getName() + ", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public static String commaSeparateTags(Collection<Tag> tags, Context context) {
        StringBuilder sb = new StringBuilder();
        if (tags.isEmpty()) {
            return context.getResources().getString(R.string.no_tags);
        }
        for (Tag tag : tags) {
            sb.append(tag.getText() + ", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public static String lineBreaksToHtml(String input) {
        return input.replace("\n", "<br/>");
    }

    public static String stripFromEnd(String toStrip, String input) {
        return input.replaceFirst(toStrip + ".*", "");
    }

    public static String stripLinksAndRefs(String input) {
        // Removes link tags and references like [1].
        return input.replaceAll("\\<a.*?>|</a>|\\[.*?\\]", "");
    }
    
    public static String stripImageTags(String input) {
        return input.replaceAll("\\<img.*?/>", "");
    }
    
    public static String stripTablesAndDivs(String input) {
        // Removes everything between pairs of div and table tags.
        return input.replaceAll("(?s:\\<div.*?</div>|\\<table.*?</table>)", "");
    }

}
