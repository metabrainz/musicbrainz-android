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

package org.musicbrainz.mobile.ui.activity;

import org.musicbrainz.mobile.R;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * Activity to display information about the application.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class AboutActivity extends SuperActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_about);
        
		try {
			TextView versionLabel = (TextView) findViewById(R.id.version_text);
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versionLabel.setText(getText(R.string.version_text) + " " + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	
    	menu.findItem(R.id.menu_about).setEnabled(false);  	
    	return true;
    }
    
}
