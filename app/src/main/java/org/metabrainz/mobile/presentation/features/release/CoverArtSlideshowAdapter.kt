package org.metabrainz.mobile.presentation.features.release

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.metabrainz.mobile.databinding.CoverArtSlideshowItemBinding

class CoverArtSlideshowAdapter(private val data: List<String>)
    : RecyclerView.Adapter<CoverArtSlideshowAdapter.CoverArtViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverArtViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = CoverArtSlideshowItemBinding.inflate(inflater, parent, false)
        return CoverArtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoverArtViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    class CoverArtViewHolder(private val binding: CoverArtSlideshowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: String) {
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(Uri.parse(uri)).into(binding.releaseCoverArt)
        }
    }

}