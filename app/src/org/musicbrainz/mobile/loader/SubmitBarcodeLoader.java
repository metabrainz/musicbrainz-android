package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class SubmitBarcodeLoader extends AsyncTaskLoader<AsyncResult<Void>> {

    private String mbid;
    private String barcode;

    public SubmitBarcodeLoader(String mbid, String barcode) {
        super(MusicBrainzApp.getContext());
        this.mbid = mbid;
        this.barcode = barcode;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public AsyncResult<Void> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(MusicBrainzApp.getCredentials());
            client.submitBarcode(mbid, barcode);
            return new AsyncResult<Void>(LoaderStatus.SUCCESS);
        } catch (IOException e) {
            return new AsyncResult<Void>(LoaderStatus.EXCEPTION, e);
        }
    }
}
