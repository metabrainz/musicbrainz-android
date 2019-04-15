package org.metabrainz.mobile.presentation.features.release;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.Track;

import java.util.List;

import static android.view.View.GONE;

public class ReleaseTrackAdapter extends RecyclerView.Adapter {

    List<Track> recordings;

    public ReleaseTrackAdapter(List<Track> data) {
        recordings = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrackViewHolder viewHolder = (TrackViewHolder) holder;
        Track item = recordings.get(position);
        setViewVisibility(item.getTitle(), viewHolder.trackName);
        //setViewVisibility(item.getMbid(), viewHolder.trackArtist);
        setViewVisibility(String.valueOf(item.getPosition()), viewHolder.trackNumber);
        setViewVisibility(item.getDuration(), viewHolder.trackDuration);
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(GONE);
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView trackNumber, trackName, trackArtist, trackDuration;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackNumber = itemView.findViewById(R.id.track_number);
            trackArtist = itemView.findViewById(R.id.track_artist);
            trackName = itemView.findViewById(R.id.track_name);
            trackDuration = itemView.findViewById(R.id.track_time);
        }
    }
}
