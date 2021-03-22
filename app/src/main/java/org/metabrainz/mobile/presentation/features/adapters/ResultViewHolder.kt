package org.metabrainz.mobile.presentation.features.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.databinding.ItemResultBinding;

class ResultViewHolder extends RecyclerView.ViewHolder {
    public final View itemView;
    private final ItemResultBinding binding;

    ResultViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        binding = ItemResultBinding.bind(itemView);
    }

    void bind(ResultItem item) {
        setViewVisibility(item.getName(), binding.itemName);
        setViewVisibility(item.getDisambiguation(), binding.itemDisambiguation);
        setViewVisibility(item.getPrimary(), binding.itemPrimary);
        setViewVisibility(item.getSecondary(), binding.itemSecondary);
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }
}
