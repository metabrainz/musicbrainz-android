package org.metabrainz.mobile.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.stripe.android.paymentsheet.PaymentSheet
import org.metabrainz.mobile.data.sources.api.PaymentService
import org.metabrainz.mobile.data.sources.api.entities.payment.CheckoutResponse
import org.metabrainz.mobile.presentation.features.dashboard.PaymentViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.SUCCESS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(val service: PaymentService) : PaymentRepository{

    @WorkerThread
    override suspend fun fetchPaymentIntent(url: String): Resource<CheckoutResponse> {
        return try {
            val result = service.getPaymentIntent(url)
            Resource(SUCCESS, result)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.failure()
        }
    }
}