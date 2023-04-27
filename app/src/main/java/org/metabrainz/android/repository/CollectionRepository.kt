package org.metabrainz.android.repository

import org.metabrainz.android.util.Resource

interface CollectionRepository {

    suspend fun fetchCollectionDetails(entity: String, id: String): Resource<String>

    suspend fun fetchCollections(editor: String, fetchPrivate: Boolean): Resource<MutableList<org.metabrainz.android.model.mbentity.Collection>>
}