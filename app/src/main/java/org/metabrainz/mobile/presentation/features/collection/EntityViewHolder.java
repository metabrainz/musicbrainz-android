package org.metabrainz.mobile.presentation.features.collection;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class EntityViewHolder extends RecyclerView.ViewHolder {
    public final View itemView;

    EntityViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }
}
