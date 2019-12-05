package com.hirauchi.habit.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hirauchi.habit.database.entity.Record

@Dao
interface RecordDao {
    @Query("SELECT * FROM record WHERE habitId = :habitId")
    fun getRecordList(habitId: Int): LiveData<List<Record>>

    @Insert
    fun insert(record: Record)

    @Update
    fun update(record: Record)

    @Delete
    fun delete(record: Record)

    @Query("DELETE FROM record WHERE habitId = :habitId")
    fun deleteAllByHabitId(habitId: Int)
}
