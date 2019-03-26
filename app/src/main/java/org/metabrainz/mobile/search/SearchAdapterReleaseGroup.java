package org.metabrainz.mobile.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.entity.ReleaseGroup;

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

        setViewVisibility(releaseGroup.getType(), viewHolder.releaseType);
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
