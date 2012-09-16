package org.musicbrainz.mobile.adapter.pager;

import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.ArtistReleaseGroupsFragment;
import org.musicbrainz.mobile.fragment.EditFragment;
import org.musicbrainz.mobile.fragment.LinksFragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

public class ArtistPagerAdapter extends FragmentPagerAdapter {
    
    private static final int[] TITLES = {R.string.tab_links, R.string.tab_releases, R.string.tab_edits};
    
    private SparseArray<Fragment> pageReferenceMap = new SparseArray<Fragment>();
    
    public ArtistPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
        case 0:
            LinksFragment lf = LinksFragment.newInstance();
            pageReferenceMap.put(position, lf);
            return lf;
        case 1:
            ArtistReleaseGroupsFragment rf = ArtistReleaseGroupsFragment.newInstance();
            pageReferenceMap.put(position, rf);
            return rf;
        case 2:
            EditFragment ef = EditFragment.newInstance(Entity.ARTIST);
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