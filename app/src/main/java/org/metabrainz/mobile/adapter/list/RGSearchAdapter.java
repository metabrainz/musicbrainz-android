package org.metabrainz.mobile.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.metabrainz.mobile.R;
import org.metabrainz.mobile.api.data.ReleaseGroupSearchResult;
import org.metabrainz.mobile.string.StringFormat;
import org.metabrainz.mobile.string.StringMapper;

import java.util.List;

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
                title = base.findViewById(R.id.search_release);
            }
            return title;
        }

        TextView getArtist() {
            if (artist == null) {
                artist = base.findViewById(R.id.search_release_artist);
            }
            return artist;
        }

        TextView getType() {
            if (type == null) {
                type = base.findViewById(R.id.search_release_type);
            }
            return type;
        }
    }

}
