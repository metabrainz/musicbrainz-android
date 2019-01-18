package org.metabrainz.android.async;

import org.metabrainz.android.App;
import org.metabrainz.android.config.Secrets;

//import com.paypal.android.MEP.PayPal;

import android.os.AsyncTask;

public class LoadPayPalTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        try {
           // PayPal payPal = PayPal.initWithAppID(App.getContext(), Secrets.PAYPAL_APP_ID, PayPal.ENV_LIVE);
            //payPal.setShippingEnabled(false);
        } catch (IllegalStateException e) {
            // Already initialised.
        }
        return null;
    }

}
