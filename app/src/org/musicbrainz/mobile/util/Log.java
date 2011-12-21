/*
 * Copyright (C) 2010 Jamie McDonald
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

package org.musicbrainz.mobile.util;

import org.musicbrainz.mobile.config.Configuration;

public class Log {

    public static final String TAG = Configuration.TAG;

    public static void e(String message) {
        android.util.Log.e(TAG, message);
    }

    public static void d(String message) {
        android.util.Log.d(TAG, message);
    }

    public static void v(String message) {
        android.util.Log.v(TAG, message);
    }

}
