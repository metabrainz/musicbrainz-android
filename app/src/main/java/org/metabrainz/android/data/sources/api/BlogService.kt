package org.metabrainz.android.data.sources.api

import org.metabrainz.android.data.sources.api.entities.blog.Blog
import retrofit2.http.GET

interface BlogService {
    @GET("https://public-api.wordpress.com/rest/v1.1/sites/blog.metabrainz.org/posts/")
    suspend fun getBlogs(): Blog
}