package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_setsNewTaskEven() {
        val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        taskViewModel.addNewTask()
        val value = taskViewModel.newTaskEvent.getOrAwaitValue()
        assertNotNull(value.getContentIfNotHandled())
    }
}