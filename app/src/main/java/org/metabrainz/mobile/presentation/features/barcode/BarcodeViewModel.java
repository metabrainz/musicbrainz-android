package org.metabrainz.mobile.presentation.features.barcode;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.BarcodeLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

public class BarcodeViewModel extends ViewModel {

    private BarcodeLookupRepository barcodeLookupRepository;
    private MutableLiveData<List<Release>> barcodeLiveData;

    @ViewModelInject
    public BarcodeViewModel(BarcodeLookupRepository repository) {
        this.barcodeLookupRepository = repository;
    }

    public MutableLiveData<List<Release>> getBarcodeLiveData() {
        if (barcodeLiveData == null) barcodeLiveData = barcodeLookupRepository.getReleaseListData();
        return barcodeLiveData;
    }

    public void fetchReleasesWithBarcode(String barcode) {
        barcodeLookupRepository.lookupReleasesWithBarcode("barcode:" + barcode);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.barcodeLookupRepository = null;
    }
}
