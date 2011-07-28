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

package org.musicbrainz.mobile.ui.dialog;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.data.ReleaseStub;
import org.musicbrainz.mobile.ui.activity.BarcodeSearchActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Dialog that confirms the release to which the user is submitting the barcode.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class BarcodeConfirmDialog extends Dialog implements View.OnClickListener {
	
	private BarcodeSearchActivity parent;
	private ReleaseStub rs;

	public BarcodeConfirmDialog(Context context, ReleaseStub rs) {
		super(context);
		
		this.rs = rs;
		parent = (BarcodeSearchActivity) context;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_bar_submit);
		
		Button confirm = (Button) findViewById(R.id.barcode_confirm);
		confirm.setOnClickListener(this);
		
		((TextView) findViewById(R.id.list_release)).setText(rs.getTitle());
		((TextView) findViewById(R.id.list_release_artist)).setText(rs.getArtistName());
		
		((TextView) findViewById(R.id.list_release_tracksnum)).setText(rs.getTracksNum() + " tracks");
		((TextView) findViewById(R.id.list_release_formats)).setText(rs.getFormattedFormats(getContext()));
		
		((TextView) findViewById(R.id.list_release_labels)).setText(rs.getLabels());
		((TextView) findViewById(R.id.list_release_date)).setText(rs.getDate());
		((TextView) findViewById(R.id.list_release_country)).setText(rs.getCountryCode());
		
		findViewById(R.id.release_box).setBackgroundResource(R.color.list_bg);
		
	}

	public void onClick(View v) {
			
		String releaseID = rs.getReleaseMbid();
		parent.submitBarcode(releaseID);
		parent.finish();
		dismiss();
	}

}
