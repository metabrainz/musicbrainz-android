package org.metabrainz.mobile.presentation.features.tagger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.entities.EntityUtils;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.presentation.features.recording.RecordingActivity;

import java.util.List;

public class TaggerAdapter extends RecyclerView.Adapter<TaggerAdapter.TaggerViewHolder> {
    private final List<Recording> data;

    public TaggerAdapter(List<Recording> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TaggerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
        return new TaggerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaggerViewHolder viewHolder, int position) {
        Recording recording = data.get(position);
        viewHolder.recordingName.setText(recording.getTitle());

        if (recording.getReleases() != null && recording.getReleases().size() != 0)
            setViewVisibility(recording.getReleases().get(0).getTitle(), viewHolder.recordingRelease);
        setViewVisibility(EntityUtils.getDisplayArtist(recording.getArtistCredits()), viewHolder.recordingArtist);
        setViewVisibility(recording.getDisambiguation(), viewHolder.recordingDisambiguation);

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecordingActivity.class);
            intent.putExtra(Constants.MBID, recording.getMbid());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void setViewVisibility(String text, TextView view) {
        if (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        } else view.setVisibility(View.GONE);
    }

    class TaggerViewHolder extends RecyclerView.ViewHolder {
        final TextView recordingName;
        final TextView recordingArtist;
        final TextView recordingDisambiguation;
        final TextView recordingRelease;

        public TaggerViewHolder(@NonNull View itemView) {
            super(itemView);
            recordingName = itemView.findViewById(R.id.recording_name);
            recordingRelease = itemView.findViewById(R.id.recording_release);
            recordingDisambiguation = itemView.findViewById(R.id.recording_disambiguation);
            recordingArtist = itemView.findViewById(R.id.recording_artist);
        }
    }
}
