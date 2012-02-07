/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.data.Label;
import org.musicbrainz.android.api.data.LabelSearchStub;
import org.musicbrainz.android.api.data.Recording;
import org.musicbrainz.android.api.data.RecordingSearchStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.util.Credentials;

/**
 * Makes the web service available for Activity classes. Calls are blocking and
 * should be made inside AsyncTask. The XML that is returned gets parsed into
 * pojos with SAX.
 */
public class MusicBrainzWebClient implements MusicBrainz {

    private static final String AUTH_REALM = "musicbrainz.org";
    private static final String AUTH_SCOPE = "musicbrainz.org";
    private static final int AUTH_PORT = 80;
    private static final String AUTH_TYPE = "Digest";

    private AbstractHttpClient httpClient;
    private ResponseParser responseParser;
    private String clientId;

    public MusicBrainzWebClient(String userAgent) {
        httpClient = HttpClient.getClient(userAgent);
        responseParser = new ResponseParser();
    }

    public MusicBrainzWebClient(Credentials creds) {
        httpClient = HttpClient.getClient(creds.getUserAgent());
        responseParser = new ResponseParser();
        setCredentials(creds.getUsername(), creds.getPassword());
        this.clientId = creds.getClientId();
    }

    @Override
    public void setCredentials(String username, String password) {
        AuthScope authScope = new AuthScope(AUTH_SCOPE, AUTH_PORT, AUTH_REALM, AUTH_TYPE);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        httpClient.getCredentialsProvider().setCredentials(authScope, credentials);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Release lookupReleaseUsingBarcode(String barcode) throws IOException {
        HttpEntity entity = get(QueryBuilder.barcodeSearch(barcode));
        String barcodeMbid = responseParser.parseMbidFromBarcode(entity.getContent());
        entity.consumeContent();
        if (barcodeMbid == null) {
            throw new BarcodeNotFoundException(barcode);
        }
        return lookupRelease(barcodeMbid);
    }

    @Override
    public Release lookupRelease(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.releaseLookup(mbid));
        Release release = responseParser.parseRelease(entity.getContent());
        entity.consumeContent();
        return release;
    }

    @Override
    public LinkedList<ReleaseStub> browseReleases(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.releaseGroupReleaseBrowse(mbid));
        LinkedList<ReleaseStub> releases = responseParser.parseReleaseGroupReleases(entity.getContent());
        entity.consumeContent();
        return releases;
    }

    @Override
    public Artist lookupArtist(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.artistLookup(mbid));
        Artist artist = responseParser.parseArtist(entity.getContent());
        entity.consumeContent();
        artist.setReleaseGroups(browseArtistReleaseGroups(mbid));
        return artist;
    }

    private ArrayList<ReleaseGroupStub> browseArtistReleaseGroups(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.artistReleaseGroupBrowse(mbid, 0));
        ArrayList<ReleaseGroupStub> releases = responseParser.parseReleaseGroupBrowse(entity.getContent());
        entity.consumeContent();
        return releases;
    }

    @Override
    public ReleaseGroup lookupReleaseGroup(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.releaseGroupLookup(mbid));
        ReleaseGroup rg = responseParser.parseReleaseGroupLookup(entity.getContent());
        entity.consumeContent();
        return rg;
    }

    @Override
    public Label lookupLabel(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.labelLookup(mbid));
        Label label = responseParser.parseLabel(entity.getContent());
        entity.consumeContent();
        return label;
    }

    @Override
    public Recording lookupRecording(String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.recordingLookup(mbid));
        Recording recording = responseParser.parseRecording(entity.getContent());
        entity.consumeContent();
        return recording;
    }

    @Override
    public LinkedList<ArtistSearchStub> searchArtist(String searchTerm) throws IOException {
        HttpEntity entity = get(QueryBuilder.artistSearch(searchTerm));
        LinkedList<ArtistSearchStub> artists = responseParser.parseArtistSearch(entity.getContent());
        entity.consumeContent();
        return artists;
    }

    @Override
    public LinkedList<ReleaseGroupStub> searchReleaseGroup(String searchTerm) throws IOException {
        HttpEntity entity = get(QueryBuilder.releaseGroupSearch(searchTerm));
        LinkedList<ReleaseGroupStub> releaseGroups = responseParser.parseReleaseGroupSearch(entity.getContent());
        entity.consumeContent();
        return releaseGroups;
    }

    @Override
    public LinkedList<ReleaseStub> searchRelease(String searchTerm) throws IOException {
        HttpEntity entity = get(QueryBuilder.releaseSearch(searchTerm));
        LinkedList<ReleaseStub> releases = responseParser.parseReleaseSearch(entity.getContent());
        entity.consumeContent();
        return releases;
    }

    @Override
    public LinkedList<LabelSearchStub> searchLabel(String searchTerm) throws IOException {
        HttpEntity entity = get(QueryBuilder.labelSearch(searchTerm));
        LinkedList<LabelSearchStub> labels = responseParser.parseLabelSearch(entity.getContent());
        entity.consumeContent();
        return labels;
    }

    @Override
    public LinkedList<RecordingSearchStub> searchRecording(String searchTerm) throws IOException {
        HttpEntity entity = get(QueryBuilder.recordingSearch(searchTerm));
        LinkedList<RecordingSearchStub> recordings = responseParser.parseRecordingSearch(entity.getContent());
        entity.consumeContent();
        return recordings;
    }

    @Override
    public LinkedList<Tag> lookupTags(Entity type, String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.tagLookup(type, mbid));
        LinkedList<Tag> tags = responseParser.parseTagLookup(entity.getContent());
        entity.consumeContent();
        Collections.sort(tags);
        return tags;
    }

    @Override
    public float lookupRating(Entity type, String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.ratingLookup(type, mbid));
        float rating = responseParser.parseRatingLookup(entity.getContent());
        entity.consumeContent();
        return rating;
    }

    @Override
    public boolean autenticateUserCredentials() throws IOException {
        HttpGet authenticationTest = new HttpGet(QueryBuilder.authenticationCheck());
        authenticationTest.setHeader("Accept", "application/xml");
        try {
            httpClient.execute(authenticationTest, new BasicResponseHandler());
        } catch (HttpResponseException e) {
            return false;
        }
        return true;
    }

    @Override
    public UserData lookupUserData(Entity entityType, String mbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.userData(entityType, mbid));
        UserData userData = responseParser.parseUserData(entity.getContent());
        entity.consumeContent();
        return userData;
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
    public LinkedList<EditorCollectionStub> lookupUserCollections() throws IOException {
        HttpEntity entity = get(QueryBuilder.collectionList());
        LinkedList<EditorCollectionStub> collections = responseParser.parseCollectionListLookup(entity.getContent());
        entity.consumeContent();
        return collections;
    }

    @Override
    public EditorCollection lookupCollection(String collectionMbid) throws IOException {
        HttpEntity entity = get(QueryBuilder.collectionLookup(collectionMbid));
        EditorCollection collection = responseParser.parseCollectionLookup(entity.getContent());
        entity.consumeContent();
        return collection;
    }

    private HttpEntity get(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/xml");
        HttpResponse response = httpClient.execute(get);
        return response.getEntity();
    }

    private void post(String url, String content) throws IOException {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/xml; charset=UTF-8");
        StringEntity xml = new StringEntity(content, "UTF-8");
        post.setEntity(xml);

        HttpResponse response = httpClient.execute(post);
        if (response != null) {
            response.getEntity().consumeContent();
        }
    }

    private void delete(String url) throws IOException {
        HttpDelete delete = new HttpDelete(url);
        HttpResponse response = httpClient.execute(delete);
        if (response != null) {
            response.getEntity().consumeContent();
        }
    }

    private void put(String url) throws IOException {
        HttpPut put = new HttpPut(url);
        HttpResponse response = httpClient.execute(put);
        if (response != null) {
            response.getEntity().consumeContent();
        }
    }

}
