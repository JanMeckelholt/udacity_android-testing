package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TaskDetailFragmentTest {
    private lateinit var repository: TasksRepository
    @Before
    fun initRepository(){
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runTest { ServiceLocator.resetRepository() }

    @Test
    fun activeTaskDetail_DisplayedInUi() {
        runTest {

            val activeTask = Task("Active Task", "Active Task Desc", false)
            repository.saveTask(activeTask)
            val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
            launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)


            // THEN - Task details are displayed on the screen
            // make sure that the title/description are both shown and correct
            onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_title_text)).check(matches(withText("Active Task")))
            onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_description_text)).check(matches(withText("Active Task Desc")))
            // and make sure the "active" checkbox is shown unchecked
            onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))
        }
    }

    @Test
    fun completedTaskDetails_displayedInUi(){
        runTest {
            val completedTask = Task("Completed Task", "Completed Task Desc", true)
            repository.saveTask(completedTask)
            val bundle = TaskDetailFragmentArgs(completedTask.id).toBundle()
            launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)
            onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_title_text)).check(matches(withText("Completed Task")))
            onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_description_text)).check(matches(withText("Completed Task Desc")))
            onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
            onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isChecked()))
        }
    }
}