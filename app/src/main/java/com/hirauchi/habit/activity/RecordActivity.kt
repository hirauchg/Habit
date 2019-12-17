package com.hirauchi.habit.activity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hirauchi.habit.Constants
import com.hirauchi.habit.database.entity.Habit
import com.hirauchi.habit.fragment.RecordFragment

class RecordActivity : BaseActivity() {

    private lateinit var mHabit: Habit

    override fun onCreate(savedInstanceState: Bundle?) {
        mHabit = intent.getSerializableExtra(Constants.KEY_HABIT) as Habit

        super.onCreate(savedInstanceState)
    }

    override fun setUpToolbar() {
        supportActionBar?.setTitle(mHabit.name)
        supportActionBar?.setLogo(mHabit.icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setFragment(): Fragment {
        return RecordFragment.newInstance(mHabit.id)
    }
}
