package com.hirauchi.habit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val icon: String,
    val start: Long
)