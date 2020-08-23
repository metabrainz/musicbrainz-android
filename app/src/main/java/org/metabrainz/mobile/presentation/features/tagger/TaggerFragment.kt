package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.simplecityapps.ktaglib.AudioFile
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTaggerBinding
import org.metabrainz.mobile.databinding.ListItemDocumentBinding

@AndroidEntryPoint
class TaggerFragment : Fragment() {

    internal var adapter: ExpandableListAdapter? = null
    private lateinit var binding: FragmentTaggerBinding
    private val viewModel : KotlinTaggerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTaggerBinding.inflate(inflater)

        binding.cardView1.setOnClickListener {
            if (binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cardView1, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility = View.VISIBLE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            } else {
                TransitionManager.beginDelayedTransition(binding.cardView1, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility = View.GONE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }

        binding.cardView2.setOnClickListener {
            if (binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility = View.VISIBLE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            } else {
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility = View.GONE
                binding.arrowBtn2.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }

        viewModel.taglibFetchedMetadata.observe(viewLifecycleOwner, { setTags(it, binding.dropdownItems1) })
        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner, { setTags(it, binding.dropdownItems2) })
        return binding.root
    }

    private fun setTags(metadata: AudioFile?, detailsViewBinding : ListItemDocumentBinding) {
        val size = if (metadata?.size == null) "null" else "${"%.2f".format((metadata.size / 1024f / 1024f))}MB"
        detailsViewBinding.title.text = metadata?.title
        detailsViewBinding.track.text = metadata?.track.toString()
        detailsViewBinding.disc.text = metadata?.disc.toString()
        detailsViewBinding.duration.text = metadata?.duration.toString()
        detailsViewBinding.artist.text = metadata?.artist.toString()
        detailsViewBinding.album.text = metadata?.disc.toString()
        detailsViewBinding.year.text = metadata?.date.toString()
        detailsViewBinding.disc.text = metadata?.disc.toString()
        detailsViewBinding.size.text = size
    }
}