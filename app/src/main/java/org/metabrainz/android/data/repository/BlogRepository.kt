package org.metabrainz.android.data.repository

import org.metabrainz.android.data.sources.api.entities.blog.Blog
import org.metabrainz.android.util.Resource

interface BlogRepository {
    suspend fun fetchBlogs(): Resource<Blog>
}