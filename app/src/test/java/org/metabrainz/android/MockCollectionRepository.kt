package org.metabrainz.android

import org.metabrainz.android.data.repository.CollectionRepository
import org.metabrainz.android.data.sources.CollectionUtils
import org.metabrainz.android.data.sources.api.entities.mbentity.Collection
import org.metabrainz.android.util.Resource

class MockCollectionRepository : CollectionRepository {

    override suspend fun fetchCollectionDetails(entity: String, id: String): Resource<String> {
        return Resource(Resource.Status.SUCCESS, EntityTestUtils.loadResourceAsString( "collection_details.json"))
    }

    override suspend fun fetchCollections(editor: String, fetchPrivate: Boolean): Resource<MutableList<Collection>> {
        val response = if (fetchPrivate)
            EntityTestUtils.loadResourceAsString("collections_private.json")
        else
            EntityTestUtils.loadResourceAsString("collections_public.json")

        val collections = CollectionUtils.setGenericCountParameter(response)
        return Resource(Resource.Status.SUCCESS, collections)
    }
}