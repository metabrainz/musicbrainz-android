package org.musicbrainz.mobile.dialog;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseInfoAdapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Dialog that allows the user to choose a specific release when a release group
 * contains more than one release.
 */
public class ReleaseSelectionDialog extends DialogFragment implements OnItemClickListener {

    public static final String TAG = "release_selection_dialog";
    
    private ReleaseSelectionCallbacks callbacks;
    private List<ReleaseInfo> release;
    private ListView releaseList;
    
    public interface ReleaseSelectionCallbacks {
        List<ReleaseInfo> getReleasesInfo();
        void onReleaseSelected(String mbid);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (ReleaseSelectionCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + ReleaseSelectionCallbacks.class.getSimpleName());
        }
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        release = callbacks.getReleasesInfo();
        if (release != null) {
            releaseList.setAdapter(new ReleaseInfoAdapter(getActivity(), R.layout.list_release, release));
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.rg_title);
        getDialog().setCanceledOnTouchOutside(false);
        View layout = inflater.inflate(R.layout.dialog_release_group, container, false);
        setupList(layout);
        return layout;
    }
    
    private void setupList(View layout) {
        releaseList = (ListView) layout.findViewById(R.id.rg_release_list);
        releaseList.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        callbacks.onReleaseSelected(release.get(position).getReleaseMbid());
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }

}
