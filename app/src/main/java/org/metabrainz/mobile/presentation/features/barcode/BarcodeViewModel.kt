package org.metabrainz.mobile.presentation.features.barcode

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import org.metabrainz.mobile.data.repository.BarcodeLookupRepository
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.Resource

class BarcodeViewModel @ViewModelInject constructor(private var barcodeLookupRepository: BarcodeLookupRepository) : ViewModel() {

    fun fetchReleasesWithBarcode(barcode: String): LiveData<Resource<List<Release>>> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(barcodeLookupRepository.lookupReleasesWithBarcode("barcode:$barcode"))
        }
    }

}