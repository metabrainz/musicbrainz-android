package org.metabrainz.android.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.metabrainz.android.R;
import org.metabrainz.android.api.data.ReleaseGroupSearchResult;
import org.metabrainz.android.string.StringFormat;
import org.metabrainz.android.string.StringMapper;

import java.util.List;

import android.content.Context;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RGSearchAdapter extends ArrayAdapter<ReleaseGroupSearchResult> {

    private Context context;
    private List<ReleaseGroupSearchResult> resultData;

    public RGSearchAdapter(Context context, List<ReleaseGroupSearchResult> resultData) {
        super(context, R.layout.list_search_release_group, resultData);
        this.context = context;
        this.resultData = resultData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View release = convertView;
        SearchReleaseGroupHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            release = inflater.inflate(R.layout.list_search_release_group, parent, false);
            holder = new SearchReleaseGroupHolder(release);
            release.setTag(holder);
        } else {
            holder = (SearchReleaseGroupHolder) release.getTag();
        }

        ReleaseGroupSearchResult releaseGroup = resultData.get(position);
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
