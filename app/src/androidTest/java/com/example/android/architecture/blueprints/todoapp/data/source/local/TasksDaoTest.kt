package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTaskAndGebById() = runTest {
        val task = Task("title", "desc")
        database.taskDao().insertTask(task)
        runCurrent()
        val loaded = database.taskDao().getTaskById(taskId = task.id)

        assertNotNull(loaded as Task)
        assertEquals(task.id, loaded.id)
        assertEquals(task.title, loaded.title)
        assertEquals(task.description, loaded.description)
        assertEquals(task.isCompleted, loaded.isCompleted)
    }

    @Test
    fun updateTaskAndGetById() = runTest {
        val task = Task("title", "desc")
        database.taskDao().insertTask(task)
        runCurrent()
        val updatedTask = Task("updated_title", "updated desc")
        updatedTask.id = task.id
        database.taskDao().updateTask(updatedTask)
        runCurrent()
        val loaded = database.taskDao().getTaskById(taskId = task.id)
        assertNotNull(loaded as Task)
        assertEquals(updatedTask.id, loaded.id)
        assertEquals(updatedTask.title, loaded.title)
        assertEquals(updatedTask.description, loaded.description)
        assertEquals(updatedTask.isCompleted, loaded.isCompleted)
    }
}
