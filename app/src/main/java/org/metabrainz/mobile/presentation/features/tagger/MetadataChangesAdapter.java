package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.util.MetadataChange;

import java.util.List;

public class MetadataChangesAdapter extends RecyclerView.Adapter
        <MetadataChangesAdapter.MetadataChangeViewHolder> {

    List<MetadataChange> changes;

    public MetadataChangesAdapter(List<MetadataChange> changes) {
        this.changes = changes;
    }

    @NonNull
    @Override
    public MetadataChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new MetadataChangeViewHolder(inflater
                .inflate(R.layout.item_metadata_change, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MetadataChangeViewHolder holder, int position) {
        holder.bind(changes.get(position));
    }

    @Override
    public int getItemCount() {
        return changes.size();
    }

    class MetadataChangeViewHolder extends RecyclerView.ViewHolder {
        private TextView tagNameView, originalValueView, newValueView;

        public MetadataChangeViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameView = itemView.findViewById(R.id.tag_name);
            originalValueView = itemView.findViewById(R.id.original_value);
            newValueView = itemView.findViewById(R.id.new_value);
        }

        public void bind(MetadataChange change) {
            tagNameView.setText(change.getTagName().toString());
            originalValueView.setText(change.getOriginalValue());
            newValueView.setText(change.getNewValue());
            if (!change.isChanged()) {
                originalValueView.setBackgroundColor(originalValueView.getContext()
                        .getResources().getColor(android.R.color.background_light));
                newValueView.setVisibility(View.GONE);
            }
        }
    }
}
