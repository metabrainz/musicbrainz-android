package org.metabrainz.android.repository

import org.metabrainz.android.util.Resource

interface BlogRepository {
    suspend fun fetchBlogs(): Resource<org.metabrainz.android.model.blog.Blog>
}