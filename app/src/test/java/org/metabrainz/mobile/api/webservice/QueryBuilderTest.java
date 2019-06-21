package org.metabrainz.mobile.test.webservice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {

    @Test
    public void artistLookup() {
        String expected = "http://metabrainz.org/ws/2/artist/b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d?inc=url-rels+artist-rels+label-rels+tags+ratings";
        String actual = QueryBuilder.artistLookup("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d");
        assertEquals(expected, actual);
    }

    @Test
    public void artistReleaseGroupBrowse() {
        String expected = "http://metabrainz.org/ws/2/release-group?artist=b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d&limit=100&offset=0";
        String actual = QueryBuilder.artistReleaseGroupBrowse("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", 0);
        assertEquals(expected, actual);
    }

    @Test
    public void artistSearch() {
        String expected = "http://metabrainz.org/ws/2/artist?query=owen";
        String actual = QueryBuilder.artistSearch("owen");
        assertEquals(expected, actual);
    }

    @Test
    public void barcodeSearch() {
        String expected = "http://metabrainz.org/ws/2/release/?query=barcode:792258106329&limit=1";
        String actual = QueryBuilder.barcodeSearch("792258106329");
        assertEquals(expected, actual);
    }

    @Test
    public void ratingLookup() {
        String expected = "http://metabrainz.org/ws/2/artist/b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d?inc=ratings";
        String actual = QueryBuilder.ratingLookup(Entity.ARTIST, "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d");
        assertEquals(expected, actual);
    }

    @Test
    public void releaseGroupReleaseBrowse() {
        String expected = "http://metabrainz.org/ws/2/release?release-group=dca03435-8adb-30a5-ba82-5a162267ff38&inc=artist-credits+labels+mediums&limit=100";
        String actual = QueryBuilder.releaseGroupReleaseBrowse("dca03435-8adb-30a5-ba82-5a162267ff38");
        assertEquals(expected, actual);
    }

    @Test
    public void releaseGroupSearch() {
        String expected = "http://metabrainz.org/ws/2/release-group?query=songs+about+leaving";
        String actual = QueryBuilder.releaseGroupSearch("songs about leaving");
        assertEquals(expected, actual);
    }

    @Test
    public void releaseGroupLookup() {
        String expected = "http://metabrainz.org/ws/2/release-group/60089b39-412b-326c-afc7-aaa47113d84f?inc=artist-credits+tags+ratings+url-rels";
        String actual = QueryBuilder.releaseGroupLookup("60089b39-412b-326c-afc7-aaa47113d84f");
        assertEquals(expected, actual);
    }

    @Test
    public void releaseLookup() {
        String expected = "http://metabrainz.org/ws/2/release/2d9f9aac-1884-3939-a3b7-01437151e495?inc=release-groups+artists+recordings+labels+tags+ratings+url-rels+artist-rels";
        String actual = QueryBuilder.releaseLookup("2d9f9aac-1884-3939-a3b7-01437151e495");
        assertEquals(expected, actual);
    }

    @Test
    public void releaseSearch() {
        String expected = "http://metabrainz.org/ws/2/release?query=songs+about+leaving";
        String actual = QueryBuilder.releaseSearch("songs about leaving");
        assertEquals(expected, actual);
    }

    @Test
    public void tagLookup() {
        String expected = "http://metabrainz.org/ws/2/artist/b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d?inc=tags";
        String actual = QueryBuilder.tagLookup(Entity.ARTIST, "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d");
        assertEquals(expected, actual);
    }

    @Test
    public void labelSearch() {
        String expected = "http://metabrainz.org/ws/2/label?query=count+your+lucky+stars";
        String actual = QueryBuilder.labelSearch("count your lucky stars");
        assertEquals(expected, actual);
    }

    @Test
    public void labelLookup() {
        String expected = "http://metabrainz.org/ws/2/label/a4f904e0-f048-4c13-88ec-f9f31f3e6109?inc=tags+ratings+url-rels";
        String actual = QueryBuilder.labelLookup("a4f904e0-f048-4c13-88ec-f9f31f3e6109");
        assertEquals(expected, actual);
    }

    @Test
    public void recordingSearch() {
        String expected = "http://metabrainz.org/ws/2/recording?query=pleaser";
        String actual = QueryBuilder.recordingSearch("pleaser");
        assertEquals(expected, actual);
    }

    @Test
    public void recordingLookuo() {
        String expected = "http://metabrainz.org/ws/2/recording/470d06f8-6c0c-443d-b521-4c4eed9f0e7e?inc=artists+tags+ratings";
        String actual = QueryBuilder.recordingLookup("470d06f8-6c0c-443d-b521-4c4eed9f0e7e");
        assertEquals(expected, actual);
    }

    @Test
    public void collectionListLookup() {
        String expected = "http://metabrainz.org/ws/2/collection?limit=100";
        String actual = QueryBuilder.collectionList();
        assertEquals(expected, actual);
    }

    @Test
    public void collectionLookup() {
        String expected = "http://metabrainz.org/ws/2/collection/c6f9fb72-e233-47f4-a2f6-19f16442d93a/releases?inc=releases+artist-credits&limit=100";
        String actual = QueryBuilder.collectionLookup("c6f9fb72-e233-47f4-a2f6-19f16442d93a");
        assertEquals(expected, actual);
    }

    @Test
    public void collectionEdit() {
        String expected = "http://metabrainz.org/ws/2/collection/c6f9fb72-e233-47f4-a2f6-19f16442d93a/releases/455641ea-fff4-49f6-8fb4-49f961d8f1ad;?client=test-client-0.1";
        String actual = QueryBuilder.collectionEdit("c6f9fb72-e233-47f4-a2f6-19f16442d93a",
                "455641ea-fff4-49f6-8fb4-49f961d8f1ad", "test-client-0.1");
        assertEquals(expected, actual);
    }

}
