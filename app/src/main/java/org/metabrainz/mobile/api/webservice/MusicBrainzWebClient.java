package org.metabrainz.mobile.api.webservice;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.api.User;
import org.metabrainz.mobile.api.data.Artist;
import org.metabrainz.mobile.api.data.ArtistSearchResult;
import org.metabrainz.mobile.api.data.Label;
import org.metabrainz.mobile.api.data.LabelSearchResult;
import org.metabrainz.mobile.api.data.Recording;
import org.metabrainz.mobile.api.data.RecordingSearchResult;
import org.metabrainz.mobile.api.data.Release;
import org.metabrainz.mobile.api.data.ReleaseGroup;
import org.metabrainz.mobile.api.data.ReleaseGroupSearchResult;
import org.metabrainz.mobile.api.data.ReleaseSearchResult;
import org.metabrainz.mobile.api.data.Tag;
import org.metabrainz.mobile.api.data.UserCollection;
import org.metabrainz.mobile.api.data.UserData;
import org.metabrainz.mobile.api.data.UserSearchResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Makes the web service available for Activity classes. Calls are blocking and
 * should be made asynchronously. The XML returned is parsed into pojos with
 * SAX handlers.
 */
public class MusicBrainzWebClient implements MusicBrainz {

    private static final String AUTH_REALM = "metabrainz.org";
    private static final String AUTH_SCOPE = "metabrainz.org";
    private static final int AUTH_PORT = 80;
    private static final String AUTH_TYPE = "Digest";

    // private AbstractHttpClient httpClient;
    private ResponseParser responseParser;
    private String clientId;

    //TODO: Remove temporary default http client
    public MusicBrainzWebClient(String userAgent) {
    /*    httpClient = new DefaultHttpClient();
        responseParser = new ResponseParser();*/
    }

    public MusicBrainzWebClient(User user, String userAgent, String clientId) {
        /*httpClient = new DefaultHttpClient();
        responseParser = new ResponseParser();
        setCredentials(user.getUsername(), user.getPassword());
        this.clientId = clientId;*/
    }

    @Override
    public void setCredentials(String username, String password) {
        /*AuthScope authScope = new AuthScope(AUTH_SCOPE, AUTH_PORT, AUTH_REALM, AUTH_TYPE);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        httpClient.getCredentialsProvider().setCredentials(authScope, credentials);*/
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Release lookupReleaseUsingBarcode(String barcode) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.barcodeSearch(barcode));
        String barcodeMbid = responseParser.parseMbidFromBarcode(entity.getContent());
        entity.consumeContent();
        if (barcodeMbid == null) {
            throw new BarcodeNotFoundException(barcode);
        }
        return lookupRelease(barcodeMbid);*/
        return new Release();
    }

    @Override
    public Release lookupRelease(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.releaseLookup(mbid));
        Release release = responseParser.parseRelease(entity.getContent());
        entity.consumeContent();
        return release;*/
        return new Release();
    }

    @Override
    public LinkedList<ReleaseSearchResult> browseReleases(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.releaseGroupReleaseBrowse(mbid));
        LinkedList<ReleaseSearchResult> releases = responseParser.parseReleaseGroupReleases(entity.getContent());
        entity.consumeContent();
        Collections.sort(releases);
        return releases;*/
        return new LinkedList<>();
    }

    @Override
    public Artist lookupArtist(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.artistLookup(mbid));
        Artist artist = responseParser.parseArtist(entity.getContent());
        entity.consumeContent();
        artist.setReleaseGroups(browseArtistReleaseGroups(mbid));
        return artist;*/
        return new Artist();
    }

    private ArrayList<ReleaseGroupSearchResult> browseArtistReleaseGroups(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.artistReleaseGroupBrowse(mbid, 0));
        ArrayList<ReleaseGroupSearchResult> releases = responseParser.parseReleaseGroupBrowse(entity.getContent());
        entity.consumeContent();
        Collections.sort(releases);
        return releases;*/
        return new ArrayList<>();
    }

    @Override
    public ReleaseGroup lookupReleaseGroup(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.releaseGroupLookup(mbid));
        ReleaseGroup rg = responseParser.parseReleaseGroupLookup(entity.getContent());
        entity.consumeContent();
        return rg;*/
        return new ReleaseGroup();
    }

    @Override
    public Label lookupLabel(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.labelLookup(mbid));
        Label label = responseParser.parseLabel(entity.getContent());
        entity.consumeContent();
        return label;*/
        return new Label();
    }

    @Override
    public Recording lookupRecording(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.recordingLookup(mbid));
        Recording recording = responseParser.parseRecording(entity.getContent());
        entity.consumeContent();
        return recording;*/
        return new Recording();
    }

    @Override
    public LinkedList<ArtistSearchResult> searchArtist(String searchTerm) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.artistSearch(searchTerm));
        LinkedList<ArtistSearchResult> artists = responseParser.parseArtistSearch(entity.getContent());
        entity.consumeContent();
        return artists;*/
        return new LinkedList<>();
    }

    @Override
    public LinkedList<ReleaseGroupSearchResult> searchReleaseGroup(String searchTerm) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.releaseGroupSearch(searchTerm));
        LinkedList<ReleaseGroupSearchResult> releaseGroups = responseParser.parseReleaseGroupSearch(entity.getContent());
        entity.consumeContent();
        return releaseGroups;*/
        return new LinkedList<>();
    }

    @Override
    public LinkedList<ReleaseSearchResult> searchRelease(String searchTerm) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.releaseSearch(searchTerm));
        LinkedList<ReleaseSearchResult> releases = responseParser.parseReleaseSearch(entity.getContent());
        entity.consumeContent();
        return releases;*/
        return new LinkedList<>();
    }

    @Override
    public LinkedList<LabelSearchResult> searchLabel(String searchTerm) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.labelSearch(searchTerm));
        LinkedList<LabelSearchResult> labels = responseParser.parseLabelSearch(entity.getContent());
        entity.consumeContent();
        return labels;*/
        return new LinkedList<>();
    }

    @Override
    public LinkedList<RecordingSearchResult> searchRecording(String searchTerm) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.recordingSearch(searchTerm));
        LinkedList<RecordingSearchResult> recordings = responseParser.parseRecordingSearch(entity.getContent());
        entity.consumeContent();
        return recordings;*/
        return new LinkedList<>();
    }

    @Override
    public LinkedList<Tag> lookupTags(Entity type, String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.tagLookup(type, mbid));
        LinkedList<Tag> tags = responseParser.parseTagLookup(entity.getContent());
        entity.consumeContent();
        Collections.sort(tags);
        return tags;*/
        return new LinkedList<>();
    }

    @Override
    public float lookupRating(Entity type, String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.ratingLookup(type, mbid));
        float rating = responseParser.parseRatingLookup(entity.getContent());
        entity.consumeContent();
        return rating;*/
        return 0.0f;
    }

    @Override
    public boolean autenticateCredentials() throws IOException {
        URL authenticationTest = new URL(QueryBuilder.authenticationCheck());
        HttpURLConnection urlConnection = (HttpURLConnection) authenticationTest.openConnection();
        urlConnection.setRequestProperty("Accept", "application/xml");
        return true;
    }

    @Override
    public UserData lookupUserData(Entity entityType, String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.userData(entityType, mbid));
        UserData userData = responseParser.parseUserData(entity.getContent());
        entity.consumeContent();
        return userData;*/
        return new UserData();
    }

    @Override
    public void submitTags(Entity entityType, String mbid, Collection<String> tags) throws IOException {
        String url = QueryBuilder.tagSubmission(clientId);
        String content = XmlBuilder.buildTagSubmissionXML(entityType, mbid, tags);
        post(url, content);
    }

    @Override
    public void submitRating(Entity entityType, String mbid, int rating) throws IOException {
        String url = QueryBuilder.ratingSubmission(clientId);
        String content = XmlBuilder.buildRatingSubmissionXML(entityType, mbid, rating);
        post(url, content);
    }

    @Override
    public void submitBarcode(String mbid, String barcode) throws IOException {
        String url = QueryBuilder.barcodeSubmission(clientId);
        String content = XmlBuilder.buildBarcodeSubmissionXML(mbid, barcode);
        post(url, content);
    }

    @Override
    public void addReleaseToCollection(String collectionMbid, String releaseMbid) throws IOException {
        put(QueryBuilder.collectionEdit(collectionMbid, releaseMbid, clientId));
    }

    @Override
    public void deleteReleaseFromCollection(String collectionMbid, String releaseMbid) throws IOException {
        delete(QueryBuilder.collectionEdit(collectionMbid, releaseMbid, clientId));
    }

    @Override
    public LinkedList<UserSearchResult> lookupUserCollections() throws IOException {
        /*HttpEntity entity = get(QueryBuilder.collectionList());
        LinkedList<UserSearchResult> collections = responseParser.parseCollectionListLookup(entity.getContent());
        entity.consumeContent();
        Collections.sort(collections);
        return collections;*/
        return new LinkedList<>();
    }

    @Override
    public UserCollection lookupCollection(String mbid) throws IOException {
        /*HttpEntity entity = get(QueryBuilder.collectionLookup(mbid));
        UserCollection collection = responseParser.parseCollectionLookup(entity.getContent());
        entity.consumeContent();
        return collection;*/
        return new UserCollection();
    }

    /*private HttpEntity get(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/xml");
        HttpResponse response = httpClient.execute(get);
        return response.getEntity();
    }*/

    private void post(String url, String content) throws IOException {
       /* HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/xml; charset=UTF-8");
        StringEntity xml = new StringEntity(content, "UTF-8");
        post.setEntity(xml);
        HttpResponse response = httpClient.execute(post);
        response.getEntity().consumeContent();*/
    }

    private void delete(String url) throws IOException {
        /*HttpDelete delete = new HttpDelete(url);
        HttpResponse response = httpClient.execute(delete);
        response.getEntity().consumeContent();*/
    }

    private void put(String url) throws IOException {
        /*URL put = new URL(url);
        HttpResponse response = httpClient.execute(put);
        response.getEntity().consumeContent();*/
    }

}
