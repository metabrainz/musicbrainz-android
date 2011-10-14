/*
 * Copyright (C) 2011 Jamie McDonald
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
import org.musicbrainz.mobile.string.StringMapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List adapter for artist release groups.
 */
public class ArtistReleaseGroupAdapter extends ArrayAdapter<ReleaseGroupStub> {

    private Activity context;
    private List<ReleaseGroupStub> releaseGroups;

    public ArtistReleaseGroupAdapter(Activity context, List<ReleaseGroupStub> releaseGroups) {
        super(context, R.layout.list_rg, releaseGroups);
        this.context = context;
        this.releaseGroups = releaseGroups;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View release = convertView;
        ArtistReleaseGroupHolder holder = null;

        if (release == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            release = inflater.inflate(R.layout.list_rg, parent, false);
            holder = new ArtistReleaseGroupHolder(release);
            release.setTag(holder);
        } else {
            holder = (ArtistReleaseGroupHolder) release.getTag();
        }

        ReleaseGroupStub rData = releaseGroups.get(position);
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