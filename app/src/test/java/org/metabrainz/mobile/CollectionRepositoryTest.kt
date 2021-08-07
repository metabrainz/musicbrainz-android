package org.metabrainz.mobile

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.metabrainz.mobile.AssertionUtils.checkCollectionAssertions
import org.metabrainz.mobile.EntityTestUtils.loadResourceAsString
import org.metabrainz.mobile.EntityTestUtils.testCollectionPrivate
import org.metabrainz.mobile.EntityTestUtils.testCollectionPublic
import org.metabrainz.mobile.RetrofitUtils.createTestService
import org.metabrainz.mobile.data.repository.CollectionRepository
import org.metabrainz.mobile.data.repository.CollectionRepositoryImpl
import org.metabrainz.mobile.data.sources.api.CollectionService
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.presentation.features.adapters.ResultItem
import org.metabrainz.mobile.util.Resource
import java.lang.Exception

class CollectionRepositoryTest {

    private lateinit var webServer: MockWebServer
    private lateinit var repository: CollectionRepository

    @Before
    fun setup() {
        webServer = MockWebServer()
        webServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val params = request.requestUrl?.queryParameter("inc")
                val file = when {
                        params != null && params.equals("user-collections", ignoreCase = true) -> "collections_private.json"
                        else -> "collections_public.json"
                    }
                return MockResponse().setResponseCode(200).setBody(loadResourceAsString(file))
            }
        }
        webServer.start()
        val service = createTestService(CollectionService::class.java, webServer.url("/"))
        println(service.toString())
        repository = CollectionRepositoryImpl(service)
    }

    @Test
    fun fetchCollectionDetails() = runBlocking {
//        val expected = testCollectionDetails
//        val resource = repository.fetchCollectionDetails(MBEntityType.RELEASE.entity,expected.mBID!!)
//        assertEquals(Resource.Status.SUCCESS, resource.status)
//        val collectionDetails = Gson().fromJson(resource.data, ResultItem::class.java)
//        AssertionUtils.checkCollectionDetailsAssertions(expected, collectionDetails)
    }

    @Test
    fun fetchCollections() = runBlocking {
        val expectedPrivate = testCollectionPrivate
        val resourcePrivate = repository.fetchCollections(expectedPrivate.editor!!,true)
        assertEquals(Resource.Status.SUCCESS, resourcePrivate.status)
        val collectionPrivate = resourcePrivate.data!!.indexOf(expectedPrivate)
        checkCollectionAssertions(expectedPrivate, resourcePrivate.data!![collectionPrivate])

        val expectedPublic = testCollectionPublic
        val resourcePublic = repository.fetchCollections(expectedPublic.editor!!,false)
        assertEquals(Resource.Status.SUCCESS, resourcePublic.status)
        val collectionPublic = resourcePublic.data!!.indexOf(expectedPublic)
        checkCollectionAssertions(expectedPublic, resourcePublic.data!![collectionPublic])


    }

    @After
    fun teardown() {
        webServer.close()
    }
}