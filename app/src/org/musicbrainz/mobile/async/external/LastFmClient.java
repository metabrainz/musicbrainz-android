package org.musicbrainz.mobile.async.external;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.musicbrainz.mobile.async.external.result.LastFmResponse;
import org.musicbrainz.mobile.async.external.result.LastFmResponse.LastFmArtist;
import org.musicbrainz.mobile.config.Secrets;

import com.google.gson.Gson;

public class LastFmClient extends SimpleWebClient {

    public LastFmArtist getArtistInfo(String mbid) throws IOException {
        InputStream stream = getConnection(buildArtistInfoUrl(mbid));
        LastFmResponse response = parseResult(stream);
        stream.close();
        return response.artist;
    }

    private String buildArtistInfoUrl(String mbid) {
        return "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&mbid=" + mbid + "&api_key="
                + Secrets.LASTFM_API_KEY;
    }

    private LastFmResponse parseResult(InputStream stream) {
        Reader reader = new InputStreamReader(stream);
        return new Gson().fromJson(reader, LastFmResponse.class);
    }

}
