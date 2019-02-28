package org.metabrainz.mobile.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.ReleaseSearchResult;
import org.metabrainz.mobile.string.StringFormat;
import org.metabrainz.mobile.string.StringMapper;

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
        ReleaseSearchResult getCurrentSelection();
        void confirmSubmission();
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
        title = layout.findViewById(R.id.list_release_title);
        artist = layout.findViewById(R.id.list_release_artist);
        numberOfTracks = layout.findViewById(R.id.list_release_tracksnum);
        formats = layout.findViewById(R.id.list_release_formats);
        labels = layout.findViewById(R.id.list_release_labels);
        releaseDate = layout.findViewById(R.id.list_release_date);
        country = layout.findViewById(R.id.list_release_country);
        confirm = layout.findViewById(R.id.barcode_confirm);
    }

    private void setViews(ReleaseSearchResult release) {
        title.setText(release.getTitle());
        artist.setText(StringFormat.commaSeparateArtists(release.getArtists()));
        numberOfTracks.setText(release.getTracksNum() + " " + getString(R.string.label_tracks));
        formats.setText(StringMapper.buildReleaseFormatsString(App.getContext(), release.getFormats()));
        labels.setText(StringFormat.commaSeparate(release.getLabels()));
        releaseDate.setText(release.getDate());
        country.setText(release.getCountryCode());
    }

    @Override
    public void onClick(View v) {
        callbacks.confirmSubmission();
        dismiss();
    }

}
