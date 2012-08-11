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

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.string.StringMapper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmBarcodeDialog extends DialogFragment implements OnClickListener {

    public static final String TAG = "confirm_barcode";

    private TextView title;
    private TextView artist;
    private TextView numberOfTracks;
    private TextView formats;
    private TextView labels;
    private TextView releaseDate;
    private TextView country;
    private Button confirm;

    private ConfirmBarcodeCallbacks callbacks;

    public interface ConfirmBarcodeCallbacks {
        public ReleaseStub getCurrentSelection();
        public void confirmSubmission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.barcode_add_header);
        getDialog().setCanceledOnTouchOutside(false);
        View layout = inflater.inflate(R.layout.dialog_barcode_submit, container, false);
        findViews(layout);
        confirm.setOnClickListener(this);
        return layout;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        try {
            callbacks = (ConfirmBarcodeCallbacks) getFragmentManager().findFragmentById(R.id.barcode_search_fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement " + ConfirmBarcodeCallbacks.class.getSimpleName());
        }
        setViews(callbacks.getCurrentSelection());
    }

    private void findViews(View layout) {
        title = (TextView) layout.findViewById(R.id.list_release_title);
        artist = (TextView) layout.findViewById(R.id.list_release_artist);
        numberOfTracks = (TextView) layout.findViewById(R.id.list_release_tracksnum);
        formats = (TextView) layout.findViewById(R.id.list_release_formats);
        labels = (TextView) layout.findViewById(R.id.list_release_labels);
        releaseDate = (TextView) layout.findViewById(R.id.list_release_date);
        country = (TextView) layout.findViewById(R.id.list_release_country);
        confirm = (Button) layout.findViewById(R.id.barcode_confirm);
    }

    private void setViews(ReleaseStub stub) {
        title.setText(stub.getTitle());
        artist.setText(StringFormat.commaSeparateArtists(stub.getArtists()));
        numberOfTracks.setText(stub.getTracksNum() + " " + getString(R.string.label_tracks));
        formats.setText(StringMapper.buildReleaseFormatsString(MusicBrainzApp.getContext(), stub.getFormats()));
        labels.setText(StringFormat.commaSeparate(stub.getLabels()));
        releaseDate.setText(stub.getDate());
        country.setText(stub.getCountryCode());
    }

    @Override
    public void onClick(View v) {
        callbacks.confirmSubmission();
        dismiss();
    }

}
