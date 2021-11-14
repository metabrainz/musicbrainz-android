package org.metabrainz.mobile.data.sources.api

import org.metabrainz.mobile.data.sources.api.entities.payment.CheckoutResponse
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentService {
    @POST
    suspend fun getPaymentIntent(@Url url: String): CheckoutResponse
}