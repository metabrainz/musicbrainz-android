package org.metabrainz.mobile.presentation.features.userdata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag
import org.metabrainz.mobile.databinding.LayoutTagBinding

class TagAdapter(private val list: List<Tag>) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return TagViewHolder(LayoutTagBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = list[position]
        holder.bindView(tag)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class TagViewHolder(var binding: LayoutTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(tag: Tag) {
            binding.tagName.text = tag.name
        }
    }
}