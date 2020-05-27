package org.metabrainz.mobile.presentation.features.userdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.userdata.UserTag;
import org.metabrainz.mobile.databinding.LayoutTagBinding;

import java.util.List;

class UserTagAdapter extends RecyclerView.Adapter<UserTagAdapter.TagViewHolder> {

    private final List<UserTag> list;

    public UserTagAdapter(List<UserTag> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserTagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new TagViewHolder(inflater.inflate(R.layout.layout_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserTagAdapter.TagViewHolder holder, int position) {
        UserTag tag = list.get(position);
        holder.bindView(tag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        LayoutTagBinding binding;

        TagViewHolder(View itemView) {
            super(itemView);
            binding = LayoutTagBinding.bind(itemView);
        }

        void bindView(UserTag tag) {
            binding.tagName.setText(tag.getName());
        }
    }
}
