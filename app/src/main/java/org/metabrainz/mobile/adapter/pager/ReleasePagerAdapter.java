package org.metabrainz.mobile.adapter.pager;

import android.content.res.Resources;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.webservice.Entity;
import org.metabrainz.mobile.fragment.CoverArtFragment;
import org.metabrainz.mobile.fragment.EditFragment;
import org.metabrainz.mobile.fragment.TracksFragment;

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
