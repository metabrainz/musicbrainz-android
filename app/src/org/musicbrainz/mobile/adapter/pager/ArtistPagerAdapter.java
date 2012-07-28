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
import android.widget.ScrollView;

import com.viewpagerindicator.TitleProvider;

public class ArtistPagerAdapter extends PagerAdapter implements TitleProvider {

    private final Activity context;
    
    private RelativeLayout links;
    private RelativeLayout releases;
    private ScrollView edit;
    private boolean viewsAdded;

    public ArtistPagerAdapter(Context context) {
        this.context = (Activity) context;
        inflateViews();
    }
    
    private void inflateViews() {
        links = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.layout_links, null);
        releases = (RelativeLayout) context.getLayoutInflater().inflate(R.layout.layout_artist_release_groups,null);
        edit = (ScrollView) context.getLayoutInflater().inflate(R.layout.layout_edit, null);
    }

    @Override
    public String getTitle(int position) {
        Resources res = context.getResources();
        switch (position) {
        case 0:
            return res.getString(R.string.tab_links);
        case 1:
            return res.getString(R.string.tab_releases);
        case 2:
            return res.getString(R.string.tab_edits);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        
        if (!viewsAdded) {
            ((ViewPager) container).addView(links, 0);
            ((ViewPager) container).addView(releases, 0);
            ((ViewPager) container).addView(edit, 0);
            viewsAdded = true;
        }

        switch (position) {
        case 0:
            return links;
        case 1:
            return releases;
        case 2:
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