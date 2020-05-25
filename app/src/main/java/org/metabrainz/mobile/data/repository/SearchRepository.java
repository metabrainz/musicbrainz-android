package org.metabrainz.mobile.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.data.sources.Constants;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.SearchService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final static SearchService service = MusicBrainzServiceGenerator
            .createService(SearchService.class, true);
    private static SearchRepository repository;

    private SearchRepository() {
    }

    public static SearchRepository getRepository() {
        if (repository == null) repository = new SearchRepository();
        return repository;
    }

    public LiveData<String> getResults(String entity, String searchTerm) {
        MutableLiveData<String> resultsLiveData = new MutableLiveData<>();
        service.searchEntity(entity, searchTerm, Constants.LIMIT, Constants.OFFSET)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            resultsLiveData.setValue(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    }
                });
        return resultsLiveData;
    }

}
