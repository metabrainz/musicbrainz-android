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

package org.musicbrainz.mobile.util;

import java.math.BigDecimal;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class MetaBrainzDonation {
    
    public static final String DONATION_CURRENCY = "USD";
    public static final String DONATION_EMAIL = "paypal@metabrainz.org";
    public static final String DONATION_NAME = "MetaBrainz Foundation";
    public static final String DONATION_DESCRIPTION = "MusicBrainz donation via Android app";
    
    private double amount;
    
    public MetaBrainzDonation(double amount) {
        this.amount = amount;
    }
    
    private BigDecimal getAmountAsBigDecimal() {
        return BigDecimal.valueOf(amount);
    }
    
    public PayPalPayment getPayPalPayment() {
        PayPalPayment donation = new PayPalPayment();
        donation.setSubtotal(getAmountAsBigDecimal());
        donation.setCurrencyType(DONATION_CURRENCY);
        donation.setRecipient(DONATION_EMAIL);
        donation.setMerchantName(DONATION_NAME);
        donation.setDescription(DONATION_DESCRIPTION);
        donation.setPaymentType(PayPal.PAYMENT_TYPE_NONE);
        donation.setPaymentSubtype(PayPal.PAYMENT_SUBTYPE_DONATIONS);
        return donation;
    }

}
