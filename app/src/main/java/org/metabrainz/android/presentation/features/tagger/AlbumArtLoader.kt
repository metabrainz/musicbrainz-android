package org.metabrainz.android.presentation.features.tagger

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.simplecityapps.ktaglib.KTagLib
import java.io.ByteArrayInputStream

class AlbumArtLoader(private val context: Context, private val kTagLib: KTagLib) : ModelLoader<AudioFile, ByteArrayInputStream> {

    override fun buildLoadData(
        model: AudioFile,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<ByteArrayInputStream> {
        return ModelLoader.LoadData(ObjectKey(model.path), AlbumArtFetcher(context, kTagLib, model))
    }

    override fun handles(model: AudioFile): Boolean = model.path.isNotBlank()

    class Factory(private val context: Context, private val kTagLib: KTagLib) : ModelLoaderFactory<AudioFile, ByteArrayInputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AudioFile, ByteArrayInputStream> {
            return AlbumArtLoader(context, kTagLib)
        }

        override fun teardown() {}
    }
}