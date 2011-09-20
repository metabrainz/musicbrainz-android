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

package org.musicbrainz.android.api.ws;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.musicbrainz.android.api.WebserviceConfig;

public class HttpClientFactory {

	private static DefaultHttpClient client;

	public synchronized static DefaultHttpClient getClient() {

		if (client != null) {
			return client;
		}
		createClient();
		return client;
	}

	private static void createClient() {
		client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		params.setParameter(CoreProtocolPNames.USER_AGENT, WebserviceConfig.USER_AGENT);
		ClientConnectionManager manager = client.getConnectionManager();
		ThreadSafeClientConnManager threadSafeManager = new ThreadSafeClientConnManager(params, manager.getSchemeRegistry());
		client = new DefaultHttpClient(threadSafeManager, params);
	}
	
}
