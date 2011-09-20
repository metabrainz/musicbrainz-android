/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.android.api.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.musicbrainz.android.api.data.Artist;
import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.android.api.data.ReleaseStub;

/**
 * Makes the web service available to Activity classes. The XML returned is
 * parsed into pojos with SAX.
 */
public class WebService {
	
	protected DefaultHttpClient httpClient;
	protected ResponseParser responseParser;
	
	public WebService() {
		httpClient = HttpClientFactory.getClient();
		responseParser = new ResponseParser();
	}
	
	public Release lookupReleaseFromBarcode(String barcode) throws IOException {
		String url = QueryBuilder.barcodeSearch(barcode); 
		InputStream response = get(url);
		String barcodeMbid = responseParser.parseMbidFromBarcode(response);
		if (barcodeMbid == null) {
			throw new BarcodeNotFoundException(barcode);
		}
		return lookupRelease(barcodeMbid);
	}
	
	public Release lookupRelease(String mbid) throws IOException {
		String url = QueryBuilder.releaseLookup(mbid);
		InputStream response = get(url);
		return responseParser.parseRelease(response);
	}
	
	public LinkedList<ReleaseStub> browseReleases(String mbid) throws IOException {
		String url = QueryBuilder.releaseGroupReleaseBrowse(mbid);
		InputStream response = get(url);
		return responseParser.parseReleaseGroupReleases(response);
	}
	
	public Artist lookupArtist(String mbid) throws IOException {
		String artistUrl = QueryBuilder.artistLookup(mbid);
		InputStream artistResponse = get(artistUrl);
		Artist artist = responseParser.parseArtist(artistResponse);
		
		String rgUrl = QueryBuilder.artistReleaseGroupBrowse(mbid);
		InputStream rgResponse = get(rgUrl);
		ArrayList<ReleaseGroupStub> releases = responseParser.parseReleaseGroupBrowse(rgResponse);
		
		artist.setReleaseGroups(releases);
		return artist;
	}
	
	public LinkedList<ArtistStub> searchArtists(String searchTerm) throws IOException {
		String url = QueryBuilder.artistSearch(searchTerm);
		InputStream response = get(url);
		return responseParser.parseArtistSearch(response);
	}
	
	public LinkedList<ReleaseGroupStub> searchReleaseGroup(String searchTerm) throws IOException {
		String url = QueryBuilder.releaseGroupSearch(searchTerm);
		InputStream response = get(url);
		return responseParser.parseReleaseGroupSearch(response);
	}
	
	public LinkedList<ReleaseStub> searchRelease(String searchTerm) throws IOException {
		String url = QueryBuilder.releaseSearch(searchTerm);
		InputStream response = get(url);
		return responseParser.parseReleaseSearch(response);
	}
	
	public Collection<String> lookupTags(MBEntity type, String mbid) throws IOException {
		String url = QueryBuilder.tagLookup(type, mbid);
		InputStream response = get(url);
		return responseParser.parseTagLookup(response);
	}
	
	public float lookupRating(MBEntity type, String mbid) throws IOException  {
		String url = QueryBuilder.ratingLookup(type, mbid);
		InputStream response = get(url);
		return responseParser.parseRatingLookup(response);
	}
	
	protected InputStream get(String url) throws IOException {
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept", "application/xml");
		HttpResponse response = httpClient.execute(get);
		return response.getEntity().getContent();
	}
	
}
