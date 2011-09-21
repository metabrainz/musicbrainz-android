/*
 * Copyright (C) 2011 Jamie McDonald
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

package org.musicbrainz.android.api.webservice;

import java.util.Collection;

public class XmlBuilder {
	
	private static final String HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">";
	private static final String FOOT = "</metadata>";

	static String buildTagSubmissionXML(MBEntity entityType, String entityMbid, Collection<String> tags) {
		StringBuilder content = new StringBuilder(HEAD);
		switch (entityType) {
		case ARTIST:
			content.append("<artist-list><artist id=\"" 
				+ entityMbid 
				+ "\">" 
				+ getTagsInXML(tags) 
				+ "</artist></artist-list>");
			break;
		case RELEASE_GROUP:
			content.append("<release-group-list><release-group id=\"" 
				+ entityMbid 
				+ "\">" 
				+ getTagsInXML(tags) 
				+ "</release-group></release-group-list>");
		}
		content.append(FOOT);
		return content.toString();
	}
	
	private static String getTagsInXML(Collection<String> tags) {
		StringBuilder tagString = new StringBuilder("<user-tag-list>");
		for (String tag : tags) {
			tagString.append("<user-tag><name>" + tag + "</name></user-tag>");
		}
		tagString.append("</user-tag-list>");
		return tagString.toString();
	}
	
	static String buildRatingSubmissionXML(MBEntity entityType, String entityMbid, int rating) {
		StringBuilder content = new StringBuilder(HEAD);
		switch (entityType) {
		case ARTIST:
			content.append("<artist-list><artist id=\"" 
				+ entityMbid + "\"><user-rating>" 
				+ rating * 20 
				+ "</user-rating></artist></artist-list>");
			break;
		case RELEASE_GROUP:
			content.append("<release-group-list><release-group id=\"" 
				+ entityMbid + "\"><user-rating>" 
				+ rating * 20 
				+ "</user-rating></release-group></release-group-list>");
		}
		content.append(FOOT);
		return content.toString();
	}
	
	static String buildBarcodeSubmissionXML(String releaseMbid, String barcode) {
		StringBuilder content = new StringBuilder(HEAD);
		content.append("<release-list><release id=\"" 
				+ releaseMbid + "\"><barcode>"
				+ barcode
				+ "</barcode></release></release-list>");
		content.append(FOOT);
		return content.toString();
	}
	
}
