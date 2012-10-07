package org.musicbrainz.mobile.adapter.pager;

import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.CoverArtFragment;
import org.musicbrainz.mobile.fragment.EditFragment;
import org.musicbrainz.mobile.fragment.TracksFragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ReleasePagerAdapter extends FragmentPagerAdapter {

    private static final int[] TITLES = {R.string.tab_tracks, R.string.tab_cover_art, R.string.tab_edits};
    
    public ReleasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            return TracksFragment.newInstance();
        case 1:
            return CoverArtFragment.newInstance();
        case 2:
            return EditFragment.newInstance(Entity.RELEASE_GROUP);
        }
        return null;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = App.getContext().getResources();
        return res.getString(TITLES[position]);
    }
    
    @Override
    public int getCount() {
        return TITLES.length;
    }
    
    public String makeTag(int position) {
        return "android:switcher:" + R.id.pager + ":" + position;
    }
    
}
