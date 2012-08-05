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
    public abstract void setCredentials(String username, String password);
    public abstract boolean autenticateUserCredentials() throws IOException;
    
    /*
     * Search
     */
    public abstract List<ArtistSearchStub> searchArtist(String searchTerm) throws IOException;
    public abstract List<ReleaseStub> searchRelease(String searchTerm) throws IOException;
    public abstract List<ReleaseGroupStub> searchReleaseGroup(String searchTerm) throws IOException;
    public abstract List<LabelSearchStub> searchLabel(String searchTerm) throws IOException;
    public abstract List<RecordingStub> searchRecording(String searchTerm) throws IOException;

    /*
     * Lookup
     */
    public abstract Artist lookupArtist(String mbid) throws IOException;
    public abstract Release lookupRelease(String mbid) throws IOException;
    public abstract Release lookupReleaseUsingBarcode(String barcode) throws IOException;
    public abstract Label lookupLabel(String mbid) throws IOException;
    public abstract Recording lookupRecording(String mbid) throws IOException;
    public abstract ReleaseGroup lookupReleaseGroup(String mbid) throws IOException;
    
    /*
     * User data lookup
     */
    public abstract List<Tag> lookupTags(Entity type, String mbid) throws IOException;
    public abstract float lookupRating(Entity type, String mbid) throws IOException;
    public abstract UserData lookupUserData(Entity entityType, String mbid) throws IOException;
    
    /*
     * Browse
     */
    public abstract List<ReleaseStub> browseReleases(String mbid) throws IOException;

    /*
     * Submission
     */
    public abstract void submitTags(Entity entityType, String mbid, Collection<String> tags) throws IOException;
    public abstract void submitRating(Entity entityType, String mbid, int rating) throws IOException;
    public abstract void submitBarcode(String mbid, String barcode) throws IOException;
    
    /*
     * Collection
     */
    public abstract void addReleaseToCollection(String collectionMbid, String releaseMbid) throws IOException;
    public abstract void deleteReleaseFromCollection(String collectionMbid, String releaseMbid) throws IOException;
    public abstract List<EditorCollectionStub> lookupUserCollections() throws IOException;
    public abstract EditorCollection lookupCollection(String mbid) throws IOException;

}