package org.metabrainz.mobile.presentation.features.collection;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.IntentFactory;
import org.metabrainz.mobile.presentation.features.recording.RecordingActivity;
import org.metabrainz.mobile.util.Log;

import java.util.List;

public class CollectionAdapterRecording extends CollectionAdapter {

    private final List<Recording> data;

    public CollectionAdapterRecording(List<Recording> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public CollectionAdapterRecording.RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new CollectionAdapterRecording.RecordingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CollectionAdapterRecording.RecordingViewHolder viewHolder = (CollectionAdapterRecording.RecordingViewHolder) holder;
        Recording recording = data.get(position);
        viewHolder.recordingName.setText(recording.getTitle());

        if (recording.getReleases().size() != 0)
            setViewVisibility(recording.getReleases().get(0).getTitle(), viewHolder.recordingRelease);
        Log.d(recording.getDisplayArtist());
        setViewVisibility(recording.getDisplayArtist(), viewHolder.recordingArtist);
        setViewVisibility(recording.getDisambiguation(), viewHolder.recordingDisambiguation);
        setAnimation(viewHolder.itemView, position);

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecordingActivity.class);
            intent.putExtra(IntentFactory.Extra.RECORDING, recording.getMbid());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
