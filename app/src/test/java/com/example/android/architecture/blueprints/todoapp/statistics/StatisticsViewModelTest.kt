package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainDispatcherRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class StatisticsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    // Subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel

    // Use a fake repository to be injected into the view model.
    private lateinit var tasksRepository: FakeTestRepository

    @Before
    fun setupStatisticsViewModel() {
        tasksRepository = FakeTestRepository()
        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading() = runTest() {
            statisticsViewModel.refresh()
            assertTrue(statisticsViewModel.dataLoading.getOrAwaitValue())
            runCurrent()
            assertFalse(statisticsViewModel.dataLoading.getOrAwaitValue())
    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay() = runTest() {
        tasksRepository.setReturnError(true)
        statisticsViewModel.refresh()
        runCurrent()
        assertTrue(statisticsViewModel.empty.getOrAwaitValue())
        assertTrue(statisticsViewModel.error.getOrAwaitValue())
    }
}