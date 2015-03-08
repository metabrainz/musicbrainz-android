package org.musicbrainz.mobile;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.loader.Loader;
import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.user.UserPreferences;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;

public class App extends Application {

    private static App instance;
    private static ImageManager imageManager;
    private static Typeface robotoLight;
    private static UserPreferences user;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        user = new UserPreferences();
        setupImageManager();
        loadCustomTypefaces();
    }

    public void setupImageManager() {
        LoaderSettings settings = new LoaderSettings.SettingsBuilder().withDisconnectOnEveryCall(true).build(this);
        imageManager = new ImageManager(this, settings);
    }

    private void loadCustomTypefaces() {
        robotoLight = Typeface.createFromAsset(instance.getAssets(), "Roboto-Light.ttf");
    }

    public static Loader getImageLoader() {
        return imageManager.getLoader();
    }

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

    public static UserPreferences getUser() {
        return user;
    }

    public static boolean isUserLoggedIn() {
        return user.isLoggedIn();
    }

    public static MusicBrainz getWebClient() {
        if (user.isLoggedIn()) {
            return new MusicBrainzWebClient(user, getUserAgent(), getClientId());
        } else {
            return new MusicBrainzWebClient(getUserAgent());
        }
    }

    public static Typeface getRobotoLight() {
        return robotoLight;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        imageManager.getFileManager().clean();
    }

}
