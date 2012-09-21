package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseGroupInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.string.StringMapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArtistRGAdapter extends ArrayAdapter<ReleaseGroupInfo> {

    private Activity context;
    private List<ReleaseGroupInfo> releaseGroups;

    public ArtistRGAdapter(Activity context, List<ReleaseGroupInfo> releaseGroups) {
        super(context, R.layout.list_release_group, releaseGroups);
        this.context = context;
        this.releaseGroups = releaseGroups;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View release = convertView;
        ArtistReleaseGroupHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            release = inflater.inflate(R.layout.list_release_group, parent, false);
            holder = new ArtistReleaseGroupHolder(release);
            release.setTag(holder);
        } else {
            holder = (ArtistReleaseGroupHolder) release.getTag();
        }

        ReleaseGroupInfo rData = releaseGroups.get(position);
        holder.getReleaseTitle().setText(rData.getTitle());
        holder.getReleaseYear().setText(rData.getReleaseYear());
        holder.getReleaseType().setText(StringMapper.mapRGTypeString(getContext(), rData.getType()));
        return release;
    }

    private class ArtistReleaseGroupHolder {
        View base;
        TextView title = null;
        TextView year = null;
        TextView type = null;

        ArtistReleaseGroupHolder(View base) {
            this.base = base;
        }

        TextView getReleaseTitle() {
            if (title == null) {
                title = (TextView) base.findViewById(R.id.list_rg_title);
            }
            return title;
        }

        TextView getReleaseYear() {
            if (year == null) {
                year = (TextView) base.findViewById(R.id.list_rg_year);
            }
            return year;
        }

        TextView getReleaseType() {
            if (type == null) {
                type = (TextView) base.findViewById(R.id.list_rg_type);
            }
            return type;
        }
    }

}