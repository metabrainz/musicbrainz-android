/*
 * Copyright (C) 2011 Jamie McDonald
 * 
 * This file is part of MusicBrainz Mobile (Android).
 * 
 * MusicBrainz Mobile (Android) is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * MusicBrainz Mobile (Android) is distributed in the hope that it 
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MusicBrainz Mobile (Android). If not, see 
 * <http://www.gnu.org/licenses/>.
 */

package org.musicbrainz.mobile;

import org.musicbrainz.mobile.util.Secrets;

import com.paypal.android.MEP.PayPal;

import android.app.Application;

/**
 * Application starts initialising PayPal in the background when the app is
 * created. This prevents the user from having to wait when they visit the
 * donation page for the first time.
 */
public class MBApplication extends Application {
	
	public PayPal payPal;
	
	@Override
	public void onCreate() {
		super.onCreate();
		new LoadPayPalThread().start();	
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
	
}
