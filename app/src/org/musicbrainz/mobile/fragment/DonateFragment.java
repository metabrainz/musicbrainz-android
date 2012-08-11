/*
 * Copyright (C) 2012 Jamie McDonald
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

package org.musicbrainz.mobile.fragment;

import org.musicbrainz.mobile.MusicBrainzApp;
import org.musicbrainz.mobile.R;
import org.musicbrainz.mobile.config.Configuration;
import org.musicbrainz.mobile.intent.IntentFactory;
import org.musicbrainz.mobile.util.MetaBrainzDonation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class DonateFragment extends ContextFragment implements OnClickListener {
    
    private static final int PAYPAL_REQUEST_CODE = 1;
    private static final int MAX_CHECKS = 20;
    
    private View layout;
    private Spinner amount;
    private CheckoutButton payPalButton;
    
    private PayPal payPal;
    private Handler handler = new Handler();
    private int initChecks;
    
    private DonationCallbacks donationCallbacks;
    
    public interface DonationCallbacks {
        public void startLoading();
        public void stopLoading();
        public void onResult();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            donationCallbacks = (DonationCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + DonationCallbacks.class.getSimpleName());
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_donate, container);
        amount = (Spinner) layout.findViewById(R.id.donate_spin);
        layout.findViewById(R.id.donate_options).setOnClickListener(this);
        return layout;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        donationCallbacks.startLoading();
        setupAmountsSpinner();
        handler.post(loadingChecker);
    }

    public void setupAmountsSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.donation,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount.setAdapter(typeAdapter);
    }
    
    private Runnable loadingChecker = new Runnable() {

        @Override
        public void run() {
            MusicBrainzApp app = (MusicBrainzApp) context;
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
        donationCallbacks.stopLoading();
        hideLoadingText();
        addPayPalButtonToLayout();
    }

    private void handleTimeout() {
        donationCallbacks.stopLoading();
        TextView loadText = (TextView) layout.findViewById(R.id.donate_loading_text);
        loadText.setText(R.string.paypal_loading_timeout);
    }

    private void hideLoadingText() {
        TextView loadText = (TextView) layout.findViewById(R.id.donate_loading_text);
        loadText.setVisibility(View.GONE);
    }

    private void addPayPalButtonToLayout() {
        payPalButton = payPal.getCheckoutButton(context, PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
        payPalButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        payPalButton.setOnClickListener(this);
        LinearLayout donateButtonLayout = (LinearLayout) layout.findViewById(R.id.donate_button_layout);
        donateButtonLayout.addView(payPalButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.donate_options:
            startActivity(IntentFactory.getWebView(context, R.string.web_donate, Configuration.URL_DONATE));
            break;
        default:
            String selection = (String) amount.getSelectedItem();
            double amount = Double.valueOf(selection.substring(1));
            PayPalPayment donation = new MetaBrainzDonation(amount).getPayPalPayment();
            Intent checkoutIntent = PayPal.getInstance().checkout(donation, context);
            startActivityForResult(checkoutIntent, PAYPAL_REQUEST_CODE);
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            switch (resultCode) {
            case Activity.RESULT_OK:
                Toast.makeText(context, R.string.toast_donate_success, Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, R.string.toast_donate_canceled, Toast.LENGTH_SHORT).show();
            }
            donationCallbacks.onResult();
        }
    }

}
