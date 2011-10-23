package org.musicbrainz.mobile.task;

import org.musicbrainz.mobile.activity.SearchActivity;

public abstract class SearchTask extends IgnitedAsyncTask<SearchActivity, String, Void, Void> {

    String userAgent;
    
    public SearchTask() {
        super();
    }
    
    public SearchTask(SearchActivity activity) {
        super(activity);
    }
    
    @Override
    protected void onStart(SearchActivity activity) {
        userAgent = activity.getUserAgent();
    }
    
    @Override
    protected void onSuccess(SearchActivity activity, Void v) {
        completed(activity);
    }

    @Override
    protected void onError(SearchActivity activity, Exception e) {
        completed(activity);
    }
    
    private void completed(SearchActivity activity) {
        if (activity != null) {
            activity.onSearchTaskFinished();
        }
    }
    
}
