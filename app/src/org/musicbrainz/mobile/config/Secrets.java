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

package org.musicbrainz.mobile.config;

/**
 * These values are required to build the app but they are simply debug/sandbox
 * values. Release builds use a file with a different set of values for obvious
 * reasons.
 */
public class Secrets {

    public static final String BUGSENSE_API_KEY = "99f3740e";
    public static final String PAYPAL_APP_ID = "APP-80W284485P519543T";

    public String getKey() {
        return "aplaceholderstring";
    }

}
