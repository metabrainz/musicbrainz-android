package org.metabrainz.mobile.presentation.features.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntities;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;

import java.util.List;

public class SearchAdapterRecording extends SearchAdapter {

    SearchAdapterRecording(List<Recording> data) {
        super(data, MBEntities.RECORDING);
    }

    @NonNull
    @Override
    public SearchAdapterRecording.RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new SearchAdapterRecording.RecordingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchAdapterRecording.RecordingViewHolder viewHolder = (SearchAdapterRecording.RecordingViewHolder) holder;
        Recording recording = (Recording) data.get(position);
        viewHolder.recordingName.setText(recording.getTitle());

        if (recording.getReleases() != null && recording.getReleases().size() != 0)
            setViewVisibility(recording.getReleases().get(0).getTitle(), viewHolder.recordingRelease);
        setViewVisibility(recording.getDisplayArtist(), viewHolder.recordingArtist);
        setViewVisibility(recording.getDisambiguation(), viewHolder.recordingDisambiguation);
        setAnimation(viewHolder.itemView, position);

        viewHolder.itemView.setOnClickListener(v -> onClick(v, position));
    }

    private static class RecordingViewHolder extends EntityViewHolder {
        final TextView recordingName;
        final TextView recordingArtist;
        final TextView recordingDisambiguation;
        final TextView recordingRelease;

        RecordingViewHolder(@NonNull View itemView) {
            super(itemView);
            recordingName = itemView.findViewById(R.id.recording_name);
            recordingRelease = itemView.findViewById(R.id.recording_release);
            recordingDisambiguation = itemView.findViewById(R.id.recording_disambiguation);
            recordingArtist = itemView.findViewById(R.id.recording_artist);
        }

    }
}
