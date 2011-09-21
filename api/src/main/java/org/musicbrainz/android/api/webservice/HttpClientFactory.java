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

import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

/**
 * 
 *
 */
public class HttpClientFactory {
	
	private static final String USER_AGENT = "MBAndroid/1.0";
	private static final int TIMEOUT = 20000;
	private static final int MAX_CONNECTIONS = 5;

	static DefaultHttpClient client;

	static {
		HttpParams params = setupParams();
	    SchemeRegistry schemeRegistry = setupSchemeRegistry();
		ThreadSafeClientConnManager threadSafeManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		client = new DefaultHttpClient(threadSafeManager, params);
	}

	private static HttpParams setupParams() {
		HttpParams params = new BasicHttpParams();
	    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	    params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
	    params.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
	    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
	    params.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		return params;
	}
	
	private static SchemeRegistry setupSchemeRegistry() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
	    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		return schemeRegistry;
	}

}
