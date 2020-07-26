package org.metabrainz.mobile;

import org.metabrainz.mobile.data.sources.api.entities.WikiSummary;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import static org.junit.Assert.assertEquals;

public class AssertionUtils {

    public static void checkWikiAssertions(WikiSummary testSummary, WikiSummary summary) {
        assertEquals(testSummary.getExtract(), summary.getExtract());
    }

    public static void checkReleaseAssertions(Release testRelease, Release release) {
        assertEquals(testRelease, release);
        assertEquals(testRelease.getTitle(), release.getTitle());
        assertEquals(testRelease.getBarcode(), release.getBarcode());
        assertEquals(testRelease.getStatus(), release.getStatus());
        assertEquals(testRelease.getCountry(), release.getCountry());
        assertEquals(testRelease.getDisambiguation(), release.getDisambiguation());
        assertEquals(testRelease.getDate(), release.getDate());
    }

    public static void checkArtistAssertions(Artist testArtist, Artist artist) {
        assertEquals(artist, testArtist);
        assertEquals(testArtist.getCountry(), artist.getCountry());
        assertEquals(testArtist.getDisambiguation(), artist.getDisambiguation());
        assertEquals(testArtist.getName(), artist.getName());
        assertEquals(testArtist.getSortName(), artist.getSortName());
        assertEquals(testArtist.getGender(), artist.getGender());
    }

}
