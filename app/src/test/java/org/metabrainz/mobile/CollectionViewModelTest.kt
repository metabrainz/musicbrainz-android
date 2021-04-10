package org.metabrainz.mobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

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
    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}