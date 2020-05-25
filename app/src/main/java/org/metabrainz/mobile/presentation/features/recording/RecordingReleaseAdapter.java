package org.metabrainz.mobile.presentation.features.recording;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.presentation.features.release.ReleaseActivity;

import java.util.List;

import static android.view.View.GONE;

class RecordingReleaseAdapter extends RecyclerView.Adapter {

    private final List<Release> releaseList;

    public RecordingReleaseAdapter(List<Release> releaseList) {
        this.releaseList = releaseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recording_release, parent, false);
        return new ReleaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReleaseViewHolder viewHolder = (ReleaseViewHolder) holder;
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return releaseList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(GONE);
    }

    private String calculateDisplayPosition(Release release) {
        int mediaPosition, trackPosition;
        mediaPosition = release.getMedia().get(0).getPosition();
        trackPosition = release.getMedia().get(0).getTracks().get(0).getPosition();
        return mediaPosition + "." + trackPosition;
    }

    private class ReleaseViewHolder extends RecyclerView.ViewHolder {
        final TextView releaseName;
        final TextView releaseArtist;
        final TextView releaseCountry;
        final TextView releaseDate;
        final TextView recordingDuration;
        final TextView recordingPosition;

        ReleaseViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseName = itemView.findViewById(R.id.release_name);
            releaseArtist = itemView.findViewById(R.id.release_artist);
            recordingPosition = itemView.findViewById(R.id.recording_position);
            recordingDuration = itemView.findViewById(R.id.recording_duration);
            releaseCountry = itemView.findViewById(R.id.release_country);
            releaseDate = itemView.findViewById(R.id.release_date);
        }

        void bind(int position) {
            Release item = releaseList.get(position);
            setViewVisibility(item.getTitle(), releaseName);
            setViewVisibility(item.getDisplayArtist(), releaseArtist);
            setViewVisibility(item.getCountry(), releaseCountry);
            setViewVisibility(item.getDate(), releaseDate);

            /* The part of a recording included as a track in a given release is retrieved
             * as follows. The releases associated with a recording are fetched. Each release
             * has a media object. Media contains the list of all tracks in usual cases but
             * when media are requested for a particular recording only the associated tracks
             * are present. Therefore, there is only one track in each medium which is the
             * required track.
             * */
            setViewVisibility(item.getMedia().get(0).getTracks().get(0).getDuration(),
                    recordingDuration);

            setViewVisibility(calculateDisplayPosition(item), recordingPosition);

            this.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ReleaseActivity.class);
                intent.putExtra(Constants.MBID, item.getMbid());
                v.getContext().startActivity(intent);
            });
        }
    }
}
