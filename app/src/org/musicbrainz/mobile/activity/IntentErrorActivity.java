/*
 * Copyright (C) 2011 Jamie McDonald
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

import android.os.Bundle;
import android.widget.TextView;

/**
 * This Activity displays an error message to the user when an incoming URI in
 * not valid.
 */
public class IntentErrorActivity extends SuperActivity {

    public static final String INTENT_MESSAGE = "message";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intenterror);
        setupActionBarWithHome();

        String message = getIntent().getStringExtra(INTENT_MESSAGE);
        TextView errorMessage = (TextView) findViewById(R.id.error_body);
        errorMessage.setText(getString(R.string.error_body) + message);
    }

}
