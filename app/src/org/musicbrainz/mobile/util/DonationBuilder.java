package org.musicbrainz.mobile.util;

import java.math.BigDecimal;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class DonationBuilder {
    
    public static final String DONATION_CURRENCY = "USD";
    public static final String DONATION_EMAIL = "paypal@metabrainz.org";
    public static final String DONATION_NAME = "MetaBrainz Foundation";
    public static final String DONATION_DESCRIPTION = "MusicBrainz donation via Android app";
    
    private double amount;
    
    public DonationBuilder(double amount) {
        this.amount = amount;
    }
    
    public PayPalPayment getPayPalPayment() {
        PayPalPayment donation = new PayPalPayment();
        donation.setSubtotal(convertAmountToBigDecimal());
        donation.setCurrencyType(DONATION_CURRENCY);
        donation.setRecipient(DONATION_EMAIL);
        donation.setMerchantName(DONATION_NAME);
        donation.setDescription(DONATION_DESCRIPTION);
        donation.setPaymentType(PayPal.PAYMENT_TYPE_NONE);
        donation.setPaymentSubtype(PayPal.PAYMENT_SUBTYPE_DONATIONS);
        return donation;
    }
    
    private BigDecimal convertAmountToBigDecimal() {
        return BigDecimal.valueOf(amount);
    }

}
