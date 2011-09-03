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

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for a list of ReleaseStubs.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseStubAdapter extends ArrayAdapter<ReleaseStub> {
	
	Activity context;
	List<ReleaseStub> stubs;

	public ReleaseStubAdapter(Context context, List<ReleaseStub> stubs) {
		super(context, R.layout.list_release, stubs);
		
		this.context = (Activity) context;
		this.stubs = stubs;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View release = convertView; // reuse View
		ReleaseStubWrapper wrapper = null;
		
		if (release == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			release = inflater.inflate(R.layout.list_release, parent, false);
			
			wrapper = new ReleaseStubWrapper(release);
			release.setTag(wrapper);
		} else {
			wrapper = (ReleaseStubWrapper) release.getTag();
		}
		
		ReleaseStub stub = (ReleaseStub) stubs.get(position);
		
		wrapper.getTitle().setText(stub.getTitle());
		wrapper.getArtist().setText(stub.getArtistName());
		
		wrapper.getTrackNum().setText(stub.getTracksNum() + " tracks");
		wrapper.getFormat().setText(StringMapper.buildReleaseFormatsString(getContext(), stub.getFormats()));
		
		wrapper.getLabels().setText(stub.getLabels());
		wrapper.getCountry().setText(stub.getCountryCode());
		wrapper.getDate().setText(stub.getDate());
		
		return(release);
	}
	
	/*
	 * Holder pattern minimises executions of findViewById().
	 */
	private class ReleaseStubWrapper {
		
		View base;
		TextView title = null;
		TextView artist = null;
		
		TextView tracksNum = null;
		TextView formats = null;
		
		TextView labels = null;
		TextView date = null;
		TextView country = null;
		
		ReleaseStubWrapper(View base) {
			this.base = base;
		}
		
		TextView getTitle() {
			if (title == null)
				title = (TextView) base.findViewById(R.id.list_release);
			
			return title;
		}
		
		TextView getArtist() {
			if (artist == null)
				artist = (TextView) base.findViewById(R.id.list_release_artist);
			
			return artist;
		}
		
		TextView getTrackNum() {
			if (tracksNum == null)
				tracksNum = (TextView) base.findViewById(R.id.list_release_tracksnum);
			
			return tracksNum;
		}
		
		TextView getFormat() {
			if (formats == null)
				formats = (TextView) base.findViewById(R.id.list_release_formats);
			
			return formats;
		}
		
		TextView getLabels() {
			if (labels == null)
				labels = (TextView) base.findViewById(R.id.list_release_labels);
			
			return labels;
		}
		
		TextView getCountry() {
			if (country == null)
				country = (TextView) base.findViewById(R.id.list_release_country);
			
			return country;
		}
		
		TextView getDate() {
			if (date == null)
				date = (TextView) base.findViewById(R.id.list_release_date);
			
			return date;
		}
		
	}
	
}
