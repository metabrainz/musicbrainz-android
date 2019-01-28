package org.metabrainz.android.adapter.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.metabrainz.android.R;
import org.metabrainz.android.api.data.ReleaseSearchResult;
import org.metabrainz.android.string.StringFormat;
import org.metabrainz.android.string.StringMapper;

import java.util.List;

import android.content.Context;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ReleaseInfoAdapter extends ArrayAdapter<ReleaseSearchResult> {

    private Context context;
    private List<ReleaseSearchResult> releasesInfo;
    private int layoutId;

    public ReleaseInfoAdapter(Context context, int layoutId, List<ReleaseSearchResult> releasesInfo) {
        super(context, layoutId, releasesInfo);
        this.context = context;
        this.layoutId = layoutId;
        this.releasesInfo = releasesInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View releaseView = convertView;
        ReleaseInfoHolder holder = null;

        if (releaseView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            releaseView = inflater.inflate(layoutId, parent, false);
            holder = new ReleaseInfoHolder(releaseView);
            releaseView.setTag(holder);
        } else {
            holder = (ReleaseInfoHolder) releaseView.getTag();
        }

        ReleaseSearchResult release = releasesInfo.get(position);
        holder.getTitle().setText(release.getTitle());
        holder.getArtist().setText(StringFormat.commaSeparateArtists(release.getArtists()));
        holder.getCountry().setText(release.getCountryCode());
        holder.getDate().setText(release.getDate());

        if (layoutId == R.layout.list_release) {
            holder.getTrackNum().setText(release.getTracksNum() + " " + context.getString(R.string.label_tracks));
            holder.getFormat().setText(StringMapper.buildReleaseFormatsString(getContext(), release.getFormats()));            
            holder.getLabels().setText(StringFormat.commaSeparate(release.getLabels()));
        }
        return releaseView;
    }

    private class ReleaseInfoHolder {
        View base;
        TextView title = null;
        TextView artist = null;
        TextView tracksNum = null;
        TextView formats = null;
        TextView labels = null;
        TextView date = null;
        TextView country = null;

        ReleaseInfoHolder(View base) {
            this.base = base;
        }

        TextView getTitle() {
            if (title == null) {
                title = (TextView) base.findViewById(R.id.list_release_title);
            }
            return title;
        }

        TextView getArtist() {
            if (artist == null) {
                artist = (TextView) base.findViewById(R.id.list_release_artist);
            }
            return artist;
        }

        TextView getTrackNum() {
            if (tracksNum == null) {
                tracksNum = (TextView) base.findViewById(R.id.list_release_tracksnum);
            }
            return tracksNum;
        }

        TextView getFormat() {
            if (formats == null) {
                formats = (TextView) base.findViewById(R.id.list_release_formats);
            }
            return formats;
        }

        TextView getLabels() {
            if (labels == null) {
                labels = (TextView) base.findViewById(R.id.list_release_labels);
            }
            return labels;
        }

        TextView getCountry() {
            if (country == null) {
                country = (TextView) base.findViewById(R.id.list_release_country);
            }
            return country;
        }

        TextView getDate() {
            if (date == null) {
                date = (TextView) base.findViewById(R.id.list_release_date);
            }
            return date;
        }
    }

}
