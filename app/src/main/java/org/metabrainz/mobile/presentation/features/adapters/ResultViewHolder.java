package org.metabrainz.mobile.presentation.features.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;

class ResultViewHolder extends RecyclerView.ViewHolder {
    public final View itemView;
    private final TextView name;
    private final TextView primary;
    private final TextView disambiguation;
    private final TextView secondary;

    ResultViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.name = itemView.findViewById(R.id.item_name);
        this.disambiguation = itemView.findViewById(R.id.item_disambiguation);
        this.primary = itemView.findViewById(R.id.item_primary);
        this.secondary = itemView.findViewById(R.id.item_secondary);
    }

    void bind(ResultItem item) {
        setViewVisibility(item.getName(), name);
        setViewVisibility(item.getDisambiguation(), disambiguation);
        setViewVisibility(item.getPrimary(), primary);
        setViewVisibility(item.getSecondary(), secondary);
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }
}
