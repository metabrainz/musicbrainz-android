package org.metabrainz.mobile.presentation.features.dashboard

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.PaymentRepository
import org.metabrainz.mobile.data.sources.api.entities.payment.CheckoutResponse
import org.metabrainz.mobile.util.Resource
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(val repository: PaymentRepository) : ViewModel() {

    fun fetchPaymentIntent(backendUrl: String): LiveData<Resource<CheckoutResponse>> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(repository.fetchPaymentIntent(backendUrl))
        }
    }
}
