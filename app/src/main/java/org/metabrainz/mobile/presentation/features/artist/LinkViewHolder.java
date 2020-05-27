package org.metabrainz.mobile.presentation.features.artist;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.databinding.ItemLinkBinding;

class LinkViewHolder extends RecyclerView.ViewHolder {
    private ItemLinkBinding binding;

    LinkViewHolder(@NonNull ItemLinkBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void bind(Drawable drawable, String type) {
        binding.linkImage.setImageDrawable(drawable);
        // TODO: Check if generic drawable used by checking type from LinksClassifier
        binding.linkText.setVisibility(View.VISIBLE);
        binding.linkText.setText(type.toUpperCase());
    }

}
