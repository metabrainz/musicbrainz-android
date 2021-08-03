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
import org.metabrainz.mobile.AssertionUtils.checkCollectionAssertions
import org.metabrainz.mobile.EntityTestUtils.loadResourceAsString
import org.metabrainz.mobile.EntityTestUtils.testCollectionDetails
import org.metabrainz.mobile.EntityTestUtils.testCollectionPrivate
import org.metabrainz.mobile.EntityTestUtils.testCollectionPublic
import org.metabrainz.mobile.RetrofitUtils.createTestService
import org.metabrainz.mobile.data.repository.CollectionRepository
import org.metabrainz.mobile.data.repository.CollectionRepositoryImpl
import org.metabrainz.mobile.data.sources.api.CollectionService
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.presentation.features.adapters.ResultItem
import org.metabrainz.mobile.util.Resource

class CollectionRepositoryTest {

    private lateinit var webServer: MockWebServer
    private lateinit var repository: CollectionRepository

    @Before
    fun setup() {
        webServer = MockWebServer()
        webServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(loadResourceAsString("collections_public.json"))
            }
        }
        webServer.start()
        val service = createTestService(CollectionService::class.java, webServer.url("/"))
        repository = CollectionRepositoryImpl(service)
    }

    @Test
    fun fetchCollectionDetails() = runBlocking {
        val expected = testCollectionDetails
        val resource = repository.fetchCollectionDetails(MBEntityType.RELEASE.entity,"a691377c-6949-44cb-8a10-9696178cca18")
        assertEquals(Resource.Status.SUCCESS, resource.status)
        val collectionDetails = Gson().fromJson(resource.data, ResultItem::class.java)
        AssertionUtils.checkCollectionDetailsAssertions(expected, collectionDetails)
    }

    @Test
    fun fetchCollections() = runBlocking {
        val expectedPublic = testCollectionPublic
        val resourcePublic = repository.fetchCollections(expectedPublic.editor!!,false)
        assertEquals(Resource.Status.SUCCESS, resourcePublic.status)
        val collectionPublic = resourcePublic.data!!.indexOf(expectedPublic)
        if(collectionPublic!=-1){
            checkCollectionAssertions(expectedPublic, resourcePublic.data!![collectionPublic])
        }

        val expectedPrivate = testCollectionPrivate
        val resourcePrivate = repository.fetchCollections(expectedPrivate.editor!!,true)
        assertEquals(Resource.Status.SUCCESS, resourcePrivate.status)
        val collectionPrivate = resourcePrivate.data!!.indexOf(expectedPrivate)
        if(collectionPrivate!=-1){
            checkCollectionAssertions(expectedPrivate, resourcePrivate.data!![collectionPrivate])
        }
    }

    @After
    fun teardown() {
        webServer.close()
    }
}