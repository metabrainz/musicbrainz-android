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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.musicbrainz.android.api.data.UserData;

/**
 * Performs webservice requests specific to registered users.
 */
public class UserClient extends WebClient {
	
	public static final String AUTH_REALM = "musicbrainz.org";
	public static final String AUTH_SCOPE = "musicbrainz.org";
	public static final int AUTH_PORT = 80;
	public static final String AUTH_TYPE = "Digest";
	
	private String clientId = "?client=musicbrainz.android-";
	
	public UserClient (String username, String password, String clientVersion) {
		configureHttpClient(username, password);
		clientId += clientVersion;
	}
	
	private void configureHttpClient(String username, String password) {
		AuthScope authScope = new AuthScope(AUTH_SCOPE, AUTH_PORT, AUTH_REALM, AUTH_TYPE);
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
	}

	public boolean autenticateUserCredentials() throws IOException {
		HttpGet authenticationTest = new HttpGet(QueryBuilder.authenticationCheck());
		authenticationTest.setHeader("Accept", "application/xml");
		try {
			httpClient.execute(authenticationTest, new BasicResponseHandler());
		} catch (HttpResponseException e) {		
			return false;
		}
		return true;
	}
	
	public UserData getUserData(MBEntity entityType, String mbid) throws IOException {
		HttpEntity entity = get(QueryBuilder.userData(entityType, mbid));
		InputStream response = entity.getContent();
		UserData userData = responseParser.parseUserData(response);
		entity.consumeContent();
		return userData;
	}

	public void submitTags(MBEntity entityType, String mbid, Collection<String> tags) throws IOException {
		String url = QueryBuilder.tagSubmission(clientId);
		String content = XmlBuilder.buildTagSubmissionXML(entityType, mbid, tags);
		post(url, content);
	}
	
	public void submitRating(MBEntity entityType, String mbid, int rating) throws IOException {
		String url = QueryBuilder.ratingSubmission(clientId);
		String content = XmlBuilder.buildRatingSubmissionXML(entityType, mbid, rating);
		post(url, content);
	}
	
	public void submitBarcode(String mbid, String barcode) throws IOException {
		String url = QueryBuilder.barcodeSubmission(clientId);
		String content = XmlBuilder.buildBarcodeSubmissionXML(mbid, barcode);
		post(url, content);
	}
	
	private void post(String url, String content) throws IOException {
		HttpPost post = new HttpPost(url);
		post.addHeader("Content-Type", "application/xml; charset=UTF-8");
		StringEntity xml = new StringEntity(content, "UTF-8");
		post.setEntity(xml);
		HttpResponse response = httpClient.execute(post);
		if (response != null) {
			response.getEntity().consumeContent();
		}
	}
	
}
