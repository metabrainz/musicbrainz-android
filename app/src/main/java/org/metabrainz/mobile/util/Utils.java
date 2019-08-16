package org.metabrainz.mobile.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    public static String[] getPermissionsList(Context context) {
        boolean readStoragePermission, writeStoragePermission;
        readStoragePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        writeStoragePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        ArrayList<String> permissionsList = new ArrayList<>();
        if (!readStoragePermission)
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!writeStoragePermission)
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String[] permissions = new String[permissionsList.size()];
        permissions = permissionsList.toArray(permissions);
        return permissions;
    }
}
