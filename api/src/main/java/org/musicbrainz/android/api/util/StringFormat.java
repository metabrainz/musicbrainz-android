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

package org.musicbrainz.android.api.util;

import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Set of static methods for String formatting.
 * 
 * TODO currently duplicated in api and app. Eliminate one or other.
 */
public class StringFormat {

	/**
	 * Takes a Collection of strings and formats them comma separated as
	 * "st1, st2, st3".
	 */
	public static String commaSeparate(Collection<String> collection) {
		
		StringBuilder sb = new StringBuilder();
		
		if (collection.isEmpty())
			return sb.toString();
		
		for (String item : collection) 
			sb.append(item + ", ");
		
		return sb.substring(0, sb.length() - 2);
	}
	
	/**
	 * Takes a string and returns the string with initial capitals.
	 */
	public static String initialCaps(String text) {

		StringTokenizer tokenizer = new StringTokenizer(text, " ", true);
		StringBuilder sb = new StringBuilder();

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			token = String.format("%s%s", Character
					.toUpperCase(token.charAt(0)), token.substring(1));
			sb.append(token);
		}

		return sb.toString();
	}
	
	/**
	 * Takes a track duration and formats as a String for display.
	 */
	public static String trackDuration(int duration) {
		
		// TODO: Would be cleaner with two different String.format()s.
		
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
