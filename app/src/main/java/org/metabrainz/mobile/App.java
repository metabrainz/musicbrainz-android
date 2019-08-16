package org.metabrainz.mobile;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Environment;

import org.metabrainz.mobile.presentation.Configuration;

public class App extends Application {

    private static App instance;
    private static Typeface robotoLight;
    public static final int DIRECTORY_SELECT_REQUEST_CODE = 0;
    public static final int AUDIO_FILE_REQUEST_CODE = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final String EXTRA_FILE_PATH = "file_path";
    public static final String TAGGER_ROOT_DIRECTORY = Environment.getExternalStorageDirectory() + "/Picard/";

    public static String getUserAgent() {
        return Configuration.USER_AGENT + "/" + getVersion();
    }

    public static String getClientId() {
        return Configuration.CLIENT_NAME + "-" + getVersion();
    }

    public static String getVersion() {
        try {
            return instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "unknown";
        }
    }

    public static App getContext() {
        return instance;
    }

    public static Typeface getRobotoLight() {
        return robotoLight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loadCustomTypefaces();
    }

    private void loadCustomTypefaces() {
        robotoLight = Typeface.createFromAsset(instance.getAssets(), "Roboto-Light.ttf");
    }

}
