package org.metabrainz.mobile.presentation.features.collection;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.intent.IntentFactory;
import org.metabrainz.mobile.presentation.features.artist.ArtistActivity;

import java.util.List;

public class CollectionAdapterArtist extends CollectionAdapter {
    private List<Artist> data;

    public CollectionAdapterArtist(List<Artist> data) {
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
        return new ArtistViewHolder(view);
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
        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), ArtistActivity.class);
        intent.putExtra(IntentFactory.Extra.ARTIST_MBID, data.get(position).getMbid());
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
