package org.metabrainz.mobile.presentation.features.links

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.metabrainz.mobile.data.sources.api.entities.Link

class LinksViewModel : ViewModel() {
    private val linksLiveData = MutableLiveData<List<Link>>()
    val data: LiveData<List<Link>>
        get() = linksLiveData

    fun setData(links: List<Link>) {
        linksLiveData.value = links
    }
}