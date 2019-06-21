package org.metabrainz.mobile.presentation.features.collection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.presentation.IntentFactory;

import java.util.List;

public class CollectionListAdapter extends RecyclerView.Adapter {

    private List<Collection> collections;

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
        private TextView collectionNameView, collectionTypeView,
                collectionCountView, collectionEntityView;

        private View view;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            collectionCountView = itemView.findViewById(R.id.collection_count);
            collectionEntityView = itemView.findViewById(R.id.collection_entity);
            collectionNameView = itemView.findViewById(R.id.collection_name);
            collectionTypeView = itemView.findViewById(R.id.collection_type);
        }

        public void bind(Collection collection) {
            collectionNameView.setText(collection.getName());
            collectionTypeView.setText(collection.getType());
            collectionEntityView.setText(collection.getEntityType());
            collectionCountView.setText(String.valueOf(collection.getCount()));

            view.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CollectionDetailsActivity.class);
                intent.putExtra(IntentFactory.Extra.COLLECTION_MBID, collection.getMbid());
                intent.putExtra(IntentFactory.Extra.TYPE, collection.getEntityType());
                v.getContext().startActivity(intent);
            });

        }
    }
}
