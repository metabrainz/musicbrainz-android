package org.metabrainz.mobile.presentation.features.barcode;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.repository.BarcodeLookupRepository;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import java.util.List;

public class BarcodeViewModel extends ViewModel {

    private final BarcodeLookupRepository barcodeLookupRepository = BarcodeLookupRepository.getRepository();
    private MutableLiveData<List<Release>> barcodeLiveData;

    public BarcodeViewModel() {
        super();
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
        BarcodeLookupRepository.destroyRepository();
    }
}
