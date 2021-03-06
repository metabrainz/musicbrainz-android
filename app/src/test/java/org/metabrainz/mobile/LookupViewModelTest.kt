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
import org.metabrainz.mobile.AssertionUtils.checkArtistAssertions
import org.metabrainz.mobile.AssertionUtils.checkLabelAssertions
import org.metabrainz.mobile.AssertionUtils.checkRecordingAssertions
import org.metabrainz.mobile.AssertionUtils.checkReleaseAssertions
import org.metabrainz.mobile.AssertionUtils.checkReleaseGroupAssertions
import org.metabrainz.mobile.AssertionUtils.checkWikiAssertions
import org.metabrainz.mobile.EntityTestUtils.testArtist
import org.metabrainz.mobile.LiveDataTestUtil.getOrAwaitValue
import org.metabrainz.mobile.presentation.features.artist.ArtistViewModel
import org.metabrainz.mobile.presentation.features.label.LabelViewModel
import org.metabrainz.mobile.presentation.features.recording.RecordingViewModel
import org.metabrainz.mobile.presentation.features.release.ReleaseViewModel
import org.metabrainz.mobile.presentation.features.release_group.ReleaseGroupViewModel
import org.metabrainz.mobile.util.Resource.Status.SUCCESS
import org.metabrainz.mobile.EntityTestUtils.testArtistWiki as testArtistWiki

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
        val testArtist = testArtist
        val testWiki = testArtistWiki
        val viewModel = ArtistViewModel(MockLookupRepository())
        viewModel.mbid.value = EntityTestUtils.testArtistMBID
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource?.status)
        checkArtistAssertions(testArtist, resource?.data!!)
        val wikiResource = getOrAwaitValue(viewModel.wikiData)
        assertEquals(SUCCESS, wikiResource?.status)
        checkWikiAssertions(testWiki, wikiResource?.data!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseViewModel() = testDispatcher.runBlockingTest {
        val testRelease = EntityTestUtils.testRelease
        val viewModel = ReleaseViewModel(MockLookupRepository())
        viewModel.mbid.value = EntityTestUtils.testReleaseMBID
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource?.status)
        checkReleaseAssertions(testRelease, resource?.data!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReleaseGroupViewModel() = testDispatcher.runBlockingTest {
        val testReleaseGroup = EntityTestUtils.testReleaseGroup
        val viewModel = ReleaseGroupViewModel(MockLookupRepository())
        viewModel.mbid.value = EntityTestUtils.testReleaseGroupMBID
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource?.status!!)
        checkReleaseGroupAssertions(testReleaseGroup, resource.data!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLabelViewModel() = testDispatcher.runBlockingTest {
        val testLabel = EntityTestUtils.testLabel
        val viewModel = LabelViewModel(MockLookupRepository())
        viewModel.mbid.value = EntityTestUtils.testLabelMBID
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource?.status)
        checkLabelAssertions(testLabel, resource?.data!!)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRecordingViewModel() = testDispatcher.runBlockingTest {
        val testRecording = EntityTestUtils.testRecording
        val viewModel = RecordingViewModel(MockLookupRepository())
        viewModel.mbid.value = EntityTestUtils.testRecordingMBID
        val resource = getOrAwaitValue(viewModel.data)
        assertEquals(SUCCESS, resource?.status!!)
        checkRecordingAssertions(testRecording, resource.data!!)
    }

    @ExperimentalCoroutinesApi
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}