package org.metabrainz.mobile.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.search.entity.Release;

import java.util.List;

public class ArtistReleaseAdapter extends RecyclerView.Adapter {

    private List<Release> data;
    public ArtistReleaseAdapter(List<Release> data){this.data = data;}

    private class ReleaseItemViewHolder extends RecyclerView.ViewHolder{
        TextView releaseName, releaseDisambiguation, releaseArtist;
        ImageView coverArtView;
        public ReleaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseArtist = itemView.findViewById(R.id.release_artist);
            releaseDisambiguation = itemView.findViewById(R.id.release_disambiguation);
            coverArtView = itemView.findViewById(R.id.cover_art);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_release,parent,false);
        return new ReleaseItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReleaseItemViewHolder viewHolder = (ReleaseItemViewHolder) holder;
        Release release = data.get(position);
        viewHolder.releaseName.setText(release.getTitle());
        setViewVisibility(release.getDisplayArtist(), viewHolder.releaseArtist);
        setViewVisibility(release.getDisambiguation(), viewHolder.releaseDisambiguation);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }
}
