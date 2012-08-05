/*
 * Copyright (C) 2012 Jamie McDonald
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

package org.musicbrainz.mobile.activity;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.PreferenceUtils;

import android.os.Bundle;

public class CollectionListActivity extends MusicBrainzActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        setUserNameTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUserNameTitle() {
        String userName = PreferenceUtils.getUsername();
        getSupportActionBar().setTitle(userName + getString(R.string.plural) + " " + getString(R.string.collections));
    }

}
