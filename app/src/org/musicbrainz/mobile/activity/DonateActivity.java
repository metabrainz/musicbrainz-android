/*
 * Copyright (C) 2010 Jamie McDonald
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

package org.musicbrainz.mobile.activity;

import java.math.BigDecimal;

import org.musicbrainz.mobile.MBApplication;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.util.Config;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

/**
 * Activity to process a donation to MetaBrainz through the PayPal MPL.
 */
public class DonateActivity extends MusicBrainzActivity implements OnClickListener {
    
    private static final int PAYPAL_REQUEST_CODE = 1;
    private static final int MAX_CHECKS = 20;

    private Spinner amount;
    private CheckoutButton payPalButton;

    private PayPal payPal;
    private Handler handler = new Handler();
    private int initChecks;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_donate);
        setProgressBarIndeterminateVisibility(Boolean.TRUE);

        amount = (Spinner) findViewById(R.id.donate_spin);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.donation,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount.setAdapter(typeAdapter);

        initChecks = 0;
        handler.post(loadingChecker);
    }

    private Runnable loadingChecker = new Runnable() {

        @Override
        public void run() {
            MBApplication app = (MBApplication) getApplication();
            payPal = app.getPayPal();
            if (payPal != null) {
                onPayPalLoaded();
            } else if (initChecks < MAX_CHECKS) {
                handler.postDelayed(this, 500);
                initChecks++;
            } else {
                handleTimeout();
            }
        }
    };

    private void onPayPalLoaded() {
        setProgressBarIndeterminateVisibility(Boolean.FALSE);
        addPayPalButtonToLayout();
        hideLoadingText();
    }

    private void handleTimeout() {
        setProgressBarIndeterminateVisibility(Boolean.FALSE);
        TextView loadText = (TextView) findViewById(R.id.donate_loading_text);
        loadText.setText(R.string.paypal_loading_timeout);
    }

    private void hideLoadingText() {
        TextView loadText = (TextView) findViewById(R.id.donate_loading_text);
        loadText.setVisibility(View.GONE);
    }

    private void addPayPalButtonToLayout() {
        payPalButton = payPal.getCheckoutButton(DonateActivity.this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
        payPalButton.setLayoutParams(configureButtonParams());
        payPalButton.setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.donate_layout)).addView(payPalButton);
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

        switch (id) {
        case R.id.alt_donate_link:
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.DONATE_LINK)));
            break;
        default:
            String selection = (String) amount.getSelectedItem();
            float amount = Float.valueOf(selection.substring(1));
            PayPalPayment donation = createPayment(BigDecimal.valueOf(amount));

            Intent checkoutIntent = PayPal.getInstance().checkout(donation, this);
            this.startActivityForResult(checkoutIntent, PAYPAL_REQUEST_CODE);
            setContentView(R.layout.layout_starting_paypal);
        }
    }

    private PayPalPayment createPayment(BigDecimal amount) {
        PayPalPayment donation = new PayPalPayment();
        donation.setSubtotal(amount);
        donation.setCurrencyType(Config.DONATION_CURRENCY);
        donation.setPaymentType(PayPal.PAYMENT_TYPE_NONE);
        donation.setPaymentSubtype(PayPal.PAYMENT_SUBTYPE_DONATIONS);
        donation.setRecipient(Config.DONATION_EMAIL);
        donation.setMerchantName(Config.DONATION_NAME);
        donation.setDescription(Config.DONATION_DESCRIPTION);
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

}
