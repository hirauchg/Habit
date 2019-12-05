package com.hirauchi.habit.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hirauchi.habit.database.entity.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getHabitList(): LiveData<List<Habit>>

    @Insert
    fun insert(habit: Habit)

    @Update
    fun update(habit: Habit)

    @Delete
    fun delete(habit: Habit)
}
