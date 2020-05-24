package org.metabrainz.mobile.presentation.features.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

public class ReleaseAdapter extends TypeAdapter {

    public ReleaseAdapter(List<Release> data) {
        super(data, MBEntities.RELEASE);
    }

    @NonNull
    @Override
    public ReleaseAdapter.ReleaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_release, parent, false);
        return new ReleaseAdapter.ReleaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReleaseAdapter.ReleaseViewHolder viewHolder = (ReleaseAdapter.ReleaseViewHolder) holder;
        Release release = (Release) data.get(position);
        viewHolder.releaseName.setText(release.getTitle());

        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));

        setViewVisibility(release.labelCatalog(), viewHolder.releaseLabel);
        setViewVisibility(release.getDisplayArtist(), viewHolder.releaseArtist);
        setViewVisibility(release.getDisambiguation(), viewHolder.releaseDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    private static class ReleaseViewHolder extends EntityViewHolder {
        final TextView releaseName;
        final TextView releaseArtist;
        final TextView releaseDisambiguation;
        final TextView releaseLabel;

        ReleaseViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseLabel = itemView.findViewById(R.id.release_label);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            releaseArtist = itemView.findViewById(R.id.release_artist);
        }

    }

}
