package org.metabrainz.mobile.presentation.features.links;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.metabrainz.mobile.data.sources.api.entities.Link;

import java.util.List;

public class LinksViewModel extends ViewModel {

    private final MutableLiveData<List<Link>> linksLiveData = new MutableLiveData<>();

    public LinksViewModel() {
    }

    LiveData<List<Link>> getData() {
        return linksLiveData;
    }

    public void setData(List<Link> links) {
        linksLiveData.setValue(links);
    }
}
