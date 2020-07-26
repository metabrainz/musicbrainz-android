package org.metabrainz.mobile;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.io.IOException;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.fail;
import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions;
import static org.metabrainz.mobile.EntityUtils.getTestArtist;
import static org.metabrainz.mobile.EntityUtils.getTestRelease;
import static org.metabrainz.mobile.EntityUtils.getTestReleaseMBID;
import static org.metabrainz.mobile.EntityUtils.loadResourceAsString;


public class LookupTest {
    MockWebServer webServer;
    LookupService service;

    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule();

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

    @Test
    public void testArtistLookup() {
        Artist testArtist = getTestArtist();
        MutableLiveData<Artist> testArtistData = new MutableLiveData<>();

        service.lookupEntityData(MBEntityType.ARTIST.name, testArtist.getMbid(),
                Constants.LOOKUP_ARTIST_PARAMS).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Artist artist = new Gson().fromJson(response.body().string(), Artist.class);
                    testArtistData.setValue(artist);
                } catch (IOException e) {
                    e.printStackTrace();
                    fail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        try {
            checkArtistAssertions(testArtist, LiveDataTestUtil.getOrAwaitValue(testArtistData));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testReleaseLookup() {
        String MBID = getTestReleaseMBID();
        Release testRelease = getTestRelease();
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

    @After
    public void teardown() {
        try {
            webServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}