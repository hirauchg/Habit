package com.hirauchi.habit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val habitId: Int,
    var status: Int,
    val date: Long
)