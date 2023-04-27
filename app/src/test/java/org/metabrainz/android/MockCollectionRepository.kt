package org.metabrainz.android

import org.metabrainz.android.repository.CollectionRepository
import org.metabrainz.android.util.CollectionUtils
import org.metabrainz.android.model.mbentity.Collection
import org.metabrainz.android.util.Resource

class MockCollectionRepository : CollectionRepository {

    override suspend fun fetchCollectionDetails(entity: String, id: String): Resource<String> {
        return Resource(Resource.Status.SUCCESS, EntityTestUtils.loadResourceAsString( "collection_details.json"))
    }

    override suspend fun fetchCollections(editor: String, fetchPrivate: Boolean): Resource<MutableList<org.metabrainz.android.model.mbentity.Collection>> {
        val response = if (fetchPrivate)
            EntityTestUtils.loadResourceAsString("collections_private.json")
        else
            EntityTestUtils.loadResourceAsString("collections_public.json")

        val collections = CollectionUtils.setGenericCountParameter(response)
        return Resource(Resource.Status.SUCCESS, collections)
    }
}