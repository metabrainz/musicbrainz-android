package org.metabrainz.mobile.data.sources.api.entities.payment

import com.stripe.android.paymentsheet.PaymentSheet

data class CheckoutResponse(val publishableKey: String, val paymentIntent: String, val customer: String? = null, val ephemeralKey: String? = null) {
        fun makeCustomerConfig() =
            when {
                customer != null && ephemeralKey != null -> {
                    PaymentSheet.CustomerConfiguration(
                        id = customer,
                        ephemeralKeySecret = ephemeralKey
                    )
                }
                else -> {
                    null
                }
            }
    }