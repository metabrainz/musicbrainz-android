package org.musicbrainz.mobile.async;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.async.result.AsyncResult;
import org.musicbrainz.mobile.async.result.LoaderStatus;

import android.support.v4.content.AsyncTaskLoader;

public class SubmitBarcodeLoader extends AsyncTaskLoader<AsyncResult<Void>> {

    private MusicBrainz client;
    private String mbid;
    private String barcode;

    public SubmitBarcodeLoader(String mbid, String barcode) {
        super(App.getContext());
        client = App.getWebClient();
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
            client.submitBarcode(mbid, barcode);
            return new AsyncResult<Void>(LoaderStatus.SUCCESS);
        } catch (IOException e) {
            return new AsyncResult<Void>(LoaderStatus.EXCEPTION, e);
        }
    }
}
