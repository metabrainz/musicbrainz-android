package org.metabrainz.mobile

import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.metabrainz.mobile.AssertionUtils.checkArtistAssertions
import org.metabrainz.mobile.AssertionUtils.checkLabelAssertions
import org.metabrainz.mobile.AssertionUtils.checkRecordingAssertions
import org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions
import org.metabrainz.mobile.AssertionUtils.checkReleaseGroupAssertions
import org.metabrainz.mobile.EntityTestUtils.loadResourceAsString
import org.metabrainz.mobile.EntityTestUtils.testArtist
import org.metabrainz.mobile.EntityTestUtils.testLabel
import org.metabrainz.mobile.EntityTestUtils.testRecording
import org.metabrainz.mobile.EntityTestUtils.testRelease
import org.metabrainz.mobile.EntityTestUtils.testReleaseGroup
import org.metabrainz.mobile.RetrofitUtils.createTestService
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.repository.LookupRepositoryImpl
import org.metabrainz.mobile.data.sources.Constants.LOOKUP_ARTIST_PARAMS
import org.metabrainz.mobile.data.sources.Constants.LOOKUP_LABEL_PARAMS
import org.metabrainz.mobile.data.sources.Constants.LOOKUP_RECORDING_PARAMS
import org.metabrainz.mobile.data.sources.Constants.LOOKUP_RELEASE_GROUP_PARAMS
import org.metabrainz.mobile.data.sources.Constants.LOOKUP_RELEASE_PARAMS
import org.metabrainz.mobile.data.sources.api.LookupService
import org.metabrainz.mobile.data.sources.api.entities.mbentity.*
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class LookupRepositoryTest {

    // FIXME: Replaced runBlockingTest with runBlocking for the time being to pass the tests
    // This is a bug in the kotlinx-coroutines-test library. This should not be necessary once the
    // bug is fixed upstream.
    private lateinit var webServer: MockWebServer
    private lateinit var repository: LookupRepository

    @Before
    fun setup() {
        webServer = MockWebServer()
        webServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val endpoint = request.path?.substring(1, request.path!!.indexOf('/', 1))
                val file = endpoint + "_lookup.json"
                return MockResponse().setResponseCode(200).setBody(loadResourceAsString(file))

                // Head to https://github.com/square/okhttp/tree/master/mockwebserver for documentation
                // Equivalent to
                /*
                    when (request.path) {
                       "/v1/check/version/" -> {
                           return MockResponse().setResponseCode(200).setBody(loadResourceAsString(file))
                       }
                   }
                 */
            }
        }
        webServer.start()
        val service = createTestService(LookupService::class.java, webServer.url("/"))
        repository = LookupRepositoryImpl(service)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testArtistLookup() = runBlocking {
        val expected = testArtist
        val resource = repository.fetchData(MBEntityType.ARTIST.entity, expected.mbid!!, LOOKUP_ARTIST_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, Artist::class.java)
        checkArtistAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseLookup() = runBlocking {
        val expected = testRelease
        val resource = repository.fetchData(MBEntityType.RELEASE.entity, expected.mbid!!, LOOKUP_RELEASE_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, Release::class.java)
        checkReleaseAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseGroupLookup() = runBlocking {
        val expected = testReleaseGroup
        val resource = repository.fetchData(MBEntityType.RELEASE_GROUP.entity, expected.mbid!!, LOOKUP_RELEASE_GROUP_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, ReleaseGroup::class.java)
        checkReleaseGroupAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLabelLookup() = runBlocking {
        val expected = testLabel
        val resource = repository.fetchData(MBEntityType.LABEL.entity, expected.mbid!!, LOOKUP_LABEL_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val label = Gson().fromJson(resource.data, Label::class.java)
        checkLabelAssertions(expected, label)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRecordingLookup() = runBlocking {
        val expected = testRecording
        val resource = repository.fetchData(MBEntityType.RECORDING.entity, expected.mbid!!, LOOKUP_RECORDING_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val recording = Gson().fromJson(resource.data, Recording::class.java)
        checkRecordingAssertions(expected, recording)
    }

    @After
    fun teardown() {
        webServer.close()
    }
}