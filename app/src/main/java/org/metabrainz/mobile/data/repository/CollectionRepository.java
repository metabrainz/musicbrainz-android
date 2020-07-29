package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.CollectionService;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CollectionRepository {

    private final CollectionService service;
    private Callback<ResponseBody> listResponseCallback;

    @Inject
    public CollectionRepository(CollectionService service) {
        this.service = service;
    }

    public LiveData<String> fetchCollectionDetails(String entity, String id) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        service.getCollectionContents(entity, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    liveData.setValue(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
        return liveData;
    }

    public LiveData<List<Collection>> fetchCollections(String editor, boolean fetchPrivate) {
        MutableLiveData<List<Collection>> collectionData = new MutableLiveData<>();
        listResponseCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    List<Collection> collections = new ArrayList<>();
                    CollectionUtils.setGenericCountParameter(collections, Objects.requireNonNull(response.body()).string());
                    collectionData.setValue(collections);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        };

        if (fetchPrivate)
            service.getAllUserCollections(editor, "user-collections").enqueue(listResponseCallback);
        else
            service.getPublicUserCollections(editor).enqueue(listResponseCallback);

        return collectionData;
    }

}
