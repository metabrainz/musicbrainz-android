package org.metabrainz.mobile.presentation.features.collection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.databinding.ItemCollectionBinding;

import java.util.List;

class CollectionListAdapter extends RecyclerView.Adapter {

    private final List<Collection> collections;

    public CollectionListAdapter(List<Collection> collections) {
        this.collections = collections;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new CollectionViewHolder(inflater.inflate(R.layout.item_collection, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Collection collection = collections.get(position);
        ((CollectionViewHolder) holder).bind(collection);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    private static class CollectionViewHolder extends RecyclerView.ViewHolder {

        private ItemCollectionBinding binding;
        private final View view;

        CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCollectionBinding.bind(itemView);
            this.view = itemView;
        }

        void bind(Collection collection) {
            binding.collectionName.setText(collection.getName());
            binding.collectionType.setText(collection.getType());
            binding.collectionEntity.setText(collection.getEntityType());
            binding.collectionCount.setText(String.valueOf(collection.getCount()));

            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CollectionDetailsActivity.class);
                intent.putExtra(Constants.MBID, collection.getMbid());
                intent.putExtra(Constants.TYPE, CollectionUtils.getCollectionEntityType(collection));
                v.getContext().startActivity(intent);
            });

        }
    }
}
