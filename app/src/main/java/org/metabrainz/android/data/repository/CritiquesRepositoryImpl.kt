package org.metabrainz.android.data.repository

import androidx.annotation.WorkerThread
import org.metabrainz.android.data.sources.api.CritiquesService
import org.metabrainz.android.data.sources.api.entities.critiques.Review
import org.metabrainz.android.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CritiquesRepositoryImpl @Inject constructor(private val service: CritiquesService) : CritiquesRepository {

    @WorkerThread
    override suspend fun fetchAllReviews(): Resource<List<Review>> {
        return try {
            val response = service.getAllReviews()
            Resource(Resource.Status.SUCCESS,response.reviews)

        }catch (e:Exception){
            e.printStackTrace()
           Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchArtistReviews(): Resource<List<Review>> {
        return try {
            val response = service.getArtistReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchReleaseGroupReviews(): Resource<List<Review>> {
        return try {
            val response = service.getReleaseGroupReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchLabelReviews(): Resource<List<Review>> {
        return try {
            val response = service.getLabelReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchRecordingReviews(): Resource<List<Review>> {
        return try {
            val response = service.getRecordingReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchEventReviews(): Resource<List<Review>> {
        return try {
            val response = service.getEventReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchPlaceReviews(): Resource<List<Review>> {
        return try {
            val response = service.getPlaceReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }

    override suspend fun fetchWorkReviews(): Resource<List<Review>> {
        return try {
            val response = service.getWorkReviews()
            Resource(Resource.Status.SUCCESS, response.reviews)
        }catch (e:Exception){
            e.printStackTrace()
            Resource(Resource.Status.FAILED,null)
        }
    }
}