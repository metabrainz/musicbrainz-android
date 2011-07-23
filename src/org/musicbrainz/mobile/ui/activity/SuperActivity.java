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
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.ws.WSUser;

import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Abstract class to represent items common to multiple Activity classes. The
 * menu which is inflated depends on user login status.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public abstract class SuperActivity extends Activity {

	protected boolean loggedIn = false;
	
	/*
	 * Get login status from shared preferences file and set local status.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		if (!Config.DEV)
			ExceptionHandler.register(this, "http://www.jdamcd.com/mbbugs/server.php");
		
		SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
		String user = prefs.getString("username", null); 
		
		if (user != null) 
			loggedIn = true; 
	}

	/**
	 * Create a webservice user from previously authenticated
	 * credentials stored in preferences.
	 * 
	 * @return Webservice user.
	 */
	protected WSUser getUser() {
		
		SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
		String username = prefs.getString("username", null);
		String password = prefs.getString("password", null);
		
		WSUser poster = new WSUser(username, password);
		return poster;
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {

    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.general, menu);
    
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	
        switch(item.getItemId()) {
        case R.id.menu_about:
        	Intent aboutIntent = new Intent(this, AboutActivity.class);
			startActivity(aboutIntent);
        	return true;
        case R.id.menu_donate:
        	Intent donateIntent = new Intent(this, DonateActivity.class);
			startActivity(donateIntent);
        	return true;
        }
        
        return false;
    }
    
}
