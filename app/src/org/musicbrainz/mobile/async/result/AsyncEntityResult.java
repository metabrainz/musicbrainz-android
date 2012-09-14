package org.musicbrainz.mobile.async.result;

import org.musicbrainz.android.api.data.UserData;

public class AsyncEntityResult<T> extends AsyncResult<T>{
    
    protected final UserData userData;
    
    public AsyncEntityResult(LoaderStatus status, T data) {
        super(status, data);
        this.userData = null;
    }
    
    public AsyncEntityResult(LoaderStatus status, T data, UserData userData) {
        super(status, data);
        this.userData = userData;
    }
    
    public AsyncEntityResult(LoaderStatus status, Throwable exception) {
        super(status, exception);
        this.userData = null;
    }

    public UserData getUserData() {
        return userData;
    }
    
    public boolean hasUserData() {
        return userData != null;
    }

}
