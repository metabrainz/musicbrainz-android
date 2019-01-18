package org.metabrainz.android.fragment;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

public class ContextFragment extends Fragment {

    protected Context context;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }
    
}
