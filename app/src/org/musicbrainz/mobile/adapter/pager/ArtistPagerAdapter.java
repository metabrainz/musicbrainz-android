package org.musicbrainz.mobile.adapter.pager;

import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.fragment.ArtistBioFragment;
import org.musicbrainz.mobile.fragment.ArtistReleaseGroupsFragment;
import org.musicbrainz.mobile.fragment.EditFragment;
import org.musicbrainz.mobile.fragment.LinksFragment;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ArtistPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TITLES = { R.string.tab_links, R.string.tab_releases, R.string.tab_bio,
            R.string.tab_edits };

    public ArtistPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            return LinksFragment.newInstance();
        case 1:
            return ArtistReleaseGroupsFragment.newInstance();
        case 2:
            return ArtistBioFragment.newInstance();
        case 3:
            return EditFragment.newInstance(Entity.ARTIST);
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
