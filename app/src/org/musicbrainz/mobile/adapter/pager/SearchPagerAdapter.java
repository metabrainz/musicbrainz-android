package org.musicbrainz.mobile.adapter.pager;

/*
 * Copyright (C) 2011 Jamie McDonald
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

import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.viewpagerindicator.TitleProvider;

public class SearchPagerAdapter extends PagerAdapter implements TitleProvider {

    private final Activity context;

    public SearchPagerAdapter(Context context) {
        this.context = (Activity) context;
    }

    @Override
    public String getTitle(int position) {
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
            RelativeLayout artists = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.layout_search_artists, null);
            ((ViewPager) container).addView(artists, 0);
            return artists;
        case 1:
            RelativeLayout releases = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.layout_search_rgs, null);
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
    public void destroyItem(View container, int position, Object object) {}

    @Override
    public void startUpdate(View container) {}

    @Override
    public void finishUpdate(View container) {}

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}
    
}
