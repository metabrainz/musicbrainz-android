/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

/**
 * Configures the static http client. Gzip code is based on BetterHttp in
 * droid-fu.
 */
public class HttpClient {

    private static final String USER_AGENT = "MBAndroid/1.0";
    private static final int TIMEOUT = 20000;
    private static final int MAX_CONNECTIONS = 5;

    private static DefaultHttpClient client;

    public static AbstractHttpClient getClient() {
        return client;
    }

    static {
        HttpParams params = setupParams();
        SchemeRegistry schemeRegistry = setupSchemeRegistry();
        ThreadSafeClientConnManager threadSafeManager = new ThreadSafeClientConnManager(params, schemeRegistry);
        client = new DefaultHttpClient(threadSafeManager, params);
        enableGzipEncoding();
    }

    private static HttpParams setupParams() {
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setTimeout(params, TIMEOUT);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(MAX_CONNECTIONS));
        ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(params, USER_AGENT);
        return params;
    }

    private static SchemeRegistry setupSchemeRegistry() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        return schemeRegistry;
    }

    public static void clearCredentials() {
        client.getCredentialsProvider().clear();
    }

    private static void enableGzipEncoding() {
        client.addRequestInterceptor(new GzipHttpRequestInterceptor());
        client.addResponseInterceptor(new GzipHttpResponseInterceptor());
    }

    private static class GzipHttpRequestInterceptor implements HttpRequestInterceptor {

        public void process(final HttpRequest request, final HttpContext context) {
            if (!request.containsHeader("Accept-Encoding")) {
                request.addHeader("Accept-Encoding", "gzip");
            }
        }
    }

    private static class GzipHttpResponseInterceptor implements HttpResponseInterceptor {
        public void process(final HttpResponse response, final HttpContext context) {
            final HttpEntity entity = response.getEntity();
            final Header encoding = entity.getContentEncoding();
            if (encoding != null) {
                inflateGzip(response, encoding);
            }
        }

        private void inflateGzip(final HttpResponse response, final Header encoding) {
            for (HeaderElement element : encoding.getElements()) {
                if (element.getName().equalsIgnoreCase("gzip")) {
                    response.setEntity(new GzipInflatingEntity(response.getEntity()));
                    break;
                }
            }
        }
    }

    private static class GzipInflatingEntity extends HttpEntityWrapper {

        public GzipInflatingEntity(final HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

}
