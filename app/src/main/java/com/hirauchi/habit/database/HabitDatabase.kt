package com.hirauchi.habit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hirauchi.habit.database.dao.HabitDao
import com.hirauchi.habit.database.dao.RecordDao
import com.hirauchi.habit.database.entity.Habit
import com.hirauchi.habit.database.entity.Record

@Database(entities = arrayOf(Habit::class, Record::class), version = 1)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit2_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}