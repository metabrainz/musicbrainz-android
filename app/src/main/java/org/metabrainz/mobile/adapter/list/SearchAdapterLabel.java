package org.metabrainz.mobile.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.EntityViewHolder;
import org.metabrainz.mobile.api.data.search.entity.Label;

import java.util.List;

public class SearchAdapterLabel extends SearchAdapter {
    private List<Label> data;

    public SearchAdapterLabel(List<Label> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public SearchAdapterLabel.LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_label, parent, false);
        return new SearchAdapterLabel.LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchAdapterLabel.LabelViewHolder viewHolder = (SearchAdapterLabel.LabelViewHolder) holder;
        Label label = data.get(position);
        viewHolder.labelName.setText(label.getName());

        setViewVisibility(label.getType(), viewHolder.labelType);
        //temporary fix TODO: Seralize null objects
        if (label.getArea() != null)
            setViewVisibility(label.getArea().getName(), viewHolder.labelArea);
        else viewHolder.labelArea.setVisibility(View.GONE);
        setViewVisibility(label.getDisambiguation(), viewHolder.labelDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    private static class LabelViewHolder extends EntityViewHolder {
        TextView labelName, labelType, labelDisambiguation, labelArea;

        LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.label_name);
            labelType = itemView.findViewById(R.id.label_type);
            labelDisambiguation = itemView.findViewById(R.id.label_disambiguation);
            labelArea = itemView.findViewById(R.id.label_area);
        }

    }
}
