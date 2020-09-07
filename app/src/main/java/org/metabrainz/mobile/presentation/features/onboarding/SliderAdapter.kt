package org.metabrainz.mobile.presentation.features.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.OnboardInstancePageBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SlideViewHolder>() {

    private val slides = arrayOf(
            Pair(R.drawable.ic_baseline_audiotrack_24, "Fix your Audio Metadata"),
            Pair(R.drawable.search_icon, "Explore MusicBrainz Data"),
            Pair(R.drawable.collection_icon, "Explore your MusicBrainz Collection"),
            Pair(R.drawable.scan_icon, "Scan using Barcodes")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = OnboardInstancePageBinding.inflate(inflater, parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) = holder.bind(slides[position])

    override fun getItemCount(): Int = slides.size

    class SlideViewHolder(private val binding: OnboardInstancePageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(slide: Pair<Int, String>) {
            binding.iconId.setImageResource(slide.first)
            binding.instructionId.text = slide.second
        }
    }

}