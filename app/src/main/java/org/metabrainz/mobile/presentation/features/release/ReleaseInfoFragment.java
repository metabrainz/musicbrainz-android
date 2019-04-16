package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;

public class ReleaseInfoFragment extends Fragment {

    private TextView releaseTitle, releaseBarcode, releaseLanguage, releaseFormat;
    private ReleaseViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CoverArtSlideshowAdapter slideshowAdapter;
    private ArrayList<String> urls = new ArrayList<>();

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
        slideshowAdapter = new CoverArtSlideshowAdapter(urls);
        findViews(view);
        viewPager.setAdapter(slideshowAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void findViews(View view) {
        releaseTitle = view.findViewById(R.id.release_title);
        releaseBarcode = view.findViewById(R.id.release_barcode);
        releaseFormat = view.findViewById(R.id.format);
        releaseLanguage = view.findViewById(R.id.release_language);
        viewPager = view.findViewById(R.id.viewpager_slideshow);
        tabLayout = view.findViewById(R.id.tab_indicator);
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
            urls.clear();
            urls.addAll(coverArt.getAllImageLinks());
            slideshowAdapter.notifyDataSetChanged();
            startAutoSlide();
        }
    }

    private void startAutoSlide() {
        int NUM_PAGES = urls.size();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int position = viewPager.getCurrentItem();
                if (position == NUM_PAGES - 1) position = 0;
                else position++;
                viewPager.setCurrentItem(position, true);
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }

}
