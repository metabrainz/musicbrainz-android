package org.metabrainz.mobile.presentation.features.userdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag;
import org.metabrainz.mobile.databinding.LayoutTagBinding;

import java.util.List;

class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private final List<Tag> list;

    public TagAdapter(List<Tag> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new TagViewHolder(LayoutTagBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
        Tag tag = list.get(position);
        holder.bindView(tag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        LayoutTagBinding binding;

        TagViewHolder(LayoutTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(Tag tag) {
            binding.tagName.setText(tag.getName());
        }
    }
}
