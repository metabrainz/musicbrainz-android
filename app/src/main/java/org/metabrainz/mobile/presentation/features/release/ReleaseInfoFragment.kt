package org.metabrainz.mobile.presentation.features.release

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import org.metabrainz.mobile.data.sources.api.entities.CoverArt
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.databinding.CardReleaseInfoBinding
import org.metabrainz.mobile.presentation.features.fragments.MusicBrainzFragment
import org.metabrainz.mobile.util.Resource
import java.util.*

class ReleaseInfoFragment : Fragment() {
    private var binding: CardReleaseInfoBinding? = null
    private val viewModel: ReleaseViewModel by activityViewModels()

    private lateinit var slideshowAdapter: CoverArtSlideshowAdapter
    private val urls = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CardReleaseInfoBinding.inflate(inflater, container, false)
        viewModel.data.observe(viewLifecycleOwner) { setData(it) }
        viewModel.coverArtData.observe(viewLifecycleOwner) { setCoverArt(it) }
        slideshowAdapter = CoverArtSlideshowAdapter(urls)

        binding!!.slideshow.viewpagerSlideshow.adapter = slideshowAdapter
        TabLayoutMediator(binding!!.slideshow.tabIndicator, binding!!.slideshow.viewpagerSlideshow) { _, _ -> }.attach()
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setData(resource: Resource<Release>) {
        if (resource.status == Resource.Status.SUCCESS) {
            val release = resource.data
            if (release.title != null && release.title.isNotEmpty())
                binding!!.releaseTitle.text = release.title
            if (release.barcode != null && release.barcode.isNotEmpty())
                binding!!.releaseBarcode.text = release.barcode
            if (release.status != null && release.status.isNotEmpty())
                binding!!.releaseStatus.text = release.status
            if (release.textRepresentation != null && release.textRepresentation.language != null)
                binding!!.releaseLanguage.text = release.textRepresentation.language
        }
    }

    private fun setCoverArt(coverArt: CoverArt?) {
        if (coverArt != null && coverArt.images != null && coverArt.images.isNotEmpty()) {
            urls.clear()
            urls.addAll(coverArt.allThumbnailsLinks)
            slideshowAdapter.notifyDataSetChanged()
            startAutoSlide()
        }
    }

    private fun startAutoSlide() {
        val NUM_PAGES = urls.size
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (binding != null) {
                    var position = binding!!.slideshow.viewpagerSlideshow.currentItem
                    if (position == NUM_PAGES - 1) position = 0 else position++
                    binding!!.slideshow.viewpagerSlideshow.setCurrentItem(position, true)
                    handler.postDelayed(this, 10000)
                }
            }
        }
        handler.postDelayed(runnable, 10000)
    }

    companion object : MusicBrainzFragment {
        override fun newInstance(): Fragment {
            return ReleaseInfoFragment()
        }
    }
}