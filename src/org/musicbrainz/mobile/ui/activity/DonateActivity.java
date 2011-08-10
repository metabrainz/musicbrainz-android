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
import org.musicbrainz.mobile.util.Config;
import org.musicbrainz.mobile.util.Secrets;

import com.markupartist.android.widget.ActionBar;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to process a donation through PayPal MEP.
 * 
 * @author Jamie McDonald - jdamcd@gmail.com
 */
public class DonateActivity extends SuperActivity implements OnClickListener {

	private ActionBar actionBar;
	private Spinner amount;
	private CheckoutButton payPalButton;
	
	private PayPal payPal;
	private static final int SERVER = PayPal.ENV_LIVE;
	private static final String APP_ID = Secrets.PAYPAL_APP_ID;
	private static final int PAYPAL_REQUEST_CODE = 1;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_donate);
        actionBar = setupActionBarWithHome();
        
        amount = (Spinner) findViewById(R.id.donate_spin);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.donation, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount.setAdapter(typeAdapter);
              
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
			addPayPalButtonToLayout();
			hideLoadingText();
		}
		
	}
	
	private void hideLoadingText() {
		TextView loading = (TextView) findViewById(R.id.donate_loading_text);
		loading.setVisibility(View.GONE);
	}
	
	private void addPayPalButtonToLayout() {
		payPalButton = payPal.getCheckoutButton(DonateActivity.this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
		payPalButton.setLayoutParams(configureButtonParams()); 
		payPalButton.setOnClickListener(this);
		((RelativeLayout)findViewById(R.id.donate_layout)).addView(payPalButton);
	}
	
	private LayoutParams configureButtonParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.donate_spin);
		params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.donate_spin);
		params.addRule(RelativeLayout.ALIGN_LEFT, R.id.donate_spin);
		params.leftMargin = 4;
		params.rightMargin = 4;
		return params;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch(id) {
		case R.id.alt_donate_link:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.DONATE_LINK)));
			break;
		default:
			String selection = (String) amount.getSelectedItem();
			float amount = Float.valueOf(selection.substring(1));
			PayPalPayment donation = createPayment(BigDecimal.valueOf(amount));
			
			Intent checkoutIntent = PayPal.getInstance().checkout(donation, this);
			this.startActivityForResult(checkoutIntent, PAYPAL_REQUEST_CODE);
			setContentView(R.layout.starting_paypal);
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == PAYPAL_REQUEST_CODE) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				Toast.makeText(this, R.string.toast_donate_success, Toast.LENGTH_SHORT).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(this, R.string.toast_donate_canceled, Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.donate, menu);
    	return true;
    }
    
}
