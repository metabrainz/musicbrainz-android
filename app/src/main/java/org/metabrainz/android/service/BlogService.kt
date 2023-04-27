package org.metabrainz.android.service

import retrofit2.http.GET

interface BlogService {
    @GET("blog.metabrainz.org/posts/")
    suspend fun getBlogs(): org.metabrainz.android.model.blog.Blog
}