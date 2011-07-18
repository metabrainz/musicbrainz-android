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

package org.musicbrainz.mobile.ui.activities;

import org.musicbrainz.mobile.R;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
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

	private Spinner amount;
	private Button donate;
	
	private PayPal payPal;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_donate);
        
        // initialise Spinner values from XML resource
        amount = (Spinner) findViewById(R.id.donate_spin);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.donation, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount.setAdapter(typeAdapter);
        
        donate = (Button) findViewById(R.id.donate_btn);
		donate.setOnClickListener(DonateActivity.this);
        donate.setEnabled(false);
              
        new LoadTask().execute();
    }
	
	private class LoadTask extends AsyncTask<Void, Void, Void> {
		
		protected void onPreExecute() {
			
			setProgressBarIndeterminateVisibility(true);
		}
		
		protected Void doInBackground(Void... v) {
			
			// TODO need an app ID, registered from MB PayPal
			// ENV_NONE seems to actually be the sandbox environment?
			payPal = PayPal.getInstance();
			if (payPal == null) {
			   	payPal = PayPal.initWithAppID(DonateActivity.this.getBaseContext(), "APP-80W284485P519543T", PayPal.ENV_NONE);
			    payPal.setLang("en_US"); // TODO set language dynamically
			}
		    
			return null;
		}
		
		protected void onPostExecute(Void v) {
			
			setProgressBarIndeterminateVisibility(false);
			donate.setEnabled(true);
		}
		
	}
	
    /*
     * Listener for donate button.
     */
	public void onClick(View v) {
		
		// get Spinner value
		String amtString = (String) amount.getSelectedItem();
		float amt = Float.valueOf(amtString.substring(1));
	
		// create payment
		PayPalPayment donation = new PayPalPayment();
		donation.setAmount(amt);
		donation.setCurrency("USD");
		donation.setRecipient("paypal@metabrainz.org");
		donation.setItemDescription("MusicBrainz Donation via Android");
		donation.setMerchantName("MetaBrainz Foundation");
		
		// check out
		Intent checkoutIntent = new Intent(this, PayPalActivity.class);
		checkoutIntent.putExtra(PayPalActivity.EXTRA_PAYMENT_INFO, donation);
		this.startActivityForResult(checkoutIntent, 1);
	}
    
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	
    	// disable current Activity menu item
    	menu.findItem(R.id.menu_donate).setEnabled(false);
    	
    	return true;
    }
    
}
