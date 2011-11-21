package org.musicbrainz.mobile.task;

import org.musicbrainz.mobile.activity.base.DataQueryActivity;

public abstract class MusicBrainzTask extends IgnitedAsyncTask<DataQueryActivity, String, Void, Void> {

    String userAgent;
    
    public MusicBrainzTask() {
        super();
    }
    
    public MusicBrainzTask(DataQueryActivity activity) {
        super(activity);
    }
    
    @Override
    protected void onStart(DataQueryActivity activity) {
        userAgent = activity.getUserAgent();
    }
    
    @Override
    protected void onSuccess(DataQueryActivity activity, Void v) {
        completed(activity);
    }

    @Override
    protected void onError(DataQueryActivity activity, Exception e) {
        completed(activity);
    }
    
    private void completed(DataQueryActivity activity) {
        if (activity != null) {
            activity.onTaskFinished();
        }
    }
    
}
