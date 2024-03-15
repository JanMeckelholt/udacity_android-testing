package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsZeroHundred() {
        val tasks = listOf<Task>(
            Task("title", "description", isCompleted = false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_2Completed_3Active_returns40_60() {
        val tasks = listOf<Task>(
            Task("title1", "description1", isCompleted = false),
            Task("title2", "description2", isCompleted = true),
            Task("title3", "description3", isCompleted = true),
            Task("title4", "description4", isCompleted = false),
            Task("title5", "description5", isCompleted = false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }
}