package com.hirauchi.habit.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.hirauchi.habit.database.dao.HabitDao
import com.hirauchi.habit.database.entity.Habit

class HabitRepository(private val habitDao: HabitDao){
    val habitList: LiveData<List<Habit>> = habitDao.getHabitList()

    @WorkerThread
    fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    @WorkerThread
    fun update(habit: Habit) {
        habitDao.update(habit)
    }

    @WorkerThread
    fun delete(habit: Habit) {
        habitDao.delete(habit)
    }
}