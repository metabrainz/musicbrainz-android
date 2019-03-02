package org.metabrainz.mobile.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.adapter.EntityViewHolder;
import org.metabrainz.mobile.api.data.search.entity.Artist;

import java.util.List;

public class SearchAdapterArtist extends SearchAdapter {
    private List<Artist> data;

    public SearchAdapterArtist(List<Artist> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false);
        ArtistViewHolder viewHolder = new ArtistViewHolder(view);
        int pos = viewHolder.getAdapterPosition();
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArtistViewHolder viewHolder = (ArtistViewHolder) holder;
        Artist artist = data.get(position);
        viewHolder.artistName.setText(artist.getName());
        setViewVisibility(artist.getCountry(), viewHolder.artistArea);
        setViewVisibility(artist.getType(), viewHolder.artistType);
        setViewVisibility(artist.getDisambiguation(), viewHolder.artistDisambiguation);
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    private static class ArtistViewHolder extends EntityViewHolder {
        TextView artistName, artistType, artistDisambiguation, artistArea;

        ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            artistArea = itemView.findViewById(R.id.artist_area);
            artistDisambiguation = itemView.findViewById(R.id.artist_disambiguation);
            artistType = itemView.findViewById(R.id.artist_type);
        }

    }
}
