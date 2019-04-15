package org.metabrainz.mobile.presentation.features.release;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

public class ReleaseInfoFragment extends Fragment {

    private TextView releaseTitle, releaseBarcode, releaseLanguage, releaseFormat;
    private ImageView coverArtView;
    private ReleaseViewModel viewModel;

    public static ReleaseInfoFragment newInstance() {
        return new ReleaseInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_release_info, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(ReleaseViewModel.class);
        viewModel.initializeReleaseData().observe(this, this::setData);
        viewModel.initializeCoverArtData().observe(this, this::setCoverArt);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        releaseTitle = view.findViewById(R.id.release_title);
        releaseBarcode = view.findViewById(R.id.release_barcode);
        releaseFormat = view.findViewById(R.id.format);
        releaseLanguage = view.findViewById(R.id.release_language);
        coverArtView = view.findViewById(R.id.release_cover_art);
    }

    private void setData(Release release) {
        String title, barcode, format = "", language = "";
        if (release != null) {
            title = release.getTitle();

            barcode = release.getBarcode();
            if (release.getMedia() != null && !release.getMedia().isEmpty())
                format = release.getMedia().get(0).getFormat();

            if (release.getTextRepresentation() != null)
                language = release.getTextRepresentation().getLanguage();

            if (title != null && !title.isEmpty()) releaseTitle.setText(title);
            if (barcode != null && !barcode.isEmpty()) releaseBarcode.setText(barcode);
            if (format != null && !format.isEmpty()) releaseFormat.setText(format);
            if (language != null && !language.isEmpty()) releaseLanguage.setText(language);

            fetchCoverArt();
        }
    }

    private void fetchCoverArt() {
        viewModel.getCoverArtData();
    }

    private void setCoverArt(CoverArt coverArt) {
        if (coverArt != null && coverArt.getImages() != null && !coverArt.getImages().isEmpty()) {
            String uri = coverArt.getImages().get(0).getImage();
            if (uri != null && !uri.isEmpty())
                Picasso.get().load(Uri.parse(uri)).into(coverArtView);
        }
    }

}
