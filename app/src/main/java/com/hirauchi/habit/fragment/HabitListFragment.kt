package com.hirauchi.habit.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hirauchi.habit.R
import com.hirauchi.habit.adapter.HabitListAdapter
import com.hirauchi.habit.database.entity.Habit
import com.hirauchi.habit.database.viewModel.HabitViewModel

class HabitListFragment : Fragment() {

    private lateinit var mContext: Context

    private val mHabitListAdapter = HabitListAdapter()

    private lateinit var mHabitViewModel: HabitViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onAttach(context: Context){
        mContext = context
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)

        mHabitViewModel = ViewModelProviders.of(this).get(HabitViewModel::class.java)
        mHabitViewModel.habitList.observe(this, Observer { habitList ->
            mHabitListAdapter.setHabitList(habitList)
        })
    }

    private fun setupView(view: View) {
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(mContext))
        recyclerView.setAdapter(mHabitListAdapter)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            mHabitViewModel.insert(Habit(0, "habit", "ic_asdf", System.currentTimeMillis()))
        }
    }
}