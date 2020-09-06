package org.metabrainz.mobile.presentation.features.artist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.metabrainz.mobile.presentation.features.fragments.MusicBrainzFragment

class ArtistPagerAdapter(activity: FragmentActivity, private val fragments: List<MusicBrainzFragment>)
    : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].newInstance()
}