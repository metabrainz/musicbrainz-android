package org.musicbrainz.mobile;

import org.musicbrainz.mobile.util.Secrets;

import com.paypal.android.MEP.PayPal;

import android.app.Application;

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
    		payPal = PayPal.initWithAppID(getApplicationContext(), Secrets.PAYPAL_APP_ID, PayPal.ENV_LIVE);
    		payPal.setShippingEnabled(false);
        }
    }
	
}
