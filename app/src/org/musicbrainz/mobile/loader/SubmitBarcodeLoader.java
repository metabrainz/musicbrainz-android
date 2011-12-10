package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.android.api.webservice.WebClient;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class SubmitBarcodeLoader extends AsyncTaskLoader<AsyncResult<Void>> {
    
    private Credentials creds;
    private String mbid;
    private String barcode;

    public SubmitBarcodeLoader(Context context, Credentials creds, String mbid, String barcode) {
        super(context);
        this.creds = creds;
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
            WebClient client = new WebClient(creds);
            client.submitBarcode(mbid, barcode);
            return new AsyncResult<Void>(LoaderStatus.SUCCESS);
        } catch (IOException e) {
            return new AsyncResult<Void>(LoaderStatus.EXCEPTION, e);
        }
    }

}
