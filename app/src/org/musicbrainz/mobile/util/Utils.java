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
    
    public static Intent shareIntent(String text) {
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
