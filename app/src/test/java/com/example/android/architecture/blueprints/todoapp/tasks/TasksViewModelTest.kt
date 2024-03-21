package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainDispatcherRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import timber.log.Timber

@ExperimentalCoroutinesApi
class TasksViewModelTest {

    private lateinit var tasksRepository: FakeTestRepository
    private lateinit var tasksViewModel: TasksViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupViewModel() {
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Desc1")
        val task2 = Task("Title2", "Desc2", true)
        val task3 = Task("Title3", "Desc3", true)
        tasksRepository.addTasks(task1, task2, task3)
        tasksViewModel = TasksViewModel(tasksRepository)
        Timber.i("test")
    }

    @Test
    fun addNewTask_setsNewTaskEven() {
        tasksViewModel.addNewTask()
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertNotNull(value.getContentIfNotHandled())
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        assertTrue(tasksViewModel.tasksAddViewVisible.getOrAwaitValue())
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)
        tasksViewModel.completeTask(task, true)

        tasksRepository.tasksServiceData[task.id]?.isCompleted?.let { assertTrue(it) }

        val snackbarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
        assertEquals(snackbarText.getContentIfNotHandled(), R.string.task_marked_complete)
    }
}