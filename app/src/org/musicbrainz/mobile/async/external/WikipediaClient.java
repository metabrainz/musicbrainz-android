package org.musicbrainz.mobile.async.external;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.musicbrainz.mobile.async.external.result.WikipediaBio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WikipediaClient extends SimpleWebClient {

    public String getArtistBio(String pageName) throws IOException {
        InputStream stream = getConnection(buildWikipediaUrl(pageName));
        String response = parseResult(stream);
        stream.close();
        return response;
    }
    
    public String buildWikipediaUrl(String pageName) {
        return "http://en.wikipedia.org/w/api.php?action=mobileview&format=json&prop=text&sections=0&page=" + pageName;
    }
    
    private String parseResult(InputStream stream) {
        Reader reader = new InputStreamReader(stream);
        JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
        WikipediaBio response = new Gson().fromJson(obj.get("mobileview"), WikipediaBio.class);
        return response.sections.get(0).text;
    }
    
}
