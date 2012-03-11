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
import org.musicbrainz.mobile.fragment.DonateFragment.DonationCallbacks;

import android.os.Bundle;

import com.actionbarsherlock.view.Window;

public class DonateActivity extends MusicBrainzActivity implements DonationCallbacks {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_donate);
    }
    
    @Override
    public void onStartLoading() {
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onFinishedLoading() {
        setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onResult() {
        finish();
    }

}
