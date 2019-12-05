package com.hirauchi.habit.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.hirauchi.habit.database.dao.RecordDao
import com.hirauchi.habit.database.entity.Record

class RecordRepository(private val recordDao: RecordDao, habitId: Int){
    val recordList: LiveData<List<Record>> = recordDao.getRecordList(habitId)

    @WorkerThread
    fun insert(record: Record) {
        recordDao.insert(record)
    }

    @WorkerThread
    fun update(record: Record) {
        recordDao.update(record)
    }

    @WorkerThread
    fun delete(record: Record) {
        recordDao.delete(record)
    }

    @WorkerThread
    fun deleteAllByHabitId(habitId: Int) {
        recordDao.deleteAllByHabitId(habitId)
    }
}