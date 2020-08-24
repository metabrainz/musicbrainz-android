package org.metabrainz.mobile.presentation.features.tagger

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simplecityapps.ktaglib.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KotlinTaggerViewModel : ViewModel() {
    private val _TaglibFetchedMetadata = MutableLiveData<AudioFile>()
    val TaglibFetchedMetadata: LiveData<AudioFile>
        get() = _TaglibFetchedMetadata

    private val _ServerFetchedMetadata = MutableLiveData<AudioFile>()
    val ServerFetchedMetadata: LiveData<AudioFile>
        get() = _ServerFetchedMetadata

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap>
        get() = _bitmap

    fun setTaglibFetchedMetadata(metadata:AudioFile?){
        _TaglibFetchedMetadata.value = metadata
    }
    fun setServerFetchedMetadata(metadata: AudioFile?){
        _ServerFetchedMetadata.value = metadata
    }

    fun setBitmap(bitmap:Bitmap?){
        _bitmap.value = bitmap
    }

    init {
        _TaglibFetchedMetadata.value = null
        _ServerFetchedMetadata.value = null
    }
}