package org.metabrainz.android.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.metabrainz.android.api.data.Artist;
import org.metabrainz.android.api.data.ArtistSearchResult;
import org.metabrainz.android.api.data.UserCollection;
import org.metabrainz.android.api.data.UserCollectionInfo;
import org.metabrainz.android.api.data.Label;
import org.metabrainz.android.api.data.LabelSearchResult;
import org.metabrainz.android.api.data.Recording;
import org.metabrainz.android.api.data.RecordingInfo;
import org.metabrainz.android.api.data.Release;
import org.metabrainz.android.api.data.ReleaseGroup;
import org.metabrainz.android.api.data.ReleaseGroupInfo;
import org.metabrainz.android.api.data.ReleaseInfo;
import org.metabrainz.android.api.data.Tag;
import org.metabrainz.android.api.data.UserData;
import org.metabrainz.android.api.webservice.Entity;

public interface MusicBrainz {

    /*
     * Authentication
     */
    void setCredentials(String username, String password);
    boolean autenticateCredentials() throws IOException;
    
    /*
     * Search
     */
    List<ArtistSearchResult> searchArtist(String searchTerm) throws IOException;
    List<ReleaseInfo> searchRelease(String searchTerm) throws IOException;
    List<ReleaseGroupInfo> searchReleaseGroup(String searchTerm) throws IOException;
    List<LabelSearchResult> searchLabel(String searchTerm) throws IOException;
    List<RecordingInfo> searchRecording(String searchTerm) throws IOException;

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
    List<ReleaseInfo> browseReleases(String mbid) throws IOException;

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
    List<UserCollectionInfo> lookupUserCollections() throws IOException;
    UserCollection lookupCollection(String mbid) throws IOException;

}