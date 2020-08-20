package org.metabrainz.mobile.presentation.features.tagger
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.simplecityapps.ktaglib.AudioFile
//import java.util.concurrent.TimeUnit
//
//class AudioFilesAdapter : RecyclerView.Adapter<ViewHolder>() {
//
//    val data: MutableList<Pair<AudioFile, Document>> = mutableListOf()
//
//    fun clear() {
//        data.clear()
//        notifyDataSetChanged()
//    }
//
//    fun addItem(item: Pair<AudioFile, Document>) {
//        data.add(item)
//        notifyItemInserted(data.size - 1)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_document, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val (audioFile, document) = data[position]
//
//        holder.documentNameTextView.text = document.displayName
//        holder.titleTextView.text = audioFile.title
//        holder.trackTextView.text = audioFile.track.toString()
//        holder.durationTextView.text = audioFile.duration?.toHms()
//        holder.artistTextView.text = audioFile.artist
//        holder.albumTextView.text = audioFile.album
//        holder.yearTextView.text = audioFile.date
//        holder.discTextView.text = audioFile.disc.toString()
//        holder.mimeTypeTextView.text = document.mimeType
//        holder.sizeTextView.text = "${"%.2f".format((audioFile.size / 1024f / 1024f))}MB"
//    }
//}
//
//class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val documentNameTextView: TextView = itemView.findViewById(R.id.documentName)
//    val titleTextView: TextView = itemView.findViewById(R.id.title)
//    val trackTextView: TextView = itemView.findViewById(R.id.track)
//    val durationTextView: TextView = itemView.findViewById(R.id.duration)
//    val artistTextView: TextView = itemView.findViewById(R.id.artist)
//    val albumTextView: TextView = itemView.findViewById(R.id.album)
//    val yearTextView: TextView = itemView.findViewById(R.id.year)
//    val discTextView: TextView = itemView.findViewById(R.id.disc)
//    val mimeTypeTextView: TextView = itemView.findViewById(R.id.mimeType)
//    val sizeTextView: TextView = itemView.findViewById(R.id.size)
//}
//
///**
// * @param defaultValue returned if specified, and the duration being converted is 0 seconds.
// */
//fun Int.toHms(defaultValue: String? = null): String {
//
//    if (this == 0 && defaultValue != null) {
//        return defaultValue;
//    }
//
//    val hours = TimeUnit.MILLISECONDS.toHours(this.toLong())
//    val minutes = TimeUnit.MILLISECONDS.toMinutes(this.toLong()) % TimeUnit.HOURS.toMinutes(1)
//    val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong()) % TimeUnit.MINUTES.toSeconds(1)
//
//    return if (hours == 0L) {
//        String.format("%2d:%02d", minutes, seconds)
//    } else {
//        String.format("%2d:%02d:%02d", hours, minutes, seconds)
//    }
//}