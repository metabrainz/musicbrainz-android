package org.musicbrainz.android.api.ws;

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
		switch(entity) {
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
