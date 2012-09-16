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

public class ArtistPagerAdapter extends FragmentPagerAdapter {

    public ArtistPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = App.getContext().getResources();
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
    public Fragment getItem(int position) {
        switch(position) {
        case 0:
            return LinksFragment.newInstance();
        case 1:
            return ArtistReleaseGroupsFragment.newInstance();
        case 2:
            return EditFragment.newInstance(Entity.ARTIST);
        }
        return null;
    }
    
    @Override
    public int getCount() {
        return 3;
    }
    
}