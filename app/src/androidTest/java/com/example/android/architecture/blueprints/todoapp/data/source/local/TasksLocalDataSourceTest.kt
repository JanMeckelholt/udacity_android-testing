package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveTask_retrievesTask() = runTest {
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)
        val result = localDataSource.getTask(newTask.id)
        runCurrent()
        assertTrue(result.succeeded)
        result as Result.Success
        assertEquals("title", result.data.title)
        assertEquals("description", result.data.description)
        assertFalse(result.data.isCompleted)
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runTest {
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)
        localDataSource.completeTask(newTask.id)
        val result = localDataSource.getTask(newTask.id)
        runCurrent()
        assertTrue(result.succeeded)
        result as Result.Success
        assertEquals("title", result.data.title)
        assertEquals("description", result.data.description)
        assertTrue(result.data.isCompleted)
    }
}