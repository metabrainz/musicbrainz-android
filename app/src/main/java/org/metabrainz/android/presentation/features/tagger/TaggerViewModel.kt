package org.metabrainz.android.presentation.features.tagger

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import com.simplecityapps.ktaglib.KTagLib
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.sources.api.entities.CoverArt
import org.metabrainz.android.util.Metadata
import org.metabrainz.android.util.Resource
import javax.inject.Inject

@HiltViewModel
class TaggerViewModel @Inject constructor(val repository: LookupRepository, val context: Application) : AndroidViewModel(context) {

    private val _taglibFetchedMetadata = MutableLiveData<AudioFile?>()
    val taglibFetchedMetadata: LiveData<AudioFile?> get() = _taglibFetchedMetadata

    private val _uri = MutableLiveData<Uri>()
    val uri:LiveData<Uri> get() = _uri

    val matchedResult: LiveData<Resource<List<TagField>>> = map(switchMap(taglibFetchedMetadata) {
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.loading())
            val result = repository.fetchRecordings(it?.artist, it?.title)
            emit(result)
        }
    }) { resource ->
        //Handling the status of the api call
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                if(resource.data !=null){
                    Metadata.createTagFields(taglibFetchedMetadata.value, resource.data[0])
                }
                else{
                    null
                }
            }
            Resource.Status.LOADING -> {
                Resource.loading()
            }
            Resource.Status.FAILED -> {
                Resource.failure()
            }
        }
    }

    val serverCoverArt: LiveData<Resource<CoverArt>>

    fun setTaglibFetchedMetadata(metadata: AudioFile?) {
        _taglibFetchedMetadata.value = metadata!!
    }

    fun setURI(uri:Uri){
        _uri.value = uri
    }

    fun saveMetadataTags(tags: HashMap<String, String>): Boolean {
        Log.i("filepath",_uri.value.toString())
        var result = false
        context.contentResolver.openFileDescriptor(Uri.parse(_uri.value.toString()), "rw")?.use {
            result = KTagLib.writeMetadata(it.detachFd(), tags)

            Log.i("resulttag",result.toString())
        }
        return result
    }

    init {

        serverCoverArt = map(switchMap(matchedResult) {
            liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(Resource.loading())
                val result = repository.fetchCoverArt("5f5aaf10-36a4-3dfc-a7c1-c2d5c2286647")
                emit(result)
            }
        })  { resource ->
            //Handling the status of the api call
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if(resource.data !=null){
                        Resource(Resource.Status.SUCCESS,resource.data)
                    }
                    else{
                        null
                    }
                }
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
                Resource.Status.FAILED -> {
                    Resource.failure()
                }
            }
        }
    }
}