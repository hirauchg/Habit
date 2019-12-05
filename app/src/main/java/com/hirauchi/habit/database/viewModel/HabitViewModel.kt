package com.hirauchi.habit.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hirauchi.habit.database.HabitDatabase
import com.hirauchi.habit.database.entity.Habit
import com.hirauchi.habit.database.repository.HabitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HabitViewModel(application: Application): AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: HabitRepository
    val habitList: LiveData<List<Habit>>

    init{
        val habitDao = HabitDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(habitDao)
        habitList = repository.habitList
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(habit: Habit) = scope.launch(Dispatchers.IO){
        repository.insert(habit)
    }

    fun update(habit: Habit) = scope.launch(Dispatchers.IO){
        repository.update(habit)
    }

    fun delete(habit: Habit) = scope.launch(Dispatchers.IO){
        repository.delete(habit)
    }
}