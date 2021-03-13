package org.metabrainz.mobile.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.metabrainz.mobile.data.sources.Constants
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator.ACOUST_ID_KEY
import org.metabrainz.mobile.data.sources.api.TaggerService
import org.metabrainz.mobile.data.sources.api.entities.acoustid.AcoustIDResponse
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Recording
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release
import org.metabrainz.mobile.data.sources.api.entities.response.RecordingSearchResponse
import org.metabrainz.mobile.util.TaggerUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaggerRepository @Inject constructor(private val service: TaggerService) {
    fun fetchRecordings(query: String?): LiveData<List<Recording>> {
        val recordingResponseData = MutableLiveData<List<Recording>>()
        service.searchRecording(query, Constants.LIMIT).enqueue(object : Callback<RecordingSearchResponse?> {
            override fun onResponse(call: Call<RecordingSearchResponse?>, response: Response<RecordingSearchResponse?>) {
                try {
                    val data = response.body()
                    recordingResponseData.setValue(data!!.recordings)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<RecordingSearchResponse?>, t: Throwable) {}
        })
        return recordingResponseData
    }

    fun fetchMatchedRelease(MBID: String?): LiveData<Release?> {
        val matchedReleaseData = MutableLiveData<Release?>()
        service.lookupRecording(MBID, Constants.TAGGER_RELEASE_PARAMS).enqueue(object : Callback<Release?> {
            override fun onResponse(call: Call<Release?>, response: Response<Release?>) {
                try {
                    val release = response.body()
                    matchedReleaseData.setValue(release)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<Release?>, t: Throwable) {}
        })
        return matchedReleaseData
    }

    fun fetchAcoustIDResults(duration: Long, fingerprint: String?): LiveData<List<Recording>> {
        val recordingResponseData = MutableLiveData<List<Recording>>()
        service.lookupFingerprint(ACOUST_ID_KEY, Constants.ACOUST_ID_RESPONSE_PARAMS, duration, fingerprint)
                .enqueue(object : Callback<AcoustIDResponse> {
                    override fun onResponse(call: Call<AcoustIDResponse>, response: Response<AcoustIDResponse>) {
                        try {
                            val result = response.body()!!.results
                            recordingResponseData.setValue(TaggerUtils.parseResults(result))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<AcoustIDResponse>, t: Throwable) {}
                })
        return recordingResponseData
    }
}