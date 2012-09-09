package org.musicbrainz.mobile;

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.util.PreferenceUtils;
import org.musicbrainz.mobile.util.PreferenceUtils.Pref;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.bugsense.trace.BugSenseHandler;
import com.paypal.android.MEP.PayPal;

/**
 * Application starts initialising PayPal in the background when the app is
 * created. This prevents the user from having to wait when they visit the
 * donation page for the first time.
 */
public class App extends Application {
    
    private static App instance;
    private static Typeface robotoLight;

    private boolean isUserLoggedIn;
    private static boolean isPayPalLoaded;
    private static PayPal payPal;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;        
        if (PreferenceUtils.getUsername() != null) {
            isUserLoggedIn = true;
        }
        setupPayPal();
        setupCrashLogging();
        loadCustomTypefaces();
    }

    private void setupPayPal() {
        if (!isPayPalLoaded) {
            isPayPalLoaded = true;
            new LoadPayPalThread().start();
        }
    }

    private void setupCrashLogging() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (Configuration.LIVE && prefs.getBoolean(Pref.PREF_BUGSENSE, true)) {
            BugSenseHandler.setup(this, Secrets.BUGSENSE_API_KEY);
        }
    }
    
    private void loadCustomTypefaces() {
        robotoLight = Typeface.createFromAsset(instance.getAssets(), "Roboto-Light.ttf");
    }

    public static PayPal getPayPal() {
        return payPal;
    }

    private static class LoadPayPalThread extends Thread {

        @Override
        public void run() {
            initialisePayPal();
        }

        private void initialisePayPal() {
            payPal = PayPal.initWithAppID(instance, Secrets.PAYPAL_APP_ID, PayPal.ENV_LIVE);
            payPal.setShippingEnabled(false);
        }
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

    public static Credentials getCredentials() {
        return new Credentials(getUserAgent(), PreferenceUtils.getUsername(), PreferenceUtils.getPassword(),
                getClientId());
    }
    
    public static boolean isUserLoggedIn() {
        return instance.isUserLoggedIn;
    }
    
    public static void updateLoginStatus(boolean isUserLoggedIn) {
        instance.isUserLoggedIn = isUserLoggedIn;
    }
    
    public static App getContext() {
        return instance;
    }
    
    public static Typeface getRobotoLight() {
        return robotoLight;
    }

}
