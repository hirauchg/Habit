package com.hirauchi.habit.model

import com.hirauchi.habit.database.entity.Record
import java.util.*

data class CalendarDate(
    val date: Date,
    val record: Record?,
    val isToday: Boolean
)