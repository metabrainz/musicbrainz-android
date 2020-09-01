package org.metabrainz.mobile.presentation.features.tagger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.metabrainz.mobile.databinding.ListItemDocumentBinding
import java.util.concurrent.TimeUnit

class DocumentAdapter(private val itemClickListener: OnItemCLickListener) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {
    val data: MutableList<Pair<AudioFile, Document>> = mutableListOf()

    class ViewHolder(val binding: ListItemDocumentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(metadata: Pair<AudioFile, Document>, cLickListener: OnItemCLickListener) {
            val (audioFile, document) = metadata
            binding.documentName.text = document.displayName
            binding.title.text = audioFile.title
            binding.track.text = audioFile.track.toString()
            binding.duration.text = audioFile.duration?.toHms()
            binding.artist.text = audioFile.artist
            binding.album.text = audioFile.album
            binding.year.text = audioFile.date
            binding.disc.text = audioFile.disc.toString()
            binding.mimeType.text = document.mimeType
            binding.size.text = "${"%.2f".format((audioFile.size / 1024f / 1024f))} MB"
            itemView.setOnClickListener {
                cLickListener.onItemClicked(metadata.first, metadata.second.uri)
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

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: Pair<AudioFile, Document>) {
        data.add(item)
        notifyItemChanged(data.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemDocumentBinding.inflate(LayoutInflater.from(parent.context),
                parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.bind(currentItem, itemClickListener)
    }

}