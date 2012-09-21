package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseGroupInfo;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.string.StringMapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RGSearchAdapter extends ArrayAdapter<ReleaseGroupInfo> {

    private Activity context;
    private List<ReleaseGroupInfo> resultData;

    public RGSearchAdapter(Activity context, List<ReleaseGroupInfo> resultData) {
        super(context, R.layout.list_search_release_group, resultData);
        this.context = context;
        this.resultData = resultData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View release = convertView;
        SearchReleaseGroupHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            release = inflater.inflate(R.layout.list_search_release_group, parent, false);
            holder = new SearchReleaseGroupHolder(release);
            release.setTag(holder);
        } else {
            holder = (SearchReleaseGroupHolder) release.getTag();
        }

        ReleaseGroupInfo releaseGroup = resultData.get(position);
        holder.getTitle().setText(releaseGroup.getTitle());
        holder.getArtist().setText(StringFormat.commaSeparateArtists(releaseGroup.getArtists()));
        holder.getType().setText(StringMapper.mapRGTypeString(getContext(), releaseGroup.getType()));
        return release;
    }

    private class SearchReleaseGroupHolder {
        View base;
        TextView title = null;
        TextView artist = null;
        TextView type = null;

        SearchReleaseGroupHolder(View base) {
            this.base = base;
        }

        TextView getTitle() {
            if (title == null) {
                title = (TextView) base.findViewById(R.id.search_release);
            }
            return title;
        }

        TextView getArtist() {
            if (artist == null) {
                artist = (TextView) base.findViewById(R.id.search_release_artist);
            }
            return artist;
        }

        TextView getType() {
            if (type == null) {
                type = (TextView) base.findViewById(R.id.search_release_type);
            }
            return type;
        }
    }

}
