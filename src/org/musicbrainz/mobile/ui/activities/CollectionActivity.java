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

package org.musicbrainz.mobile.ui.activities;

import org.musicbrainz.mobile.R;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

/**
 * Activity to display the user collection as a list of releases.
 * 
 * TODO implement collections browsing when webservice support is available.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class CollectionActivity extends SuperActivity {
	
	private ListView collectionList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_collection);
        
        collectionList = (ListView) findViewById(R.id.collection_list);
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	
    	// disable current Activity menu item
    	menu.findItem(R.id.menu_collection).setEnabled(false);
    	
    	return true;
    }
    
}
