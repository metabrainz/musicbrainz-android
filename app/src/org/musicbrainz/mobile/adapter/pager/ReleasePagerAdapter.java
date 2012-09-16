package org.musicbrainz.mobile.adapter.pager;

import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.EditFragment;
import org.musicbrainz.mobile.fragment.TracksFragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

public class ReleasePagerAdapter extends FragmentPagerAdapter {

    private static final int[] TITLES = {R.string.tab_tracks, R.string.tab_edits};
    
    private SparseArray<Fragment> pageReferenceMap = new SparseArray<Fragment>();
    
    public ReleasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            TracksFragment tf = TracksFragment.newInstance();
            pageReferenceMap.put(position, tf);
            return tf;
        case 1:
            EditFragment ef = EditFragment.newInstance(Entity.RELEASE_GROUP);
            pageReferenceMap.put(position, ef);
            return ef;
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
    
    public Fragment getFragment(int position) {
        return pageReferenceMap.get(position);
    }
    
}
