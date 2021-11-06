package org.metabrainz.mobile.presentation.features.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.stripe.android.paymentsheet.PaymentSheet

class PaymentSheetViewModel(application: Application) : AndroidViewModel(application) {
    private val inProgress = MutableLiveData<Boolean>()
    val status = MutableLiveData<String>()
    val exampleCheckoutResponse = MutableLiveData<CheckoutResponse>()

    fun prepareCheckout(backendUrl: String) {
        inProgress.postValue(true)

        Fuel.post(backendUrl)
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        status.postValue("${status.value}\n\nPreparing checkout failed\n" +
                            "${result.getException().message}")
                    }
                    is Result.Success -> {
                        exampleCheckoutResponse.postValue(
                            Gson().fromJson(result.get(), CheckoutResponse::class.java)
                        )
                    }
                }
                inProgress.postValue(false)
            }
    }

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
}
