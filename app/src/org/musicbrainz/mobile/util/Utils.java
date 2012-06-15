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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * A set of fairly general Android utility methods.
 */
public class Utils {
    
    public static Intent shareIntent(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
        return intent.putExtra(Intent.EXTRA_TEXT, text);
    }
    
    public static Intent emailIntent(String recipient, String subject) {
        Uri uri = Uri.parse("mailto:" + recipient);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        return intent;
    }
    
    public static Intent urlIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
    
    public static String stringFromAsset(Context context, String asset) {
        try {
            InputStream input = context.getResources().getAssets().open(asset);
            byte[] buffer = new byte[input.available()];  
            input.read(buffer);  
            ByteArrayOutputStream output = new ByteArrayOutputStream();  
            output.write(buffer);  
            output.close();  
            input.close(); 
            return output.toString();
        } catch (IOException e) {
            Log.e("Error reading text file from assets folder.");
            return "";
        }
    }

}
