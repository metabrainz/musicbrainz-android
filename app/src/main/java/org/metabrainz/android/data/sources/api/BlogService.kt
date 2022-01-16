package org.metabrainz.android.data.sources.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BlogService {
    @GET("https://api.listenbrainz.org/1/submit-listens")
    suspend fun getBlogs(): ResponseBody
}