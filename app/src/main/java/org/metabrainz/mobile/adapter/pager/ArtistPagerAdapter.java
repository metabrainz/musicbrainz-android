package org.metabrainz.mobile.adapter.pager;

import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.webservice.Entity;
import org.metabrainz.mobile.fragment.ArtistBioFragment;
import org.metabrainz.mobile.fragment.ArtistReleaseGroupsFragment;
import org.metabrainz.mobile.fragment.EditFragment;
import org.metabrainz.mobile.fragment.LinksFragment;

public class ArtistPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TITLES = {R.string.tab_links, R.string.tab_releases, R.string.tab_bio,
            R.string.tab_edits};

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
