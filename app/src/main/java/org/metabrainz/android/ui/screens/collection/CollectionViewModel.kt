package org.metabrainz.android.ui.screens.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.repository.CollectionRepository
import org.metabrainz.android.ui.adapters.ResultItem
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Utils.toResultItemsList
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(val repository: CollectionRepository) : ViewModel() {

    fun fetchCollectionData(editor: String, fetchPrivate: Boolean): LiveData<Resource<MutableList<org.metabrainz.android.model.mbentity.Collection>>> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(repository.fetchCollections(editor, fetchPrivate))
        }
    }

    fun fetchCollectionDetails(entity: MBEntityType, id: String): LiveData<Resource<List<ResultItem>>> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val result = repository.fetchCollectionDetails(entity.entity, id)
            emit(toResultItemsList(entity, result))
        }
    }
}