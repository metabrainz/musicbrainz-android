/*
 * Copyright (C) 2011 Jamie McDonald
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

package org.musicbrainz.mobile.adapter;

import java.util.List;

import org.musicbrainz.android.api.data.WebLink;
import org.musicbrainz.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List adapter for web links.
 */
public class LinkAdapter extends ArrayAdapter<WebLink> {
	
	private Activity context;
	private List<WebLink> links;
	
	public LinkAdapter(Activity context, List<WebLink> links) {
		super(context, R.layout.list_link, links);
		this.context = context;
		this.links = links;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		View link = convertView;
		LinkHolder holder = null;
		
		if (link == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			link = inflater.inflate(R.layout.list_link, parent, false);
			holder = new LinkHolder(link);
			link.setTag(holder);
		} else {
			holder = (LinkHolder) link.getTag();
		}
		
		WebLink linkData = links.get(position);
		holder.getLinkTitle().setText(linkData.getPrettyType());
		holder.getLink().setText(linkData.getPrettyUrl());
		return link;
	}
	
	private class LinkHolder {
		
		View base;
		TextView linkTitle = null;
		TextView link = null;
		
		LinkHolder(View base) {
			this.base = base;
		}
		
		TextView getLinkTitle() {
			if (linkTitle == null) {
				linkTitle = (TextView) base.findViewById(R.id.list_link_title);
			}
			return linkTitle;
		}
		
		TextView getLink() {
			if (link == null) {
				link = (TextView) base.findViewById(R.id.list_link);
			}
			return link;
		}
	}
	
}
