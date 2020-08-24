package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.simplecityapps.ktaglib.AudioFile
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTaggerBinding

@AndroidEntryPoint
class TaggerFragment : Fragment() {

    internal var adapter: ExpandableListAdapter? = null
    private lateinit var binding: FragmentTaggerBinding
    private val viewModel : TaggerViewModel by activityViewModels()

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

        viewModel.taglibFetchedMetadata.observe(viewLifecycleOwner, this::setTaglibTags)
        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner, this::setServerTags)
        return binding.root
    }

    private fun setTaglibTags(metadata: AudioFile?) {
        if (metadata == null)
            return
        val size = "${"%.2f".format((metadata.size / 1024f / 1024f))} MB"
        binding.dropdownItems1.title.text = metadata.title
        binding.dropdownItems1.track.text = metadata.track.toString()
        binding.dropdownItems1.disc.text = metadata.disc.toString()
        binding.dropdownItems1.duration.text = metadata.duration.toString()
        binding.dropdownItems1.artist.text = metadata.artist.toString()
        binding.dropdownItems1.album.text = metadata.disc.toString()
        binding.dropdownItems1.year.text = metadata.date.toString()
        binding.dropdownItems1.disc.text = metadata.disc.toString()
        binding.dropdownItems1.size.text = size
    }

    private fun setServerTags(metadata: AudioFile?){
        if (metadata == null)
            return
        binding.dropdownItems2.title.setText(metadata.title)
        binding.dropdownItems2.track.setText(metadata.track.toString())
        binding.dropdownItems2.disc.setText(metadata.disc.toString())
        binding.dropdownItems2.duration.setText(metadata.duration.toString())
        binding.dropdownItems2.artist.setText(metadata.artist.toString())
        binding.dropdownItems2.album.setText(metadata.disc.toString())
        binding.dropdownItems2.year.setText(metadata.date.toString())
        binding.dropdownItems2.disc.setText(metadata.disc.toString())
    }
}