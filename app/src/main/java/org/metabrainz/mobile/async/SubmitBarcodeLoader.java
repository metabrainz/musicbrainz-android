package org.metabrainz.mobile.async;

import java.io.IOException;

import org.metabrainz.mobile.api.MusicBrainz;
import org.metabrainz.mobile.App;
import org.metabrainz.mobile.async.result.AsyncResult;
import org.metabrainz.mobile.async.result.LoaderStatus;

import androidx.loader.content.AsyncTaskLoader;

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
