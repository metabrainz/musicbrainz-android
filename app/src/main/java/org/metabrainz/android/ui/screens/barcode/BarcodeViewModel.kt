package org.metabrainz.android.ui.screens.barcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.repository.BarcodeLookupRepository
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(private var barcodeLookupRepository: BarcodeLookupRepository) : ViewModel() {

    fun fetchReleasesWithBarcode(barcode: String): LiveData<Resource<List<Release>>> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(barcodeLookupRepository.lookupReleasesWithBarcode("barcode:$barcode"))
        }
    }

}