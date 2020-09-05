package org.metabrainz.mobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.metabrainz.mobile.AssertionUtils.*
import org.metabrainz.mobile.EntityTestUtils.getTestArtist
import org.metabrainz.mobile.EntityTestUtils.getTestArtistWiki
import org.metabrainz.mobile.LiveDataTestUtil.getOrAwaitValue
import org.metabrainz.mobile.presentation.features.artist.ArtistViewModel
import org.metabrainz.mobile.presentation.features.label.LabelViewModel
import org.metabrainz.mobile.presentation.features.recording.RecordingViewModel
import org.metabrainz.mobile.presentation.features.release.ReleaseViewModel
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupViewModel
import org.metabrainz.mobile.util.Resource.Status.SUCCESS

class LookupViewModelTest {

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
    fun testArtistViewModel() = testDispatcher.runBlockingTest {
        val testArtist = getTestArtist()
        val testWiki = getTestArtistWiki()
        val viewModel = ArtistViewModel(MockLookupRepository())
        viewModel.setMBID(EntityTestUtils.getTestArtistMBID())
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource.status)
        checkArtistAssertions(testArtist, resource.data)
        val wikiResource = getOrAwaitValue(viewModel.wikiData)
        assertEquals(SUCCESS, wikiResource.status)
        checkWikiAssertions(testWiki, wikiResource.data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseViewModel() = testDispatcher.runBlockingTest {
        val testRelease = EntityTestUtils.getTestRelease()
        val viewModel = ReleaseViewModel(MockLookupRepository())
        viewModel.setMBID(EntityTestUtils.getTestReleaseMBID())
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource.status)
        checkReleaseAssertions(testRelease, resource.data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseGroupViewModel() = testDispatcher.runBlockingTest {
        val testReleaseGroup = EntityTestUtils.getTestReleaseGroup()
        val viewModel = ReleaseGroupViewModel(MockLookupRepository())
        viewModel.setMBID(EntityTestUtils.getTestReleaseGroupMBID())
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource.status)
        checkReleaseGroupAssertions(testReleaseGroup, resource.data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLabelViewModel() = testDispatcher.runBlockingTest {
        val testLabel = EntityTestUtils.getTestLabel()
        val viewModel = LabelViewModel(MockLookupRepository())
        viewModel.setMBID(EntityTestUtils.getTestLabelMBID())
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource.status)
        checkLabelAssertions(testLabel, resource.data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRecordingViewModel() = testDispatcher.runBlockingTest {
        val testRecording = EntityTestUtils.getTestRecording()
        val viewModel = RecordingViewModel(MockLookupRepository())
        viewModel.setMBID(EntityTestUtils.getTestRecordingMBID())
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource.status)
        checkRecordingAssertions(testRecording, resource.data)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}