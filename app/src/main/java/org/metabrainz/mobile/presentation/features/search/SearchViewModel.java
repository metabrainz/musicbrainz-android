package org.metabrainz.mobile.presentation.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.metabrainz.mobile.data.repository.SearchRepository;
<<<<<<< HEAD
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType;
=======
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Artist;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Event;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Instrument;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntity;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.ReleaseGroup;
import org.metabrainz.mobile.presentation.IntentFactory;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
import org.metabrainz.mobile.presentation.features.adapters.ResultItem;
import org.metabrainz.mobile.presentation.features.adapters.ResultItemUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private static SearchRepository repository = SearchRepository.getRepository();
    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();

    public SearchViewModel() {
    }

    void search(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty())
            searchQueryLiveData.setValue(searchTerm);
    }

<<<<<<< HEAD
    LiveData<List<ResultItem>> getResultData(MBEntityType entity) {
        return Transformations.map(
                Transformations.switchMap(searchQueryLiveData,
                        searchTerm -> repository.getResults(entity.name, searchTerm)),
                response -> ResultItemUtils.getJSONResponseAsResultItemList(response, entity));
=======
    LiveData<List<ResultItem>> getResultData(String entity) {
        return Transformations.map(
                Transformations.switchMap(searchQueryLiveData,
                        searchTerm -> repository.getResults(entity, searchTerm)),
                response -> {
                    List<? extends MBEntity> list = new Gson().fromJson(
                            new JsonParser().parse(response)
                                    .getAsJsonObject()
                                    .get(entity + "s"), getTypeToken(entity));
                    List<ResultItem> items = new ArrayList<>();
                    for (MBEntity e : list) items.add(ResultItemUtils.getEntityAsResultItem(e));
                    return items;
                });
    }

    private Type getTypeToken(String entity) {
        if (entity.equalsIgnoreCase(IntentFactory.Extra.ARTIST))
            return TypeToken.getParameterized(List.class, Artist.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RELEASE))
            return TypeToken.getParameterized(List.class, Release.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.LABEL))
            return TypeToken.getParameterized(List.class, Label.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RECORDING))
            return TypeToken.getParameterized(List.class, Recording.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.EVENT))
            return TypeToken.getParameterized(List.class, Event.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.INSTRUMENT))
            return TypeToken.getParameterized(List.class, Instrument.class).getType();
        else if (entity.equalsIgnoreCase(IntentFactory.Extra.RELEASE_GROUP))
            return TypeToken.getParameterized(List.class, ReleaseGroup.class).getType();
        else return null;
>>>>>>> cdaf05d... Remove redundancy in search module using generics.
    }

}
