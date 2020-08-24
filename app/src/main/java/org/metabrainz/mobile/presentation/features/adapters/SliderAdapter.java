package org.metabrainz.mobile.presentation.features.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.metabrainz.mobile.R;

public class SliderAdapter extends PagerAdapter {

    public final int[] slide_images = {
            R.drawable.ic_baseline_audiotrack_24,
            R.drawable.search_icon,
            R.drawable.collection_icon,
            R.drawable.scan_icon
    };
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public final String[] slide_desc = {
            "Fix your Audio Metadata",
            "Explore MusicBrainz Data",
            "Explore your MusicBrainz Collection",
            "Scan using Barcodes"
    };
    final Context context;

    @Override
    public int getCount() {
        return slide_desc.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.onbaord_instance_page,container,false);

        ImageView imageView=view.findViewById(R.id.icon_id);
        TextView textView=view.findViewById(R.id.instruction_id);

        imageView.setImageResource(slide_images[position]);
        textView.setText(slide_desc[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
