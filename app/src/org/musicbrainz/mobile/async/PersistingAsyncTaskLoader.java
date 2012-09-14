package org.musicbrainz.mobile.async;

import org.musicbrainz.mobile.App;

import android.support.v4.content.AsyncTaskLoader;

public abstract class PersistingAsyncTaskLoader<D> extends AsyncTaskLoader<D> {

    protected D data;
    
    public PersistingAsyncTaskLoader() {
        super(App.getContext());
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }
        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }
    
    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            return;
        }
        this.data = data;
        super.deliverResult(data);
    }
    
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        data = null;
    }

}
