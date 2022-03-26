package org.metabrainz.android.presentation.features.userprofile

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.metabrainz.android.presentation.features.userprofile.collections_section.CollectionsFragment
import org.metabrainz.android.presentation.features.userprofile.profile_section.ProfileFragment
import org.metabrainz.android.presentation.features.userprofile.ratings_section.RatingsFragment
import org.metabrainz.android.presentation.features.userprofile.subscribers_section.SubscribersFragment
import org.metabrainz.android.presentation.features.userprofile.subscriptions_section.SubscriptionsFragment
import org.metabrainz.android.presentation.features.userprofile.tags_section.TagsFragment

class ProfileViewPagerAdapter(appCompatActivity: AppCompatActivity): FragmentStateAdapter(appCompatActivity) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ProfileFragment()
            1-> SubscriptionsFragment()
            2-> SubscribersFragment()
            3-> CollectionsFragment()
            4-> TagsFragment()
            else -> RatingsFragment()
        }
    }

}