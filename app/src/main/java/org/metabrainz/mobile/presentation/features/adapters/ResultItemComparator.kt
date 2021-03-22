package org.metabrainz.mobile.presentation.features.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ResultItemComparator extends DiffUtil.ItemCallback<ResultItem> {
    @Override
    public boolean areItemsTheSame(@NonNull ResultItem oldItem, @NonNull ResultItem newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ResultItem oldItem, @NonNull ResultItem newItem) {
        return oldItem.getMBID().equalsIgnoreCase(newItem.getMBID());
    }
}
