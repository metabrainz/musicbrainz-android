/*
 * Copyright (C) 2010 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile.ui.util;

import java.util.List;

import org.musicbrainz.android.api.data.ArtistStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.ui.activity.SearchActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for list of artist stub search results.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ArtistSearchAdapter extends ArrayAdapter<ArtistStub> {
	
	SearchActivity context;
	List<ArtistStub> resultData;

	public ArtistSearchAdapter(Context context, List<ArtistStub> resultData) {
		super(context, R.layout.list_srch_artist, resultData);
		
		this.context = (SearchActivity) context;
		this.resultData = resultData;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View artist = convertView; 
		ArtistSearchWrapper wrapper = null;
		
		if (artist == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			artist = inflater.inflate(R.layout.list_srch_artist, parent, false);
			
			wrapper = new ArtistSearchWrapper(artist);
			artist.setTag(wrapper);
		} else {
			wrapper = (ArtistSearchWrapper) artist.getTag();
		}
		
		ArtistStub aData = (ArtistStub) resultData.get(position);
		
		wrapper.getArtistName().setText(aData.getName());
		TextView option = wrapper.getDisambiguation();
		
		String disambiguation = aData.getDisambiguation();
		option.setText(disambiguation);
		
		if (disambiguation != null)
			option.setVisibility(View.VISIBLE);
		else 
			option.setVisibility(View.GONE);
		
		return artist;
	}
	
	private class ArtistSearchWrapper {
		
		View base;
		TextView artistName = null;
		TextView disambiguation = null;
		
		ArtistSearchWrapper(View base) {
			this.base = base;
		}
		
		TextView getArtistName() {
			if (artistName == null)
				artistName = (TextView) base.findViewById(R.id.search_artist_name);
			
			return artistName;
		}
		
		TextView getDisambiguation() {
			if (disambiguation == null)
				disambiguation = (TextView) base.findViewById(R.id.search_artist_disambig);
			
			return disambiguation;
		}
	}
	
}
