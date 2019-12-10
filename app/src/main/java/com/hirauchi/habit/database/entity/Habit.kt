package com.hirauchi.habit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    var name: String,
    var icon: Int,
    val start: Long
)