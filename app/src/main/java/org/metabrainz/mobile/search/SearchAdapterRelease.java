package org.metabrainz.mobile.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.entity.Release;

import java.util.List;

public class SearchAdapterRelease extends SearchAdapter {
    private List<Release> data;

    public SearchAdapterRelease(List<Release> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public SearchAdapterRelease.ReleaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_release, parent, false);
        return new SearchAdapterRelease.ReleaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchAdapterRelease.ReleaseViewHolder viewHolder = (SearchAdapterRelease.ReleaseViewHolder) holder;
        Release release = data.get(position);
        viewHolder.releaseName.setText(release.getTitle());

        setViewVisibility(release.labelCatalog(), viewHolder.releaseLabel);
        setViewVisibility(release.getDisplayArtist(), viewHolder.releaseArtist);
        setViewVisibility(release.getDisambiguation(), viewHolder.releaseDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ReleaseViewHolder extends EntityViewHolder {
        TextView releaseName, releaseArtist, releaseDisambiguation, releaseLabel;

        ReleaseViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseLabel = itemView.findViewById(R.id.release_label);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            releaseArtist = itemView.findViewById(R.id.release_artist);
        }

    }

}
