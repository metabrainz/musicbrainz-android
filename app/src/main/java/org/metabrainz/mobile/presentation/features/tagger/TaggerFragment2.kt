package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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

        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner) {
            setServerFetchedMetadata(it)
        }

        viewModel.taglibFetchedMetadata.observe(viewLifecycleOwner) {
            setTaglibFetchedMetadata(it)
        }


        binding.overwriteTagsButton.setOnClickListener { saveMetadata() }

        return binding.root
    }

    private fun setTaglibFetchedMetadata(metadata: HashMap<String, String>) {
        binding.loadingAnimation.root.visibility = View.VISIBLE
        binding.originalValue.visibility = View.GONE
        binding.newValue.visibility = View.GONE
        binding.overwriteTagsButton.visibility = View.GONE
        binding.serverFetched.root.visibility = View.GONE
        binding.taglibFetched.root.visibility = View.GONE

        reset()

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
        
        var countOfEmptyOccurrences = 0

        for (tags in tagsList) {
            if (tags.newValue.isEmpty()) {
                countOfEmptyOccurrences++
                continue
            }

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

    fun reset(){
        binding.taglibFetched.title.text = ""
        binding.serverFetched.title.setText("")

        binding.taglibFetched.track.text = ""
        binding.serverFetched.track.setText("")

        binding.taglibFetched.disc.text = ""
        binding.serverFetched.disc.setText("")

        binding.taglibFetched.duration.text = ""
        binding.serverFetched.duration.setText("")

        binding.taglibFetched.artist.text = ""
        binding.serverFetched.artist.setText("")

        binding.taglibFetched.album.text = ""
        binding.serverFetched.album.setText("")

        binding.taglibFetched.year.text = ""
        binding.serverFetched.year.setText("")

    }

    private fun saveMetadata() {
        val newMetadata = HashMap<String, String>()
        if (binding.serverFetched.title.text.toString().isNotEmpty())
            newMetadata["TITLE"] = binding.serverFetched.title.text.toString()

        if (binding.serverFetched.track.text.toString().isNotEmpty())
            newMetadata["TRACK"] = binding.serverFetched.track.text.toString()

        if (binding.serverFetched.disc.text.toString().isNotEmpty())
            newMetadata["DISC"] = binding.serverFetched.disc.text.toString()

        if (binding.serverFetched.album.text.toString().isNotEmpty())
            newMetadata["ALBUM"] = binding.serverFetched.album.text.toString()

        if (binding.serverFetched.artist.text.toString().isNotEmpty())
            newMetadata["ARTIST"] = binding.serverFetched.artist.text.toString()

        if (binding.serverFetched.MBID.text.toString().isNotEmpty())
            newMetadata["MBID"] = binding.serverFetched.MBID.text.toString()

        if (viewModel.saveMetadataTags(newMetadata))
            Toast.makeText(activity, "Saved Tags Successfully!", LENGTH_SHORT).show()
        else
            Toast.makeText(activity, "Saving Tags Failed!", LENGTH_SHORT).show()
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