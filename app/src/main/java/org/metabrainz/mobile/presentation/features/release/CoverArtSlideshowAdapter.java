package org.metabrainz.mobile.presentation.features.release;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import org.metabrainz.mobile.databinding.CoverArtSlideshowItemBinding;

import java.util.List;

class CoverArtSlideshowAdapter extends PagerAdapter {

    private final List<String> data;

    CoverArtSlideshowAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CoverArtSlideshowItemBinding binding = CoverArtSlideshowItemBinding.inflate(inflater, container, false);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(Uri.parse(data.get(position))).into(binding.releaseCoverArt);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
