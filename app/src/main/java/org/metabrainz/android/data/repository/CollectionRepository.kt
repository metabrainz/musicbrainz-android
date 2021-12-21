package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.CollectionUtils
import org.metabrainz.android.data.sources.api.CollectionService
import org.metabrainz.android.data.sources.api.entities.mbentity.Collection
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.FAILED
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepository @Inject constructor(val service: CollectionService) {

    suspend fun fetchCollectionDetails(entity: String, id: String): Resource<String> {
        return try {
            val result = service.getCollectionContents(entity, id)
            Resource(SUCCESS, result.string())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failure()
        }
    }

    suspend fun fetchCollections(editor: String, fetchPrivate: Boolean): Resource<MutableList<Collection>> {
        return try {
            val response =
                    if (fetchPrivate)
                        service.getAllUserCollections(editor, "user-collections")
                    else
                        service.getPublicUserCollections(editor)
            val collections = CollectionUtils.setGenericCountParameter(response.string())
            Resource(SUCCESS, collections)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource(FAILED, null)
        }
    }
}