package org.musicbrainz.mobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class ContextFragment extends Fragment {

    protected Context context;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }
    
}
