package org.metabrainz.android

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.metabrainz.android.AssertionUtils.checkCollectionAssertions
import org.metabrainz.android.EntityTestUtils.loadResourceAsString
import org.metabrainz.android.EntityTestUtils.testCollectionDetails
import org.metabrainz.android.EntityTestUtils.testCollectionPrivate
import org.metabrainz.android.EntityTestUtils.testCollectionPublic
import org.metabrainz.android.RetrofitUtils.createTestService
import org.metabrainz.android.model.mbentity.MBEntityType
import org.metabrainz.android.repository.CollectionRepository
import org.metabrainz.android.repository.CollectionRepositoryImpl
import org.metabrainz.android.service.CollectionService
import org.metabrainz.android.util.Resource
import org.metabrainz.android.util.Utils

class CollectionRepositoryTest {

    private lateinit var webServer: MockWebServer
    private lateinit var repository: CollectionRepository

    @Before
    fun setup() {
        webServer = MockWebServer()
        webServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val params = request.requestUrl?.queryParameter("inc")
                val paramsDetails = request.requestUrl?.queryParameter("collection")
                val file = when {
                        params != null && params.equals("user-collections", ignoreCase = true) -> "collections_private.json"
                        paramsDetails != null -> "collection_details.json"
                        else -> "collections_public.json"
                    }
                return MockResponse().setResponseCode(200).setBody(loadResourceAsString(file))
            }
        }
        webServer.start()
        val service = createTestService(CollectionService::class.java, webServer.url("/"))
        repository = CollectionRepositoryImpl(service)
    }

    @Test
    fun fetchCollectionDetails() = runBlocking {
        val expected = testCollectionDetails
        val resource = repository.fetchCollectionDetails(MBEntityType.RELEASE.entity,expected.mBID!!)
        assertEquals(Resource.Status.SUCCESS, resource.status)
        val collectionDetails = Utils.toResultItemsList(MBEntityType.RELEASE, resource)
        val cDIndex = collectionDetails.data!!.indexOfFirst { it.mBID == expected.mBID!!}
        AssertionUtils.checkCollectionDetailsAssertions(expected, collectionDetails.data!![cDIndex])
    }

    @Test
    fun fetchCollections() = runBlocking {
        val expectedPublic = testCollectionPublic
        val resourcePublic = repository.fetchCollections(expectedPublic.editor!!,false)
        assertEquals(Resource.Status.SUCCESS, resourcePublic.status)
        val collectionPublic = resourcePublic.data!!.indexOfFirst { it.mbid == expectedPublic.mbid }
        checkCollectionAssertions(expectedPublic, resourcePublic.data!![collectionPublic])

        val expectedPrivate = testCollectionPrivate
        val resourcePrivate = repository.fetchCollections(expectedPrivate.editor!!,true)
        assertEquals(Resource.Status.SUCCESS, resourcePrivate.status)
        val collectionPrivate = resourcePrivate.data!!.indexOfFirst { it.mbid == expectedPrivate.mbid }
        checkCollectionAssertions(expectedPrivate, resourcePrivate.data!![collectionPrivate])
    }

    @After
    fun teardown() {
        webServer.close()
    }
}