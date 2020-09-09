package org.metabrainz.mobile.presentation.features.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.metabrainz.mobile.data.sources.api.entities.WikiSummary
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist
import org.metabrainz.mobile.databinding.FragmentBioBinding
import org.metabrainz.mobile.presentation.features.base.MusicBrainzFragment
import org.metabrainz.mobile.util.Resource

class ArtistBioFragment : Fragment() {
    private var binding: FragmentBioBinding? = null

    private val artistViewModel: ArtistViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBioBinding.inflate(inflater, container, false)
        artistViewModel.data.observe(viewLifecycleOwner, { setArtistInfo(it) })
        artistViewModel.wikiData.observe(viewLifecycleOwner, { setWiki(it) })
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setWiki(wikiSummaryResource: Resource<WikiSummary>?) {
        if (wikiSummaryResource != null && wikiSummaryResource.status == Resource.Status.SUCCESS) {
            val wiki = wikiSummaryResource.data
            val wikiText = wiki.extract
            if (wikiText != null && wikiText.isNotEmpty()) {
                showWikiCard()
                binding!!.cardArtistWiki.wikiSummary.text = wikiText
            } else hideWikiCard()
        } else hideWikiCard()
    }

    private fun showWikiCard() {
        binding!!.cardArtistWiki.root.visibility = View.VISIBLE
    }

    private fun hideWikiCard() {
        binding!!.cardArtistWiki.root.visibility = View.GONE
    }

    private fun setArtistInfo(resource: Resource<Artist>) {
        if (resource.status == Resource.Status.SUCCESS) {
            val artist = resource.data

            if (artist.type != null && artist.type.isNotEmpty())
                binding!!.cardArtistInfo.artistType.text = artist.type
            if (artist.gender != null && artist.gender.isNotEmpty())
                binding!!.cardArtistInfo.artistGender.text = artist.gender
            if (artist.area != null && artist.area.name != null)
                binding!!.cardArtistInfo.artistArea.text = artist.area.name
            if (artist.lifeSpan != null && artist.lifeSpan.timePeriod != null)
                binding!!.cardArtistInfo.lifeSpan.text = artist.lifeSpan.timePeriod
        }
    }

    companion object : MusicBrainzFragment {
        override fun newInstance(): Fragment {
            return ArtistBioFragment()
        }
    }

}