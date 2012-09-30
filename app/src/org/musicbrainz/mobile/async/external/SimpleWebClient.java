package org.musicbrainz.mobile.async.external;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.util.Log;

import android.os.Build;

public abstract class SimpleWebClient {

    public SimpleWebClient() {
        enableHttpResponseCache();
        disableConnectionReuseIfNecessary();
    }
    
    protected InputStream getConnection(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", App.getUserAgent());
        return new BufferedInputStream(conn.getInputStream());
    }
    
    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MB
            File httpCacheDir = new File(App.getContext().getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache").getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception e) {
            Log.d("HTTP response cache not available.");
        }
    }

    @SuppressWarnings("deprecation")
    private void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
    
}
