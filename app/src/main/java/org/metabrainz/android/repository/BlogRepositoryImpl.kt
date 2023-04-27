package org.metabrainz.android.repository

import androidx.annotation.WorkerThread
import org.metabrainz.android.service.BlogService
import org.metabrainz.android.model.blog.Blog
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Resource.Status.SUCCESS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogRepositoryImpl @Inject constructor(val service: BlogService) : BlogRepository {

    @WorkerThread
    override suspend fun fetchBlogs(): Resource<org.metabrainz.android.model.blog.Blog> {
        return try {
            val result = service.getBlogs()
            Resource(SUCCESS, result)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failure()
        }
    }
}