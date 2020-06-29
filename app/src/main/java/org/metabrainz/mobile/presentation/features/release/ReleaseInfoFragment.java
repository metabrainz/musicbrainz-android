package org.metabrainz.mobile.presentation.features.release;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.databinding.CardReleaseInfoBinding;

import java.util.ArrayList;

public class ReleaseInfoFragment extends Fragment {

    private CardReleaseInfoBinding binding;

    private ReleaseViewModel viewModel;
    private CoverArtSlideshowAdapter slideshowAdapter;
    private final ArrayList<String> urls = new ArrayList<>();

    public static ReleaseInfoFragment newInstance() {
        return new ReleaseInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CardReleaseInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReleaseViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), this::setData);
        viewModel.fetchCoverArt().observe(getViewLifecycleOwner(), this::setCoverArt);
        slideshowAdapter = new CoverArtSlideshowAdapter(urls);
        binding.slideshow.viewpagerSlideshow.setAdapter(slideshowAdapter);
        binding.slideshow.tabIndicator.setupWithViewPager(binding.slideshow.viewpagerSlideshow);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setData(Release release) {
        if (release != null) {
            String title, barcode, status = "", language = "";
            title = release.getTitle();

            barcode = release.getBarcode();
            if (release.getMedia() != null && !release.getMedia().isEmpty())
                status = release.getStatus();

            if (release.getTextRepresentation() != null)
                language = release.getTextRepresentation().getLanguage();

            if (title != null && !title.isEmpty()) binding.releaseTitle.setText(title);
            if (barcode != null && !barcode.isEmpty()) binding.releaseBarcode.setText(barcode);
            if (status != null && !status.isEmpty()) binding.releaseStatus.setText(status);
            if (language != null && !language.isEmpty()) binding.releaseLanguage.setText(language);

        }
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
                if (binding != null) {
                    int position = binding.slideshow.viewpagerSlideshow.getCurrentItem();
                    if (position == NUM_PAGES - 1) position = 0;
                    else position++;
                    binding.slideshow.viewpagerSlideshow.setCurrentItem(position, true);
                    handler.postDelayed(this, 10000);
                }
            }
        };
        handler.postDelayed(runnable, 10000);
    }

}
