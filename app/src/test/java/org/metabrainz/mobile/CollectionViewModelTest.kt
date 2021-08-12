package org.metabrainz.mobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.metabrainz.mobile.data.sources.api.entities.mbentity.MBEntityType
import org.metabrainz.mobile.presentation.features.collection.CollectionViewModel
import org.metabrainz.mobile.util.Resource
import org.metabrainz.mobile.util.Utils

class CollectionViewModelTest {

    // FIXME: flaky tests because we use Dispatchers.IO in running the coroutines in production
    // but the test library only provides option to replace Dispatchers.Main. Solution can be to
    // inject the CoroutineScope in the ViewModel.
    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testCollectionDetailsViewModel() = testDispatcher.runBlockingTest {
        val testCollectionDetails = EntityTestUtils.testCollectionDetails
        val viewModel = CollectionViewModel(MockCollectionRepository())
        val resource = LiveDataTestUtil.getOrAwaitValue(viewModel.fetchCollectionDetails(MBEntityType.RELEASE,testCollectionDetails.mBID!!))
        Assert.assertEquals(Resource.Status.SUCCESS, resource!!.status)
        val cDIndex = resource.data!!.indexOfFirst { it.mBID == testCollectionDetails.mBID!!}
        AssertionUtils.checkCollectionDetailsAssertions(testCollectionDetails, resource.data!![cDIndex])
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testCollectionViewModel() = testDispatcher.runBlockingTest {
        val testCollection = EntityTestUtils.testCollectionPublic
        val viewModel = CollectionViewModel(MockCollectionRepository())
        val resource = LiveDataTestUtil.getOrAwaitValue(viewModel.fetchCollectionData(testCollection.mbid!!,false))
        Assert.assertEquals(Resource.Status.SUCCESS, resource!!.status)
        val collectionPublic = resource.data!!.indexOfFirst { it.mbid == testCollection.mbid }
        AssertionUtils.checkCollectionAssertions(testCollection, resource.data!![collectionPublic])
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}