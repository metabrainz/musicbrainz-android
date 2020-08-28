package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.metabrainz.mobile.databinding.FragmentTagger2Binding
import java.util.concurrent.TimeUnit

class TaggerFragment2 : Fragment() {

    private lateinit var binding: FragmentTagger2Binding
    private val viewModel: TaggerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTagger2Binding.inflate(inflater)

        binding.originalValue.visibility = View.VISIBLE
        binding.newValue.visibility = View.VISIBLE
        binding.overwriteTagsButton.visibility = View.VISIBLE
        binding.serverFetched.root.visibility = View.VISIBLE
        binding.taglibFetched.root.visibility = View.VISIBLE

        viewModel.taglibFetchedMetadata.observe(viewLifecycleOwner) {
            setTaglibFetchedMetadata(it)
        }

        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner) {
            setServerFetchedMetadata(it)
        }

        return binding.root
    }

    private fun setTaglibFetchedMetadata(metadata: HashMap<String, String>) {
        binding.taglibFetched.title.text = metadata["TITLE"]
        binding.serverFetched.title.setText(metadata["TITLE"])

        binding.taglibFetched.track.text = metadata["TRACK"]
        binding.serverFetched.track.setText(metadata["TRACK"])

        binding.taglibFetched.disc.text = metadata["DISC"]
        binding.serverFetched.disc.setText(metadata["DISC"])

        binding.taglibFetched.duration.text = metadata["DURATION"]?.toInt()?.toHms()
        binding.serverFetched.duration.setText(metadata["DURATION"]?.toInt()?.toHms())

        binding.taglibFetched.artist.text = metadata["ARTIST"]
        binding.serverFetched.artist.setText(metadata["ARTIST"])

        binding.taglibFetched.album.text = metadata["ALBUM"]
        binding.serverFetched.album.setText(metadata["ALBUM"])

        binding.taglibFetched.year.text = metadata["DATE"]
        binding.serverFetched.year.setText(metadata["DATE"])
    }

    private fun setServerFetchedMetadata(tagsList: List<TagField>) {
        binding.loadingAnimation.root.visibility = View.GONE
        binding.originalValue.visibility = View.VISIBLE
        binding.newValue.visibility = View.VISIBLE
        binding.overwriteTagsButton.visibility = View.VISIBLE
        binding.serverFetched.root.visibility = View.VISIBLE
        binding.taglibFetched.root.visibility = View.VISIBLE

        for (tags in tagsList) {
            if (tags.newValue.isEmpty())
                continue

            when (tags.tagName) {
                "TITLE" -> binding.serverFetched.title.setText(tags.newValue)
                "ARTIST" -> binding.serverFetched.artist.setText(tags.newValue)
                "ALBUM" -> binding.serverFetched.album.setText(tags.newValue)
                "TRACKNUMBER" -> binding.serverFetched.track.setText(tags.newValue)
                //"discnumber" -> binding.serverFetched.disc2.setText(tags.newValue)
                "DATE" -> binding.serverFetched.year.setText(tags.newValue)
                "MUSICBRAINZ_TRACKID" -> binding.serverFetched.MBID.text = tags.newValue
                //"acoustid_id" -> binding.serverFetched.AccousticID.text = tags.newValue
            }
        }

    }

    private fun Int.toHms(defaultValue: String? = null): String {
        if (this == 0 && defaultValue != null)
            return defaultValue
        val hours = TimeUnit.MILLISECONDS.toHours(this.toLong())
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % TimeUnit.HOURS.toMinutes(1)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % TimeUnit.MINUTES.toSeconds(1)

        return if (hours == 0L) {
            String.format("%2d:%02d", minutes, seconds)
        } else {
            String.format("%2d:%02d:%02d", hours, minutes, seconds)
        }
    }

}