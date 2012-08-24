/*
 * Copyright (C) 2012 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.dialog;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.adapter.list.ReleaseStubAdapter;

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
    private List<ReleaseStub> stubs;
    private ListView releaseList;
    
    public interface ReleaseSelectionCallbacks {
        List<ReleaseStub> getReleaseStubs();
        void onReleaseStubSelected(String mbid);
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
        stubs = callbacks.getReleaseStubs();
        if (stubs != null) {
            releaseList.setAdapter(new ReleaseStubAdapter(getActivity(), R.layout.list_release, stubs));
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
        callbacks.onReleaseStubSelected(stubs.get(position).getReleaseMbid());
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }

}
