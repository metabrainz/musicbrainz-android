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

package org.musicbrainz.android.api.ws;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.musicbrainz.android.api.WebserviceConfig;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.parsers.UserDataParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This class performs webservice requests specific to registered users.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class WebServiceUser extends WebService {
	
	public static final String WEB_SERVICE = WebserviceConfig.WEB_SERVICE;
	
	public static final String AUTH_SCOPE = WebserviceConfig.SCOPE;
	public static final int AUTH_PORT = 80;
	public static final String AUTH_REALM = WebserviceConfig.REALM;
	public static final String AUTH_TYPE = "Digest";
	
	// MBID for Various Artists always exists.
	private static final String AUTH_TEST = "artist/89ad4ac3-39f7-470e-963a-56509c546377?inc=user-tags";

	private static final String TAG = "tag";
	private static final String RATING = "rating";
	private static final String BARCODE = "release/";
	
	private String clientId = "?client=musicbrainz.android-";
	private DefaultHttpClient httpClient;
	
	public WebServiceUser (String username, String password, String clientVersion) {
		
		httpClient = new DefaultHttpClient();
		clientId += clientVersion;
		AuthScope authScope = new AuthScope(AUTH_SCOPE, AUTH_PORT, AUTH_REALM, AUTH_TYPE);
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
	}

	public boolean autenticateUserCredentials() throws IOException {
		
		HttpGet authenticationTest = new HttpGet(WEB_SERVICE + AUTH_TEST);
		authenticationTest.setHeader("Accept", "application/xml");
	
		try {
			httpClient.execute(authenticationTest, new BasicResponseHandler());
		} catch (HttpResponseException e) {			
			if (e.getStatusCode() == 401) {
				return false;
			}
		}
		return true;
	}
	
	public UserData getUserData(MBEntity entityType, String entityMbid) throws IOException {
		
		String entity = "";
		switch(entityType) {
		case ARTIST:
			entity = "artist";
			break;
		case RELEASE_GROUP:
			entity = "release-group";
		}
		
		HttpGet userGet = new HttpGet(WEB_SERVICE + entity + "/" + entityMbid + "?inc=user-tags+user-ratings");
		userGet.setHeader("Accept", "application/xml");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();	
		String response = httpClient.execute(userGet, responseHandler);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		UserData data = new UserData();
		try {
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			UserDataParser handler = new UserDataParser(data);
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(response)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void submitTags(MBEntity entityType, String entityMbid, Collection<String> tags) throws IOException {
		
		String tagSubmissionUrl = WEB_SERVICE + TAG + clientId;
		HttpPost post = new HttpPost(tagSubmissionUrl);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		
		String requestContent = buildTagSubmissionXML(entityType, entityMbid, tags);
		StringEntity xml = new StringEntity(requestContent, "UTF-8");
		post.setEntity(xml);
		
		try {
			httpClient.execute(post);
		} catch (HttpResponseException e) {
			//Log.e(e.getStatusCode() + ": " + e.getMessage());
		}
	}

	private String buildTagSubmissionXML(MBEntity entityType, String entityMbid, Collection<String> tags) {
		
		StringBuilder content = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">");
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
		content.append("</metadata>");
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
	
	public void submitRating(MBEntity entityType, String entityMbid, int rating) throws IOException {
		
		String ratingSubmissionUrl = WEB_SERVICE + RATING + clientId;
		HttpPost post = new HttpPost(ratingSubmissionUrl);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		
		String requestContent = buildRatingSubmissionXML(entityType, entityMbid, rating);
		StringEntity xml = new StringEntity(requestContent, "UTF-8");
		post.setEntity(xml);
		
		try {
			httpClient.execute(post);
		} catch (HttpResponseException e) {
			//Log.e(e.getStatusCode() + ": " + e.getMessage());
		}

	}

	private String buildRatingSubmissionXML(MBEntity entityType, String entityMbid, int rating) {
		
		StringBuilder content = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">");
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
		content.append("</metadata>");
		return content.toString();
	}
	
	public void submitBarcode(String releaseMbid, String barcode) throws IOException {
		
		String barcodeSubmissionUrl = WEB_SERVICE + BARCODE + clientId;
		HttpPost post = new HttpPost(barcodeSubmissionUrl);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		
		String requestContent = buildBarcodeSubmissionXML(releaseMbid, barcode);
		StringEntity xml = new StringEntity(requestContent, "UTF-8");
		post.setEntity(xml);
		
		try {
			httpClient.execute(post);
		} catch (HttpResponseException e) {
			//Log.e(e.getStatusCode() + ": " + e.getMessage());
		}
	}

	private String buildBarcodeSubmissionXML(String releaseMbid, String barcode) {
		
		StringBuilder content = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">");
		content.append("<release-list><release id=\"" 
				+ releaseMbid + "\"><barcode>"
				+ barcode
				+ "</barcode></release></release-list>");
		content.append("</metadata>");
		return content.toString();
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
	
	public void shutdownConnectionManager() {
		httpClient.getConnectionManager().shutdown();
	}
	
}