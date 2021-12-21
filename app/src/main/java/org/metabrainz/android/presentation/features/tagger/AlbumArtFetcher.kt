package org.metabrainz.android.presentation.features.tagger

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.simplecityapps.ktaglib.KTagLib
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException

class AlbumArtFetcher(
    private val context: Context,
    private val kTagLib: KTagLib,
    private val model: AudioFile
) : DataFetcher<ByteArrayInputStream> {

    private var stream: ByteArrayInputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in ByteArrayInputStream>) {
        try {
            context.contentResolver.openFileDescriptor(Uri.parse(model.path), "r")?.use {
                val artwork = kTagLib.getArtwork(it.detachFd())
                if (artwork != null) {
                    stream = ByteArrayInputStream(artwork)
                    callback.onDataReady(stream)
                } else {
                    callback.onLoadFailed(FileNotFoundException("No album art found"))
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun cleanup() {
        stream?.close()
    }

    override fun cancel() {
        stream?.close()
    }

    override fun getDataClass(): Class<ByteArrayInputStream> = ByteArrayInputStream::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL


}