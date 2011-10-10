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

package org.musicbrainz.mobile.adapter;

import java.util.List;

import org.musicbrainz.android.api.data.ReleaseGroupStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.uiutil.StringFormat;
import org.musicbrainz.mobile.uiutil.StringMapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List adapter for release group search results.
 */
public class SearchReleaseGroupAdapter extends ArrayAdapter<ReleaseGroupStub> {

    private Activity context;
    private List<ReleaseGroupStub> resultData;

    public SearchReleaseGroupAdapter(Activity context, List<ReleaseGroupStub> resultData) {
        super(context, R.layout.list_srch_rg, resultData);
        this.context = context;
        this.resultData = resultData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View release = convertView;
        SearchReleaseGroupHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            release = inflater.inflate(R.layout.list_srch_rg, parent, false);
            holder = new SearchReleaseGroupHolder(release);
            release.setTag(holder);
        } else {
            holder = (SearchReleaseGroupHolder) release.getTag();
        }

        ReleaseGroupStub releaseGroup = resultData.get(position);
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
