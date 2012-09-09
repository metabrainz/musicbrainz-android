package org.musicbrainz.mobile.adapter.pager;

import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.viewpagerindicator.TitleProvider;

public class ReleasePagerAdapter extends PagerAdapter implements TitleProvider {

    private final Activity context;
    
    private RelativeLayout tracks;
    private ScrollView edit;
    private boolean viewsAdded;

    public ReleasePagerAdapter(Context context) {
        this.context = (Activity) context;
        inflateViews();
    }
    
    private void inflateViews() {
        tracks = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.layout_tracklist, null);
        edit = (ScrollView) context.getLayoutInflater().inflate(R.layout.layout_edit, null);
    }

    @Override
    public String getTitle(int position) {
        Resources res = context.getResources();
        switch (position) {
        case 0:
            return res.getString(R.string.tab_tracks);
        case 1:
            return res.getString(R.string.tab_edits);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        
        if (!viewsAdded) {
            ((ViewPager) container).addView(tracks, 0);
            ((ViewPager) container).addView(edit, 0);
            viewsAdded = true;
        }
        
        switch (position) {
        case 0:
            return tracks;
        case 1:
            return edit;
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {}

    @Override
    public void startUpdate(View container) {}

    @Override
    public void finishUpdate(View container) {}

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}
    
}
