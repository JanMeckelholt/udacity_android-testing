package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.MainDispatcherRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {
    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2")
    private val task3 = Task("Title3", "Description3")
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    private lateinit var tasksRepository: DefaultTasksRepository

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()
    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())

        tasksRepository = DefaultTasksRepository(tasksRemoteDataSource, tasksLocalDataSource, Dispatchers.Main)
    }

    @Test
    fun getTasks_requestAllTasksFromRemotDataSource() = runTest{
        val result =  tasksRepository.getTasks(true) as Result.Success
        assertEquals(result.data, remoteTasks)
    }

    @Test
    fun getTasks_requestAllTasksFromLocalDataSource() = runTest{
        val result =  tasksRepository.getTasks(false) as Result.Success
        assertEquals(result.data, localTasks)
    }
}