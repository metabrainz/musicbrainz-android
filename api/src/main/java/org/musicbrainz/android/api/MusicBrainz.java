package org.musicbrainz.android.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistSearchStub;
import org.musicbrainz.android.api.data.EditorCollection;
import org.musicbrainz.android.api.data.EditorCollectionStub;
import org.musicbrainz.android.api.data.Label;
import org.musicbrainz.android.api.data.LabelSearchStub;
import org.musicbrainz.android.api.data.Recording;
import org.musicbrainz.android.api.data.RecordingStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroup;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.data.Tag;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.Entity;

public interface MusicBrainz {

    /*
     * Authentication
     */
    void setCredentials(String username, String password);
    boolean autenticateCredentials() throws IOException;
    
    /*
     * Search
     */
    List<ArtistSearchStub> searchArtist(String searchTerm) throws IOException;
    List<ReleaseStub> searchRelease(String searchTerm) throws IOException;
    List<ReleaseGroupStub> searchReleaseGroup(String searchTerm) throws IOException;
    List<LabelSearchStub> searchLabel(String searchTerm) throws IOException;
    List<RecordingStub> searchRecording(String searchTerm) throws IOException;

    /*
     * Lookup
     */
    Artist lookupArtist(String mbid) throws IOException;
    Release lookupRelease(String mbid) throws IOException;
    Release lookupReleaseUsingBarcode(String barcode) throws IOException;
    Label lookupLabel(String mbid) throws IOException;
    Recording lookupRecording(String mbid) throws IOException;
    ReleaseGroup lookupReleaseGroup(String mbid) throws IOException;
    
    /*
     * User data lookup
     */
    List<Tag> lookupTags(Entity type, String mbid) throws IOException;
    float lookupRating(Entity type, String mbid) throws IOException;
    UserData lookupUserData(Entity entityType, String mbid) throws IOException;
    
    /*
     * Browse
     */
    List<ReleaseStub> browseReleases(String mbid) throws IOException;

    /*
     * Submission
     */
    void submitTags(Entity entityType, String mbid, Collection<String> tags) throws IOException;
    void submitRating(Entity entityType, String mbid, int rating) throws IOException;
    void submitBarcode(String mbid, String barcode) throws IOException;
    
    /*
     * Collection
     */
    void addReleaseToCollection(String collectionMbid, String releaseMbid) throws IOException;
    void deleteReleaseFromCollection(String collectionMbid, String releaseMbid) throws IOException;
    List<EditorCollectionStub> lookupUserCollections() throws IOException;
    EditorCollection lookupCollection(String mbid) throws IOException;

}