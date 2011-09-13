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

package org.musicbrainz.mobile.dialog;

import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.activity.ReleaseActivity;
import org.musicbrainz.mobile.adapter.ReleaseStubAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Dialog which allows the user to choose a specific release when a release
 * group contains more than one release.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class ReleaseSelectionDialog extends Dialog implements ListView.OnItemClickListener {
	
	private ReleaseActivity parent;
	private LinkedList<ReleaseStub> data;
	private ListView releaseList;

	public ReleaseSelectionDialog(Context context, LinkedList<ReleaseStub> data) {
		super(context);
		
		parent = (ReleaseActivity) context;
		this.data = data;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_rg);
		
		releaseList = (ListView) findViewById(R.id.rg_release_list);
		releaseList.setOnItemClickListener(this);
		releaseList.setAdapter(new ReleaseStubAdapter(parent, data));
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		ReleaseStub r = data.get(position);
		
		Intent releaseIntent = new Intent(this.parent, ReleaseActivity.class);
		releaseIntent.putExtra("r_id", r.getReleaseMbid());
		this.parent.startActivity(releaseIntent);
		this.parent.finish();
		dismiss();
	}
	
	/**
	 * Overrides the Dialog cancel() method to finish the parent Activity on
	 * cancel.
	 */
	public void cancel() {
		super.cancel();
		parent.finish();
	}
	
	@Override
	public boolean onSearchRequested() {
		return false;
	}
	
}
