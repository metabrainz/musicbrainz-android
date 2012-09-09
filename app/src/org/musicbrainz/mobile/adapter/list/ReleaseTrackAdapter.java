package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.Track;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReleaseTrackAdapter extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> tracks;

    public ReleaseTrackAdapter(Activity context, List<Track> tracks) {
        super(context, R.layout.list_track, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View track = convertView;
        ReleaseTrackHolder holder = null;

        if (track == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            track = inflater.inflate(R.layout.list_track, parent, false);
            holder = new ReleaseTrackHolder(track);
            track.setTag(holder);
        } else {
            holder = (ReleaseTrackHolder) track.getTag();
        }

        Track trackData = tracks.get(position);
        holder.getTrackNum().setText("" + trackData.getPosition() + ".");
        holder.getTrackName().setText(trackData.getTitle());
        holder.getTrackTime().setText(trackData.getFormattedDuration());
        return track;
    }

    private class ReleaseTrackHolder {
        View base;
        TextView number = null;
        TextView name = null;
        TextView time = null;

        ReleaseTrackHolder(View base) {
            this.base = base;
        }

        TextView getTrackNum() {
            if (number == null) {
                number = (TextView) base.findViewById(R.id.list_track_num);
            }
            return number;
        }

        TextView getTrackName() {
            if (name == null) {
                name = (TextView) base.findViewById(R.id.list_track_name);
            }
            return name;
        }

        TextView getTrackTime() {
            if (time == null) {
                time = (TextView) base.findViewById(R.id.list_track_time);
            }
            return time;
        }
    }

}
