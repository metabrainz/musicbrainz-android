package org.metabrainz.android.async.external;

import java.io.IOException;

import org.metabrainz.android.async.PersistingAsyncTaskLoader;
import org.metabrainz.android.async.external.result.ArtistBio;
import org.metabrainz.android.async.external.result.LastFmArtist;

import android.text.TextUtils;

public class ArtistBioLoader extends PersistingAsyncTaskLoader<ArtistBio> {

    private String mbid;
    private String wikiPageName;

    public ArtistBioLoader(String mbid, String wikiPageName) {
        this.mbid = mbid;
        this.wikiPageName = wikiPageName;
    }

    @Override
    public ArtistBio loadInBackground() {
        try {
            LastFmArtist lastfm = new LastFmClient().getArtistInfo(mbid);
            if (!TextUtils.isEmpty(wikiPageName)) {
                String wikiBio = new WikipediaClient().getArtistBio(wikiPageName);
                data = new ArtistBio(lastfm.image.get(4).text, lastfm.bio.full, wikiBio);
                return data;
            }
            data = new ArtistBio(lastfm.image.get(4).text, lastfm.bio.full);
            return data;
        } catch (IOException e) {
            return null;
        } catch (IllegalStateException ise) {
            return null;
        }
    }

}
