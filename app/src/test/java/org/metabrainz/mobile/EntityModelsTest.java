package org.metabrainz.mobile;

import com.google.gson.Gson;

import org.junit.Test;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import static org.metabrainz.mobile.AssertionUtils.checkArtistAssertions;
import static org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions;
import static org.metabrainz.mobile.EntityUtils.getTestArtist;
import static org.metabrainz.mobile.EntityUtils.getTestRelease;
import static org.metabrainz.mobile.EntityUtils.loadResourceAsString;

public class EntityModelsTest {

    @Test
    public void testArtistModel() {
        Artist testArtist = getTestArtist();
        Artist artist = new Gson().fromJson(loadResourceAsString("artist_lookup.json"), Artist.class);
        checkArtistAssertions(testArtist, artist);
    }

    @Test
    public void testReleaseModel() {
        Release testRelease = getTestRelease();
        Release release = new Gson().fromJson(loadResourceAsString("release_lookup.json"), Release.class);
        checkReleaseAssertions(testRelease, release);
    }

}
