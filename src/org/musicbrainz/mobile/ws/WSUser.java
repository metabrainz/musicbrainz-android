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

package org.musicbrainz.mobile.ws;

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
import org.musicbrainz.mobile.data.UserData;
import org.musicbrainz.mobile.util.Config;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

/**
 * Class to perform all webservice requests that need the user to be
 * authenticated.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class WSUser extends WebService {
	
	// authentication
	public static final String SCOPE = Config.SCOPE;
	public static final String REALM = Config.REALM;

	// request
	private static final String TAG = "tag";
	private static final String RATING = "rating";
	private static final String BARCODE = "release/";
	
	// MBID for Various Artists always exists
	private static final String TEST = "artist/89ad4ac3-39f7-470e-963a-56509c546377?inc=user-tags";
	
	private String clientId = "?client=musicbrainz.android-";
	private DefaultHttpClient httpClient;
	
	public WSUser (String username, String password, String clientVersion) {
		httpClient = new DefaultHttpClient();
		
		clientId = clientId + clientVersion;
		AuthScope authScope = new AuthScope(SCOPE, 80, REALM, "Digest");
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
	}

	/**
	 * Attempt to authenticate the username and password with the webservice and
	 * return whether or not it was successful.
	 * 
	 * @param uname
	 * @param pass
	 * @return Authentication success
	 * @throws IOException
	 */
	public boolean authenticate() throws IOException {
		
		HttpGet test = new HttpGet(WEB_SERVICE + TEST); // authentication test
		test.setHeader("Accept", "application/xml");
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		try {
			httpClient.execute(test, handler);
		} catch (HttpResponseException e) {
			System.err.println(e.getStatusCode() +": "+ e.getMessage());
			
			if (e.getStatusCode() == 401) 
				return false; // authentication failure
		}
		return true;
	}
	
	/**
	 * Retrieve user tags and rating data for a particular entity.
	 * 
	 * @param type
	 * @param entityID
	 * @return UserData object
	 * @throws IOException
	 */
	public UserData getUserData(MBEntity type, String entityID) throws IOException {
		
		String entity = "";
		switch(type) {
		case ARTIST:
			entity = "artist";
			break;
		case RELEASE_GROUP:
			entity = "release-group";
		}
		
		HttpGet userGet = new HttpGet(WEB_SERVICE + entity + "/" + entityID + "?inc=user-tags+user-ratings");
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

	/**
	 * Submit a collection of tags to a particular entity.
	 * 
	 * @param type
	 * @param entityID
	 * @param tags
	 * @throws IOException
	 */
	public void submitTags(MBEntity type, String entityID, Collection<String> tags) throws IOException {
		
		String tagUrl = WEB_SERVICE + TAG + clientId;
		
		HttpPost post = new HttpPost(tagUrl);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		
		// create tag submission XML
		StringBuilder content = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">");
		switch (type) {
		case ARTIST:
			content.append("<artist-list><artist id=\"" 
				+ entityID 
				+ "\">" 
				+ formatTags(tags) 
				+ "</artist></artist-list>");
			break;
		case RELEASE_GROUP:
			content.append("<release-group-list><release-group id=\"" 
				+ entityID 
				+ "\">" 
				+ formatTags(tags) 
				+ "</release-group></release-group-list>");
		}
		content.append("</metadata>");
		
		StringEntity xml = new StringEntity(content.toString(), "UTF-8");
		post.setEntity(xml);
		
		// TODO execute is the blocking call, eventually we want this in a thread in PostService
		try {
			httpClient.execute(post);
		} catch (HttpResponseException e) {
			System.out.println(e.getStatusCode() + ": " + e.getMessage());
		}
	}
	
	/*
	 * Method takes a collection of individual tag strings and returns an XML
	 * user tag list.
	 */
	private static String formatTags(Collection<String> tags) {
		StringBuilder tagString = new StringBuilder("<user-tag-list>");
		
		for (String tag : tags) 
			tagString.append("<user-tag><name>" + tag + "</name></user-tag>");
		
		tagString.append("</user-tag-list>");
		return tagString.toString();
	}
	
	/**
	 * Submit a user rating to a particular entity.
	 * 
	 * @param type
	 * @param entityID
	 * @param rating
	 * @throws IOException
	 */
	public void submitRating(MBEntity type, String entityID, int rating) throws IOException {
		
		String ratingUrl = WEB_SERVICE + RATING + clientId;
		
		HttpPost post = new HttpPost(ratingUrl);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		
		// create rating submission XML
		StringBuilder content = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">");
		switch (type) {
		case ARTIST:
			content.append("<artist-list><artist id=\"" 
				+ entityID + "\"><user-rating>" 
				+ rating * 20 
				+ "</user-rating></artist></artist-list>");
			break;
		case RELEASE_GROUP:
			content.append("<release-group-list><release-group id=\"" 
				+ entityID + "\"><user-rating>" 
				+ rating * 20 
				+ "</user-rating></release-group></release-group-list>");
		}
		content.append("</metadata>");
		
		StringEntity xml = new StringEntity(content.toString(), "UTF-8");
		post.setEntity(xml);
		
		try {
			httpClient.execute(post);
		} catch (HttpResponseException e) {
			System.err.println(e.getStatusCode() + ": " + e.getMessage());
		}

	}
	
	/**
	 * Add a release to the user collection given a release ID.
	 * 
	 * TODO Implement this feature when collection support becomes available in
	 * the webservice.
	 * 
	 * @param releaseID
	 */
	public void addToCollection(String releaseID) {

	}
	
	/**
	 * Shutdown HTTP connection manager.
	 */
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * Utility method to transform a string of potentially many comma separated
	 * tags into a list for submission.
	 * 
	 * @param tags String of one or more tags.
	 * @return List of individual tag strings.
	 */
	public static LinkedList<String> processTags(String tags) {
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
