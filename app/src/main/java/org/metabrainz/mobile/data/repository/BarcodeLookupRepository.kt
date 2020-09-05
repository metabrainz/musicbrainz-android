package org.metabrainz.mobile.data.repository

import org.metabrainz.mobile.data.sources.api.LookupService
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Resource.Status.FAILED
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

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