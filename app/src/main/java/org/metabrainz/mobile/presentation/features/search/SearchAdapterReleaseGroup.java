package org.metabrainz.mobile.presentation.features.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;

import java.util.List;

public class SearchAdapterReleaseGroup extends SearchAdapter {

    SearchAdapterReleaseGroup(List<ReleaseGroup> data) {
        super(data, MBEntities.RELEASE_GROUP);
    }

    @NonNull
    @Override
    public SearchAdapterReleaseGroup.ReleaseGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_release_group, parent, false);
        return new SearchAdapterReleaseGroup.ReleaseGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchAdapterReleaseGroup.ReleaseGroupViewHolder viewHolder = (SearchAdapterReleaseGroup.ReleaseGroupViewHolder) holder;
        ReleaseGroup releaseGroup = (ReleaseGroup) data.get(position);
        viewHolder.releaseName.setText(releaseGroup.getTitle());

        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));

        setViewVisibility(releaseGroup.getFullType(), viewHolder.releaseType);
        setViewVisibility(releaseGroup.getDisplayArtist(), viewHolder.releaseArtist);
        setViewVisibility(releaseGroup.getDisambiguation(), viewHolder.releaseDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    private static class ReleaseGroupViewHolder extends EntityViewHolder {
        final TextView releaseName;
        final TextView releaseArtist;
        final TextView releaseDisambiguation;
        final TextView releaseType;

        ReleaseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_group_name);
            releaseType = itemView.findViewById(R.id.release_group_type);
            releaseDisambiguation = itemView.findViewById(R.id.release_group_disambiguation);
            releaseArtist = itemView.findViewById(R.id.release_group_artist);
        }

    }
}
