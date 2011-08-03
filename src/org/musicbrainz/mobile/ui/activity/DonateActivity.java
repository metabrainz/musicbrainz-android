/*
 * Copyright (C) 2010 Jamie McDonald
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

package org.musicbrainz.mobile.ui.activity;

import java.math.BigDecimal;

import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.Log;
import org.musicbrainz.mobile.util.Secrets;

import com.markupartist.android.widget.ActionBar;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Activity to process a donation through PayPal MEP.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class DonateActivity extends SuperActivity implements OnClickListener {

	private ActionBar actionBar;
	private Spinner amount;
	private Button donate;
	
	private PayPal payPal;
	private static final int SERVER = PayPal.ENV_LIVE;
	private static final String APP_ID = Secrets.PAYPAL_APP_ID;
	private static final int REQUEST_CODE = 1;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_donate);
        actionBar = setupActionBarWithHome();
        
        amount = (Spinner) findViewById(R.id.donate_spin);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.donation, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount.setAdapter(typeAdapter);
        donate = (Button) findViewById(R.id.donate_btn);
              
        new LoadTask().execute();
    }
	
	private class LoadTask extends AsyncTask<Void, Void, Void> {
		
		protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
		}
		
		protected Void doInBackground(Void... v) {
			payPal = PayPal.getInstance();
			if (payPal == null) {
			   	payPal = PayPal.initWithAppID(DonateActivity.this, APP_ID, SERVER);
			   	payPal.setShippingEnabled(false);
			}
			return null;
		}
		
		protected void onPostExecute(Void v) {
			actionBar.setProgressBarVisibility(View.GONE);
			enablePayButton();
		}
		
	}
	
	private void enablePayButton() {
		donate.setText(R.string.paypal_label);
		donate.setEnabled(true);
	}
	
	public void onClick(View v) {
		String selection = (String) amount.getSelectedItem();
		float amount = Float.valueOf(selection.substring(1));
		PayPalPayment donation = createPayment(BigDecimal.valueOf(amount));
		
		try {
			Intent checkoutIntent = PayPal.getInstance().checkout(donation, this);
			this.startActivityForResult(checkoutIntent, REQUEST_CODE);
		} catch (Exception e) {
			Log.e("Unhandled exception within PayPal library");
		}
	}
	
	private PayPalPayment createPayment(BigDecimal amount) {
		PayPalPayment donation = new PayPalPayment();
		donation.setSubtotal(amount);
		donation.setCurrencyType("USD");
		donation.setPaymentType(PayPal.PAYMENT_TYPE_NONE);
		donation.setPaymentSubtype(PayPal.PAYMENT_SUBTYPE_DONATIONS);
		donation.setRecipient("paypal@metabrainz.org");
		donation.setMerchantName("MetaBrainz Foundation");
		donation.setDescription("MusicBrainz Donation via Android app");
		return donation;
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.donate, menu);
    	return true;
    }
    
}
