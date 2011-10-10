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

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List adapter for artist stub search results.
 */
public class SearchArtistAdapter extends ArrayAdapter<ArtistStub> {

    private Activity context;
    private List<ArtistStub> resultData;

    public SearchArtistAdapter(Activity context, List<ArtistStub> resultData) {
        super(context, R.layout.list_srch_artist, resultData);
        this.context = context;
        this.resultData = resultData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View artist = convertView;
        ArtistSearchHolder holder = null;

        if (artist == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            artist = inflater.inflate(R.layout.list_srch_artist, parent, false);
            holder = new ArtistSearchHolder(artist);
            artist.setTag(holder);
        } else {
            holder = (ArtistSearchHolder) artist.getTag();
        }

        ArtistStub artistData = resultData.get(position);
        holder.getArtistName().setText(artistData.getName());

        TextView option = holder.getDisambiguation();
        String disambiguation = artistData.getDisambiguation();
        option.setText(disambiguation);

        if (disambiguation != null) {
            option.setVisibility(View.VISIBLE);
        } else {
            option.setVisibility(View.GONE);
        }
        return artist;
    }

    private class ArtistSearchHolder {

        View base;
        TextView artistName = null;
        TextView disambiguation = null;

        ArtistSearchHolder(View base) {
            this.base = base;
        }

        TextView getArtistName() {
            if (artistName == null) {
                artistName = (TextView) base.findViewById(R.id.search_artist_name);
            }
            return artistName;
        }

        TextView getDisambiguation() {
            if (disambiguation == null) {
                disambiguation = (TextView) base.findViewById(R.id.search_artist_disambig);
            }
            return disambiguation;
        }
    }

}
