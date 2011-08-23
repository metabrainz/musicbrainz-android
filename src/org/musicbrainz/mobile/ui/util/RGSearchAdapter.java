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

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.ReleaseGroup;
import org.musicbrainz.mobile.ui.activity.SearchActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for list of release group search results.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class RGSearchAdapter extends ArrayAdapter<ReleaseGroup> {
	
	SearchActivity context;
	List<ReleaseGroup> resultData;

	public RGSearchAdapter(Context context, List<ReleaseGroup> resultData) {
		super(context, R.layout.list_srch_rg, resultData);
		
		this.context = (SearchActivity) context;
		this.resultData = resultData;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View release = convertView; // reuse View
		ReleaseSearchWrapper wrapper = null;
		
		if (release == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			release = inflater.inflate(R.layout.list_srch_rg, parent, false);
			
			wrapper = new ReleaseSearchWrapper(release);
			release.setTag(wrapper);
		} else {
			wrapper = (ReleaseSearchWrapper) release.getTag();
		}
		
		ReleaseGroup rgData = (ReleaseGroup) resultData.get(position);
		
		wrapper.getTitle().setText(rgData.getTitle());
		wrapper.getArtist().setText(rgData.getFormattedArtist());	
		wrapper.getType().setText(rgData.getFormattedType(getContext()));
		
		return release;
	}
	
	/**
	 * Holder pattern minimises executions of findViewById().
	 */
	private class ReleaseSearchWrapper {
		
		View base;
		TextView title = null;
		TextView artist = null;
		TextView type = null;
		
		ReleaseSearchWrapper(View base) {
			this.base = base;
		}
		
		TextView getTitle() {
			if (title == null)
				title = (TextView) base.findViewById(R.id.search_release);
			
			return title;
		}
		
		TextView getArtist() {
			if (artist == null)
				artist = (TextView) base.findViewById(R.id.search_release_artist);
			
			return artist;
		}
		
		TextView getType() {
			if (type == null)
				type = (TextView) base.findViewById(R.id.search_release_type);
			
			return type;
		}
	}
	
}
