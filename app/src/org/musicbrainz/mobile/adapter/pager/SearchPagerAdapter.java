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

public class SearchPagerAdapter extends PagerAdapter {

    private final Activity context;

    public SearchPagerAdapter(Context context) {
        this.context = (Activity) context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = context.getResources();
        switch (position) {
        case 0:
            return res.getString(R.string.results_artists);
        case 1:
            return res.getString(R.string.results_releases);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        switch (position) {
        case 0:
            RelativeLayout artists = (RelativeLayout) context.getLayoutInflater().inflate(
                    R.layout.layout_search_artists, null);
            ((ViewPager) container).addView(artists, 0);
            return artists;
        case 1:
            RelativeLayout releases = (RelativeLayout) context.getLayoutInflater().inflate(
                    R.layout.layout_search_release_groups, null);
            ((ViewPager) container).addView(releases, 0);
            return releases;
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
    public void destroyItem(View container, int position, Object object) {
    }

    @Override
    public void startUpdate(View container) {
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

}
