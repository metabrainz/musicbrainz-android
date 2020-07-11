package org.metabrainz.mobile.presentation.features.artist;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.metabrainz.mobile.App;
import org.metabrainz.mobile.R;
import org.metabrainz.mobile.presentation.features.links.LinksFragment;
import org.metabrainz.mobile.presentation.features.release_list.ReleaseListFragment;
import org.metabrainz.mobile.presentation.features.userdata.UserDataFragment;

class ArtistPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TITLES = {R.string.tab_releases, R.string.tab_bio, R.string.tab_links,
            R.string.tab_edits};

    ArtistPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ReleaseListFragment.newInstance();
            case 1:
                return ArtistBioFragment.newInstance();
            case 2:
                return LinksFragment.newInstance();
            case 3:
                return UserDataFragment.newInstance();
        }
        return ArtistBioFragment.newInstance();
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

}
