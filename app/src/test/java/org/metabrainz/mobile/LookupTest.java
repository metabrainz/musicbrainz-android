package org.metabrainz.mobile;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LookupTest {
    MockWebServer webServer;
    LookupService service;

    @Before
    public void setup() {
        try {
            webServer = new MockWebServer();
            webServer.setDispatcher(new Dispatcher() {
                @NotNull
                @Override
                public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                    String endpoint = recordedRequest.getPath()
                            .substring(1, recordedRequest.getPath().indexOf('/', 1));
                    String file = endpoint + "_lookup.json";
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(loadResourceAsString(file));
                }
            });
            webServer.start();
            service = MusicBrainzServiceGenerator
                    .createTestService(LookupService.class, webServer.url("/"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private String loadResourceAsString(String resource) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        return builder.toString();
    }

    @Test
    public void testArtistLookup() {
        final String MBID = "b8a7c51f-362c-4dcb-a259-bc6e0095f0a6";
        Artist testArtist = new Artist();
        testArtist.setMbid(MBID);
        testArtist.setCountry("GB");
        testArtist.setDisambiguation("famous UK singer-songwriter");
        testArtist.setName("Ed Sheeran");
        testArtist.setSortName("Sheeran, Ed");
        testArtist.setGender("Male");

        try {
            Response<ResponseBody> response = service.lookupEntityData(
                    MBEntityType.ARTIST.name, MBID, Constants.LOOKUP_ARTIST_PARAMS).execute();
            Artist artist = new Gson().fromJson(response.body().string(), Artist.class);
            checkArtistAssertions(testArtist, artist);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseLookup() {
        final String MBID = "94ad3a58-a1cc-46a3-acf4-9cb6c1d6f032";
        Release testRelease = new Release();
        testRelease.setMbid(MBID);
        testRelease.setTitle("+");
        testRelease.setBarcode("5052498646524");
        testRelease.setStatus("Official");
        testRelease.setCountry("XE");
        testRelease.setDisambiguation("");
        testRelease.setDate("2011-09-09");

        try {
            Response<ResponseBody> response = service.lookupEntityData(
                    MBEntityType.RELEASE.name, MBID, Constants.LOOKUP_RELEASE_PARAMS).execute();
            Release release = new Gson().fromJson(response.body().string(), Release.class);
            checkReleaseAssertions(testRelease, release);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void checkReleaseAssertions(Release testRelease, Release release) {
        assertEquals(testRelease, release);
        assertEquals(testRelease.getTitle(), release.getTitle());
        assertEquals(testRelease.getBarcode(), release.getBarcode());
        assertEquals(testRelease.getStatus(), release.getStatus());
        assertEquals(testRelease.getCountry(), release.getCountry());
        assertEquals(testRelease.getDisambiguation(), release.getDisambiguation());
        assertEquals(testRelease.getDate(), release.getDate());
    }

    private void checkArtistAssertions(Artist testArtist, Artist artist) {
        assertEquals(artist, testArtist);
        assertEquals(testArtist.getCountry(), artist.getCountry());
        assertEquals(testArtist.getDisambiguation(), artist.getDisambiguation());
        assertEquals(testArtist.getName(), artist.getName());
        assertEquals(testArtist.getSortName(), artist.getSortName());
        assertEquals(testArtist.getGender(), artist.getGender());
    }

    @After
    public void teardown() {
        try {
            webServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}