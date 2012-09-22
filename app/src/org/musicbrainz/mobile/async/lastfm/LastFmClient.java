package org.musicbrainz.mobile.async.lastfm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.lastfm.Response.Artist;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.util.Log;

import android.os.Build;

import com.google.gson.Gson;

public class LastFmClient {

    public LastFmClient() {
        enableHttpResponseCache();
        disableConnectionReuseIfNecessary();
    }

    public Artist getArtistInfo(String mbid) throws IOException {
        URLConnection urlConnection = new URL(buildArtistInfoUrl(mbid)).openConnection();
        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        Response response = parseResult(stream);
        stream.close();
        return response.artist;
    }

    private String buildArtistInfoUrl(String mbid) {
        return "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&mbid=" + mbid + "&api_key="
                + Secrets.LASTFM_API_KEY + "&format=json";
    }

    private Response parseResult(InputStream stream) {
        Reader reader = new InputStreamReader(stream);
        return new Gson().fromJson(reader, Response.class);
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
