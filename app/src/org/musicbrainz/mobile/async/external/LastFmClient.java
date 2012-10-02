package org.musicbrainz.mobile.async.external;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


import org.musicbrainz.mobile.async.external.result.LastFmArtist;
import org.musicbrainz.mobile.config.Secrets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LastFmClient extends SimpleWebClient {

    public LastFmArtist getArtistInfo(String mbid) throws IOException {
        InputStream stream = getConnection(buildArtistInfoUrl(mbid));
        LastFmArtist artist = parseResult(stream);
        stream.close();
        return artist;
    }

    private String buildArtistInfoUrl(String mbid) {
        return "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&mbid=" + mbid + "&api_key="
                + Secrets.LASTFM_API_KEY;
    }

    private LastFmArtist parseResult(InputStream stream) {
        Reader reader = new InputStreamReader(stream);
        JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
        return new Gson().fromJson(obj.get("artist"), LastFmArtist.class);
    }

}
