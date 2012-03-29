/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz for Android.
 * 
 * MusicBrainz for Android is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz for Android is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz for Android. If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.adapter.list;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.string.StringFormat;
import org.musicbrainz.mobile.string.StringMapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReleaseStubAdapter extends ArrayAdapter<ReleaseStub> {

    private Activity context;
    private List<ReleaseStub> releaseStubs;

    public ReleaseStubAdapter(Activity context, List<ReleaseStub> releaseStubs) {
        super(context, R.layout.list_release, releaseStubs);
        this.context = context;
        this.releaseStubs = releaseStubs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View release = convertView;
        ReleaseStubHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            release = inflater.inflate(R.layout.list_release, parent, false);
            holder = new ReleaseStubHolder(release);
            release.setTag(holder);
        } else {
            holder = (ReleaseStubHolder) release.getTag();
        }

        ReleaseStub stub = releaseStubs.get(position);

        holder.getTitle().setText(stub.getTitle());
        holder.getArtist().setText(stub.getArtistName());

        holder.getTrackNum().setText(stub.getTracksNum() + " " + context.getString(R.string.label_tracks));
        holder.getFormat().setText(StringMapper.buildReleaseFormatsString(getContext(), stub.getFormats()));

        holder.getLabels().setText(StringFormat.commaSeparate(stub.getLabels()));
        holder.getCountry().setText(stub.getCountryCode());
        holder.getDate().setText(stub.getDate());
        return release;
    }

    private class ReleaseStubHolder {
        View base;
        TextView title = null;
        TextView artist = null;
        TextView tracksNum = null;
        TextView formats = null;
        TextView labels = null;
        TextView date = null;
        TextView country = null;

        ReleaseStubHolder(View base) {
            this.base = base;
        }

        TextView getTitle() {
            if (title == null) {
                title = (TextView) base.findViewById(R.id.list_release);
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
