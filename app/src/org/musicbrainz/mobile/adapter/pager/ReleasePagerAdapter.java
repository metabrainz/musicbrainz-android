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

public class ReleasePagerAdapter extends FragmentPagerAdapter {

    public ReleasePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = App.getContext().getResources();
        switch (position) {
        case 0:
            return res.getString(R.string.tab_tracks);
        case 1:
            return res.getString(R.string.tab_edits);
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            return TracksFragment.newInstance();
        case 1:
            return EditFragment.newInstance(Entity.RELEASE_GROUP);
        }
        return null;
    }
    
    @Override
    public int getCount() {
        return 2;
    }
    
}
