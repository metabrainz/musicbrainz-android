package org.metabrainz.mobile.presentation.features.userdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.userdata.Tag;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter {

    private List<Tag> list;

    public TagAdapter(List<Tag> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new TagViewHolder(inflater.inflate(R.layout.layout_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tag tag = list.get(position);
        ((TagViewHolder) holder).bindView(tag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagView;

        TagViewHolder(View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag_name);
        }

        void bindView(Tag tag) {
            tagView.setText(tag.getName());
        }
    }
}
