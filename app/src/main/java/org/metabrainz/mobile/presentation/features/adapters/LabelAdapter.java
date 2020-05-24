package org.metabrainz.mobile.presentation.features.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;

import java.util.List;

public class LabelAdapter extends TypeAdapter {

    public LabelAdapter(List<Label> data) {
        super(data, MBEntities.LABEL);
    }

    @NonNull
    @Override
    public LabelAdapter.LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_label, parent, false);
        return new LabelAdapter.LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LabelAdapter.LabelViewHolder viewHolder = (LabelAdapter.LabelViewHolder) holder;
        Label label = (Label) data.get(position);
        viewHolder.labelName.setText(label.getName());

        setViewVisibility(label.getType(), viewHolder.labelType);
        //temporary fix TODO: Seralize null objects
        if (label.getArea() != null)
            setViewVisibility(label.getArea().getName(), viewHolder.labelArea);
        else viewHolder.labelArea.setVisibility(View.GONE);
        setViewVisibility(label.getDisambiguation(), viewHolder.labelDisambiguation);
        setAnimation(viewHolder.itemView, position);
        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private static class LabelViewHolder extends EntityViewHolder {
        final TextView labelName;
        final TextView labelType;
        final TextView labelDisambiguation;
        final TextView labelArea;

        LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.label_name);
            labelType = itemView.findViewById(R.id.label_type);
            labelDisambiguation = itemView.findViewById(R.id.label_disambiguation);
            labelArea = itemView.findViewById(R.id.label_area);
        }

    }
}
