package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.CollectionUtils;
import org.metabrainz.mobile.data.sources.api.CollectionService;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Collection;
import org.metabrainz.mobile.util.Resource;

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

    public LiveData<Resource<String>> fetchCollectionDetails(String entity, String id) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();
        service.getCollectionContents(entity, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Resource<String> resource;
                try {
                    String data = response.body().string();
                    resource = new Resource<>(Resource.Status.SUCCESS, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    resource = Resource.getFailure(String.class);
                }
                liveData.setValue(resource);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                liveData.setValue(Resource.getFailure(String.class));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<Collection>>> fetchCollections(String editor, boolean fetchPrivate) {
        MutableLiveData<Resource<List<Collection>>> collectionData = new MutableLiveData<>();
        listResponseCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Resource<List<Collection>> resource;
                try {
                    List<Collection> collections = new ArrayList<>();
                    CollectionUtils.setGenericCountParameter(collections, Objects.requireNonNull(response.body()).string());
                    resource = new Resource<>(Resource.Status.SUCCESS, collections);
                } catch (Exception e) {
                    e.printStackTrace();
                    resource = new Resource<>(Resource.Status.FAILED, null);
                }
                collectionData.setValue(resource);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                collectionData.setValue(new Resource<>(Resource.Status.FAILED, null));
            }
        };

        if (fetchPrivate)
            service.getAllUserCollections(editor, "user-collections").enqueue(listResponseCallback);
        else
            service.getPublicUserCollections(editor).enqueue(listResponseCallback);

        return collectionData;
    }

}
