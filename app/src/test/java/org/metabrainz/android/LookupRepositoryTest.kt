package org.metabrainz.android

import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.metabrainz.android.AssertionUtils.checkArtistAssertions
import org.metabrainz.android.AssertionUtils.checkLabelAssertions
import org.metabrainz.android.AssertionUtils.checkRecordingAssertions
import org.metabrainz.android.AssertionUtils.checkReleaseAssertions
import org.metabrainz.android.AssertionUtils.checkReleaseGroupAssertions
import org.metabrainz.android.EntityTestUtils.loadResourceAsString
import org.metabrainz.android.EntityTestUtils.testArtist
import org.metabrainz.android.EntityTestUtils.testLabel
import org.metabrainz.android.EntityTestUtils.testRecording
import org.metabrainz.android.EntityTestUtils.testRelease
import org.metabrainz.android.EntityTestUtils.testReleaseGroup
import org.metabrainz.android.RetrofitUtils.createTestService
import org.metabrainz.android.data.repository.LookupRepository
import org.metabrainz.android.data.repository.LookupRepositoryImpl
import org.metabrainz.android.data.sources.Constants.LOOKUP_ARTIST_PARAMS
import org.metabrainz.android.data.sources.Constants.LOOKUP_LABEL_PARAMS
import org.metabrainz.android.data.sources.Constants.LOOKUP_RECORDING_PARAMS
import org.metabrainz.android.data.sources.Constants.LOOKUP_RELEASE_GROUP_PARAMS
import org.metabrainz.android.data.sources.Constants.LOOKUP_RELEASE_PARAMS
import org.metabrainz.android.data.sources.api.LookupService
import org.metabrainz.android.data.sources.api.entities.mbentity.*
import org.metabrainz.android.util.Resource.Status.SUCCESS

class LookupRepositoryTest {

    // FIXME: Replace runBlockingTest with runBlocking for the time being to pass the tests
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
            }
        }
        webServer.start()
        val service = createTestService(LookupService::class.java, webServer.url("/"))
        repository = LookupRepositoryImpl(service)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testArtistLookup() = runBlockingTest {
        val expected = testArtist
        val resource = repository.fetchData(MBEntityType.ARTIST.nameHere, expected.mbid!!, LOOKUP_ARTIST_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, Artist::class.java)
        checkArtistAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseLookup() = runBlockingTest {
        val expected = testRelease
        val resource = repository.fetchData(MBEntityType.RELEASE.nameHere, expected.mbid!!, LOOKUP_RELEASE_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, Release::class.java)
        checkReleaseAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseGroupLookup() = runBlockingTest {
        val expected = testReleaseGroup
        val resource = repository.fetchData(MBEntityType.RELEASE_GROUP.nameHere, expected.mbid!!, LOOKUP_RELEASE_GROUP_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val actual = Gson().fromJson(resource.data, ReleaseGroup::class.java)
        checkReleaseGroupAssertions(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLabelLookup() = runBlockingTest {
        val expected = testLabel
        val resource = repository.fetchData(MBEntityType.LABEL.nameHere, expected.mbid!!, LOOKUP_LABEL_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val label = Gson().fromJson(resource.data, Label::class.java)
        checkLabelAssertions(expected, label)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRecordingLookup() = runBlockingTest {
        val expected = testRecording
        val resource = repository.fetchData(MBEntityType.RECORDING.nameHere, expected.mbid!!, LOOKUP_RECORDING_PARAMS)
        assertEquals(SUCCESS, resource.status)
        val recording = Gson().fromJson(resource.data, Recording::class.java)
        checkRecordingAssertions(expected, recording)
    }

    @After
    fun teardown() {
        webServer.close()
    }
}