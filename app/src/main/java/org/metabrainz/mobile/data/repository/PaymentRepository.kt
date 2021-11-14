package org.metabrainz.mobile.data.repository

import org.metabrainz.mobile.data.sources.api.entities.payment.CheckoutResponse
import org.metabrainz.mobile.util.Resource

interface PaymentRepository {
    suspend fun fetchPaymentIntent(url: String): Resource<CheckoutResponse>
}