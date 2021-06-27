package org.metabrainz.mobile.presentation.features.tagger

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.simplecityapps.ktaglib.KTagLib
import java.io.ByteArrayInputStream

@GlideModule
class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(AudioFile::class.java,
            ByteArrayInputStream::class.java,
            AlbumArtLoader.Factory(context.applicationContext, KTagLib))
    }
}