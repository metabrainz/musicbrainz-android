package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.api.entities.mbentity.Collection
import org.metabrainz.android.util.Resource

interface CollectionRepository {

    suspend fun fetchCollectionDetails(entity: String, id: String): Resource<String>

    suspend fun fetchCollections(editor: String, fetchPrivate: Boolean): Resource<MutableList<Collection>>
}