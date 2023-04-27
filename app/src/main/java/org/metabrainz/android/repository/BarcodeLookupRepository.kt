package org.metabrainz.android.repository

import org.metabrainz.android.model.mbentity.Release
import org.metabrainz.android.service.LookupService
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.FAILED
import org.metabrainz.android.util.Resource.Status.SUCCESS

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeLookupRepository @Inject constructor(private val service: LookupService) {

    suspend fun lookupReleasesWithBarcode(barcode: String): Resource<List<Release>> {
        return try {
            val result = service.lookupReleaseWithBarcode(barcode)
            Resource(SUCCESS, result.releases)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource(FAILED, null)
        }
    }

}