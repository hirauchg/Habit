package com.hirauchi.habit.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hirauchi.habit.database.HabitDatabase
import com.hirauchi.habit.database.entity.Record
import com.hirauchi.habit.database.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordViewModel(application: Application, habitId: Int): AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: RecordRepository
    val recordList: LiveData<List<Record>>

    init{
        val recordDao = HabitDatabase.getDatabase(application).recordDao()
        repository = RecordRepository(recordDao, habitId)
        recordList = repository.recordList
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(record: Record) = scope.launch(Dispatchers.IO){
        repository.insert(record)
    }

    fun update(record: Record) = scope.launch(Dispatchers.IO){
        repository.update(record)
    }

    fun delete(record: Record) = scope.launch(Dispatchers.IO){
        repository.delete(record)
    }

    fun deleteAllByHabitId(habitId: Int) = scope.launch(Dispatchers.IO){
        repository.deleteAllByHabitId(habitId)
    }

    class Factory(private val application: Application, private val habitId: Int) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecordViewModel(application, habitId) as T
        }
    }
}