package org.metabrainz.android.data.repository

import org.metabrainz.android.util.Resource

interface BlogRepository {
    suspend fun fetchBlogs(): Resource<String>
}