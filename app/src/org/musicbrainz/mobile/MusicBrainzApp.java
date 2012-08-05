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

package org.musicbrainz.mobile;

import org.musicbrainz.android.api.util.Credentials;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.config.Secrets;
import org.musicbrainz.mobile.util.PreferenceUtils;
import org.musicbrainz.mobile.util.PreferenceUtils.Pref;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import com.bugsense.trace.BugSenseHandler;
import com.paypal.android.MEP.PayPal;

/**
 * Application starts initialising PayPal in the background when the app is
 * created. This prevents the user from having to wait when they visit the
 * donation page for the first time.
 */
public class MusicBrainzApp extends Application {
    
    private static MusicBrainzApp instance;

    private boolean isUserLoggedIn;
    private PayPal payPal;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new LoadPayPalThread().start();

        if (PreferenceUtils.getUsername() != null) {
            isUserLoggedIn = true;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (Configuration.LIVE && prefs.getBoolean(Pref.PREF_BUGSENSE, true)) {
            BugSenseHandler.setup(this, Secrets.BUGSENSE_API_KEY);
        }
    }

    public PayPal getPayPal() {
        return payPal;
    }

    private class LoadPayPalThread extends Thread {

        @Override
        public void run() {
            initialisePayPal();
        }

        private void initialisePayPal() {
            payPal = PayPal.initWithAppID(getApplicationContext(), Secrets.PAYPAL_APP_ID, PayPal.ENV_LIVE);
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
    
    public static MusicBrainzApp getContext() {
        return instance;
    }

}
