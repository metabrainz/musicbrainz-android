package org.metabrainz.mobile.presentation.features.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import org.metabrainz.mobile.databinding.ItemResultBinding;

class ResultViewHolder extends RecyclerView.ViewHolder {
    public final View itemView;
    private ItemResultBinding binding;
=======
import org.metabrainz.mobile.R;

class ResultViewHolder extends RecyclerView.ViewHolder {
    public final View itemView;
    private final TextView name;
    private final TextView primary;
    private final TextView disambiguation;
    private final TextView secondary;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.

    ResultViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
<<<<<<< HEAD
        binding = ItemResultBinding.bind(itemView);
    }

    void bind(ResultItem item) {
        setViewVisibility(item.getName(), binding.itemName);
        setViewVisibility(item.getDisambiguation(), binding.itemDisambiguation);
        setViewVisibility(item.getPrimary(), binding.itemPrimary);
        setViewVisibility(item.getSecondary(), binding.itemSecondary);
=======
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
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }
}
