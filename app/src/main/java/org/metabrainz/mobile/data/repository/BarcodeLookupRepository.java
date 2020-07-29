package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.response.BarcodeReleaseResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class BarcodeLookupRepository {

    private final LookupService service;
    private static MutableLiveData<List<Release>> releaseListData;

    @Inject
    public BarcodeLookupRepository(LookupService service) {
        this.service = service;
        releaseListData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Release>> getReleaseListData() {
        return releaseListData;
    }

    public void lookupReleasesWithBarcode(String barcode) {
        service.lookupReleaseWithBarcode(barcode).enqueue(new Callback<BarcodeReleaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BarcodeReleaseResponse> call, @NonNull Response<BarcodeReleaseResponse> response) {
                BarcodeReleaseResponse releaseResponse = response.body();
                releaseListData.setValue(Objects.requireNonNull(releaseResponse).getReleases());
            }

            @Override
            public void onFailure(@NonNull Call<BarcodeReleaseResponse> call, @NonNull Throwable t) {
            }
        });
    }
}
