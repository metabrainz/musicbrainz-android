package org.metabrainz.mobile.presentation.features.tagger

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.ItemMetadataChangeBinding

class TagFieldsAdapter(private val tagFields: List<TagField>) :
        RecyclerView.Adapter<TagFieldsAdapter.TagFieldViewHolder>() {

    class TagFieldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemMetadataChangeBinding = ItemMetadataChangeBinding.bind(itemView)
        fun bind(item: TagField) {
            binding.tagName.text = item.tagName
            binding.originalValue.text = item.originalValue
            binding.newValue.setText(item.newValue)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagFieldViewHolder {
        val layoutInflater = parent.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return TagFieldViewHolder(layoutInflater.inflate(R.layout.item_metadata_change,
                parent, false))
    }

    override fun onBindViewHolder(holder: TagFieldViewHolder, position: Int) = holder.bind(tagFields[position])

    override fun getItemCount(): Int = tagFields.size
}