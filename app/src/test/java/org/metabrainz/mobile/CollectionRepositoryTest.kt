package org.metabrainz.mobile

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.metabrainz.mobile.EntityTestUtils.loadResourceAsString
import org.metabrainz.mobile.RetrofitUtils.createTestService
import org.metabrainz.mobile.data.repository.LookupRepository
import org.metabrainz.mobile.data.repository.LookupRepositoryImpl
import org.metabrainz.mobile.data.sources.api.LookupService

class CollectionRepositoryTest {

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

    @After
    fun teardown() {
        webServer.close()
    }
}