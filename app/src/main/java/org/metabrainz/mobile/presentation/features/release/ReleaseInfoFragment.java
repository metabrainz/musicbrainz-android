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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.ArrayList;

public class ReleaseInfoFragment extends Fragment {

    private TextView releaseTitle, releaseBarcode, releaseLanguage, releaseStatus;
    private ReleaseViewModel viewModel;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CoverArtSlideshowAdapter slideshowAdapter;
    private final ArrayList<String> urls = new ArrayList<>();

    public static ReleaseInfoFragment newInstance() {
        return new ReleaseInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_release_info, container, false);
        viewModel = new ViewModelProvider(this).get(ReleaseViewModel.class);
        viewModel.initializeData().observe(getViewLifecycleOwner(), this::setData);
        viewModel.initializeCoverArtData().observe(getViewLifecycleOwner(), this::setCoverArt);
        slideshowAdapter = new CoverArtSlideshowAdapter(urls);
        findViews(view);
        viewPager.setAdapter(slideshowAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void findViews(View view) {
        releaseTitle = view.findViewById(R.id.release_title);
        releaseBarcode = view.findViewById(R.id.release_barcode);
        releaseStatus = view.findViewById(R.id.release_status);
        releaseLanguage = view.findViewById(R.id.release_language);
        viewPager = view.findViewById(R.id.viewpager_slideshow);
        tabLayout = view.findViewById(R.id.tab_indicator);
    }

    private void setData(MBEntity entity) {
        if (entity instanceof Release) {
            Release release = (Release) entity;
            String title, barcode, status = "", language = "";
            title = release.getTitle();

            barcode = release.getBarcode();
            if (release.getMedia() != null && !release.getMedia().isEmpty())
                status = release.getStatus();

            if (release.getTextRepresentation() != null)
                language = release.getTextRepresentation().getLanguage();

            if (title != null && !title.isEmpty()) releaseTitle.setText(title);
            if (barcode != null && !barcode.isEmpty()) releaseBarcode.setText(barcode);
            if (status != null && !status.isEmpty()) releaseStatus.setText(status);
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
            urls.addAll(coverArt.getAllThumbnailsLinks());
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
