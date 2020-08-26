package org.metabrainz.mobile.presentation.features.tagger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTagger2Binding
import java.util.concurrent.TimeUnit

class TaggerFragment2 : Fragment() {
    private lateinit var binding:FragmentTagger2Binding
    private val viewmodel:TaggerViewModel by activityViewModels()
    private val tagsList = ArrayList<TagField>()
    private val tagFieldsAdapter = TagFieldsAdapter(tagsList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTagger2Binding.inflate(inflater)

        viewmodel.taglibFetchedMetadata.observe(viewLifecycleOwner, Observer {
            metadata -> setTaglibfetchedMetadata(metadata)
        })

        viewmodel.serverFetchedMetadata.observe(viewLifecycleOwner, Observer {
            tagsList.clear()
            tagsList.addAll(it)
            tagFieldsAdapter.notifyDataSetChanged()
            setServerFetchedMetadata()
        })
        return binding.root
    }

    fun setTaglibfetchedMetadata(metadata:HashMap<String,String>){
        binding.root.findViewById<TextView>(R.id.title).text = metadata["TITLE"]
        binding.root.findViewById<TextView>(R.id.title2).text = metadata["TITLE"]

        binding.root.findViewById<TextView>(R.id.track).text = metadata["TRACK"]
        binding.root.findViewById<TextView>(R.id.track2).text = metadata["TRACK"]

        binding.root.findViewById<TextView>(R.id.disc).text = metadata["DISC"]
        binding.root.findViewById<TextView>(R.id.disc2).text = metadata["DISC"]

        binding.root.findViewById<TextView>(R.id.duration).text = metadata["DURATION"]?.toInt()?.toHms()
        binding.root.findViewById<TextView>(R.id.duration2).text = metadata["DURATION"]?.toInt()?.toHms()

        binding.root.findViewById<TextView>(R.id.artist).text = metadata["ARTIST"]
        binding.root.findViewById<TextView>(R.id.artist2).text = metadata["ARTIST"]

        binding.root.findViewById<TextView>(R.id.album).text = metadata["ALBUM"]
        binding.root.findViewById<TextView>(R.id.album2).text = metadata["ALBUM"]

        binding.root.findViewById<TextView>(R.id.year).text = metadata["DATE"]
        binding.root.findViewById<TextView>(R.id.year2).text = metadata["DATE"]
       // Log.i("taglibtags",metadata["TITLE"]+"--"+metadata["TRACK"]+"--"+metadata["DISC"]+"--"+metadata["DURATION"]+"--"+metadata["ARTIST"]+"--"+metadata["ALBUM"]+"--"+metadata["DATE"])
    }

    fun setServerFetchedMetadata(){
        binding.loadingAnimation.visibility = View.GONE
        binding.tagDiff.visibility = View.VISIBLE
        binding.overwriteTagsButton.visibility = View.VISIBLE
        binding.tagsList.visibility = View.VISIBLE
        for(tags in tagsList){
            when(tags.tagName){
                "TITLE" -> binding.root.findViewById<TextView>(R.id.title2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                "ARTIST" ->  binding.root.findViewById<TextView>(R.id.artist2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                "ALBUM" -> binding.root.findViewById<TextView>(R.id.album2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                "TRACKNUMBER" -> binding.root.findViewById<TextView>(R.id.track2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                //"discnumber" -> binding.root.findViewById<TextView>(R.id.disc2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                "DATE" -> binding.root.findViewById<TextView>(R.id.year2).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                "MUSICBRAINZ_TRACKID" -> binding.root.findViewById<TextView>(R.id.MBID).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
                //"acoustid_id" -> binding.root.findViewById<TextView>(R.id.AccuosticID).text = if(tags.newValue.isNotEmpty()) tags.newValue else tags.originalValue
            }
        }

    }

    fun Int.toHms(defaultValue: String? = null): String {
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