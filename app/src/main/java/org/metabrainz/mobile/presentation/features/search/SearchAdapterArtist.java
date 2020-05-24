package org.metabrainz.mobile.presentation.features.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;

import java.util.List;

public class SearchAdapterArtist extends SearchAdapter {

    SearchAdapterArtist(List<Artist> data) {
        super(data, MBEntities.ARTIST);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArtistViewHolder viewHolder = (ArtistViewHolder) holder;
        Artist artist = (Artist) data.get(position);
        viewHolder.artistName.setText(artist.getName());
        setViewVisibility(artist.getCountry(), viewHolder.artistArea);
        setViewVisibility(artist.getType(), viewHolder.artistType);
        setViewVisibility(artist.getDisambiguation(), viewHolder.artistDisambiguation);
        setAnimation(viewHolder.itemView, position);
        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private static class ArtistViewHolder extends EntityViewHolder {
        final TextView artistName;
        final TextView artistType;
        final TextView artistDisambiguation;
        final TextView artistArea;

        ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            artistArea = itemView.findViewById(R.id.artist_area);
            artistDisambiguation = itemView.findViewById(R.id.artist_disambiguation);
            artistType = itemView.findViewById(R.id.artist_type);
        }

    }
}
