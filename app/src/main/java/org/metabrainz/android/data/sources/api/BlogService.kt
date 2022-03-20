package org.metabrainz.android.data.sources.api

import org.metabrainz.android.data.sources.api.entities.blog.Blog
import retrofit2.http.GET

interface BlogService {
    @GET("blog.metabrainz.org/posts/")
    suspend fun getBlogs(): Blog
}