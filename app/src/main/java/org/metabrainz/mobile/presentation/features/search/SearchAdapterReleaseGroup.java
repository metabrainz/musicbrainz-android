package org.metabrainz.mobile.presentation.features.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupActivity;

import java.util.List;

public class SearchAdapterReleaseGroup extends SearchAdapter {

    private List<ReleaseGroup> data;

    public SearchAdapterReleaseGroup(List<ReleaseGroup> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        ReleaseGroup releaseGroup = data.get(position);
        viewHolder.releaseName.setText(releaseGroup.getTitle());

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReleaseGroupActivity.class);
            intent.putExtra(IntentFactory.Extra.RELEASE_GROUP, releaseGroup.getMbid());
            v.getContext().startActivity(intent);
        });

        setViewVisibility(releaseGroup.getFullType(), viewHolder.releaseType);
        setViewVisibility(releaseGroup.getDisplayArtist(), viewHolder.releaseArtist);
        setViewVisibility(releaseGroup.getDisambiguation(), viewHolder.releaseDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ReleaseGroupViewHolder extends EntityViewHolder {
        TextView releaseName, releaseArtist, releaseDisambiguation, releaseType;

        ReleaseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_group_name);
            releaseType = itemView.findViewById(R.id.release_group_type);
            releaseDisambiguation = itemView.findViewById(R.id.release_group_disambiguation);
            releaseArtist = itemView.findViewById(R.id.release_group_artist);
        }

    }
}
