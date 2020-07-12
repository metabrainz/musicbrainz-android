package org.metabrainz.mobile.presentation.features.collection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.databinding.ItemCollectionBinding;

import java.util.List;

class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder> {

    private final List<Collection> collections;

    CollectionListAdapter(List<Collection> collections) {
        this.collections = collections;
    }

    @NonNull
    @Override
    public CollectionListAdapter.CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new CollectionViewHolder(ItemCollectionBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionListAdapter.CollectionViewHolder holder, int position) {
        Collection collection = collections.get(position);
        holder.bind(collection);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {

        private final ItemCollectionBinding binding;

        CollectionViewHolder(ItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Collection collection) {
            binding.collectionName.setText(collection.getName());
            binding.collectionType.setText(collection.getType());
            binding.collectionEntity.setText(collection.getEntityType());
            binding.collectionCount.setText(String.valueOf(collection.getCount()));

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CollectionDetailsActivity.class);
                intent.putExtra(Constants.MBID, collection.getMbid());
                intent.putExtra(Constants.TYPE, CollectionUtils.getCollectionEntityType(collection));
                v.getContext().startActivity(intent);
            });

        }
    }
}
