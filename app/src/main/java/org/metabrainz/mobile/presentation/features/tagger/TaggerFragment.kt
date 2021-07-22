package org.metabrainz.mobile.presentation.features.tagger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.databinding.FragmentTaggerBinding
import org.metabrainz.mobile.presentation.features.recording.RecordingActivity
import org.metabrainz.mobile.util.Log.d
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Utils
import java.util.concurrent.TimeUnit

class TaggerFragment : Fragment() {

    private lateinit var binding: FragmentTaggerBinding
    private val viewModel: TaggerViewModel by activityViewModels()
    private var recordingMBID: String? = null
    private var releaseMBID: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaggerBinding.inflate(inflater)

        viewModel.serverFetchedMetadata.observe(viewLifecycleOwner) {resource->
            //Making the required changes if  the results were found
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    setServerFetchedMetadata(resource.data!!)
                }
                Resource.Status.LOADING -> {
                    d("Loading the server data...")
                }
                Resource.Status.FAILED -> {
                    Toast.makeText(context,"Error fetching data from server!",LENGTH_SHORT).show()
                }
            }
        }

        viewModel.taglibFetchedMetadata.observe(viewLifecycleOwner) {
            setTaglibFetchedMetadata(it)
        }

        viewModel.serverCoverArt.observe(viewLifecycleOwner) { resource ->
            //Handling the status of the api call
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    Glide.with(this)
                        .load(resource.data!!.images[0].thumbnails.small)
                        .into(binding.albumArtServer)
                }
                Resource.Status.LOADING -> {
                    d("Loading the cover art...")
                }
                Resource.Status.FAILED -> {
                    Toast.makeText(context,"Error fetching cover art from server!",LENGTH_SHORT).show()
                }
            }
        }

        binding.overwriteTagsButton.setOnClickListener {
            saveMetadata()
        }

        binding.recordingButton.setOnClickListener {
            val intent = Intent(context, RecordingActivity::class.java)
            intent.putExtra(Constants.MBID, recordingMBID)
            context?.startActivity(intent)
        }
        binding.picard.setOnClickListener {
            Utils.sendToPicard(requireContext(),releaseMBID!!)
        }

        return binding.root
    }

    private fun setTaglibFetchedMetadata(metadata: AudioFile?) {
        binding.loadingAnimation.root.visibility = View.VISIBLE
        binding.originalValue.visibility = View.GONE
        binding.newValue.visibility = View.GONE
        binding.overwriteTagsButton.visibility = View.GONE
        binding.scrollView.visibility = View.GONE
        binding.AcoustID.visibility = View.GONE
        binding.AcoustIDHeading.visibility = View.GONE
        binding.MBID.visibility = View.GONE
        binding.MBIDHeading.visibility = View.GONE
        binding.albumArtLocal.visibility = View.GONE
        binding.albumArtServer.visibility = View.GONE
        binding.recordingButton.visibility = View.GONE

        reset()

        GlideApp.with(binding.albumArtLocal)
            .load(metadata)
            .into(binding.albumArtLocal)

        binding.title.setText(metadata!!.allProperties["TITLE"])
        binding.titleServer.setText(metadata.allProperties["TITLE"])

        binding.track.setText(metadata.allProperties["TRACK"])
        binding.trackServer.setText(metadata.allProperties["TRACK"])

        binding.disc.setText(metadata.allProperties["DISC"])
        binding.discServer.setText(metadata.allProperties["DISC"])

        binding.duration.setText(metadata.allProperties["DURATION"]?.toInt()?.toHms())
        binding.durationServer.setText(metadata.allProperties["DURATION"]?.toInt()?.toHms())

        binding.artist.setText(metadata.allProperties["ARTIST"])
        binding.artistServer.setText(metadata.allProperties["ARTIST"])

        binding.album.setText(metadata.allProperties["ALBUM"])
        binding.albumServer.setText(metadata.allProperties["ALBUM"])

        binding.year.setText(metadata.allProperties["DATE"])
        binding.yearServer.setText(metadata.allProperties["DATE"])
    }

    private fun setServerFetchedMetadata(tagsList: List<TagField>) {
        binding.loadingAnimation.root.visibility = View.GONE
        binding.originalValue.visibility = View.VISIBLE
        binding.newValue.visibility = View.VISIBLE
        binding.overwriteTagsButton.visibility = View.VISIBLE
        binding.albumArtLocal.visibility = View.VISIBLE
        binding.albumArtServer.visibility = View.VISIBLE
        binding.recordingButton.visibility = View.VISIBLE
        binding.scrollView.visibility = View.VISIBLE

        for (tags in tagsList) {
            if (tags.newValue.isEmpty()) {
                continue
            }

            when (tags.tagName) {
                "TITLE" -> binding.titleServer.setText(tags.newValue)
                "ARTIST" -> binding.artistServer.setText(tags.newValue)
                "ALBUM" -> binding.albumServer.setText(tags.newValue)
                "TRACKNUMBER" -> binding.trackServer.setText(tags.newValue)
//                "discnumber" -> binding.serverFetched.disc2.setText(tags.newValue)
                "DATE" -> binding.yearServer.setText(tags.newValue)
                "MUSICBRAINZ_TRACKID" -> {
                    binding.MBID.text = tags.newValue
                    binding.MBID.visibility = View.VISIBLE
                    binding.MBIDHeading.visibility = View.VISIBLE
                }
                "MUSICBRAINZ_RELEASEID"->{
                    releaseMBID = tags.newValue
                }
                "MUSICBRAINZ_RECORDINGID" ->{
                    recordingMBID = tags.newValue
                }
                "acoustid_id" -> {
                    binding.AcoustID.text = tags.newValue
                    binding.AcoustID.visibility = View.VISIBLE
                    binding.AcoustIDHeading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun reset(){
        binding.titleServer.setText("")
        binding.title.setText("")

        binding.track.setText("")
        binding.trackServer.setText("")

        binding.discServer.setText("")
        binding.disc.setText("")

        binding.duration.setText("")
        binding.durationServer.setText("")

        binding.artist.setText("")
        binding.artistServer.setText("")

        binding.album.setText("")
        binding.albumServer.setText("")

        binding.year.setText("")
        binding.yearServer.setText("")
    }

    private fun saveMetadata() {
        val newMetadata = HashMap<String, String>()
        if (binding.titleServer.text.toString().isNotEmpty())
            newMetadata["TITLE"] = binding.titleServer.text.toString()

        if (binding.trackServer.text.toString().isNotEmpty())
            newMetadata["TRACK"] = binding.trackServer.text.toString()

        if (binding.discServer.text.toString().isNotEmpty())
            newMetadata["DISC"] = binding.discServer.text.toString()

        if (binding.albumServer.text.toString().isNotEmpty())
            newMetadata["ALBUM"] = binding.albumServer.text.toString()

        if (binding.artistServer.text.toString().isNotEmpty())
            newMetadata["ARTIST"] = binding.artistServer.text.toString()

        if (binding.MBID.text.toString().isNotEmpty())
            newMetadata["MBID"] = binding.MBID.text.toString()

        if (binding.yearServer.text.toString().isNotEmpty())
            newMetadata["DATE"] = binding.yearServer.text.toString()

        if (viewModel.saveMetadataTags(newMetadata))
            Toast.makeText(activity, "Saved Tags Successfully!", LENGTH_SHORT).show()
        else
            Toast.makeText(activity, "Saving Tags Failed!", LENGTH_SHORT).show()
    }

    private fun Int.toHms(defaultValue: String? = null): String {
        if (this == 0 && defaultValue != null) {
            return defaultValue
        }
        val hours = TimeUnit.MILLISECONDS.toHours(this.toLong())
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % TimeUnit.HOURS.toMinutes(1)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % TimeUnit.MINUTES.toSeconds(1)

        return if (hours == 0L) {
            String.format("%2d:%02d", minutes, seconds)
        }
        else {
            String.format("%2d:%02d:%02d", hours, minutes, seconds)
        }
    }

}