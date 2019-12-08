package com.hirauchi.habit.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
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

class HabitListFragment : Fragment(), HabitListAdapter.OnHabitListAdapter {
    override fun onCardClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNameClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onIconClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mContext: Context
    private lateinit var mHabitListAdapter: HabitListAdapter
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
        mHabitListAdapter = HabitListAdapter(mContext, this)
        recyclerView.setAdapter(mHabitListAdapter)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun showAddHabitDialog() {
        val view = LinearLayout(mContext)
        val editText = EditText(mContext)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(40, 0, 40, 0)
        view.addView(editText, params)

        AlertDialog.Builder(mContext)
                .setTitle(R.string.add_abit_title)
                .setMessage(R.string.add_habit_message)
                .setCancelable(false)
                .setView(view)
                .setPositiveButton(R.string.ok, { dialog, which ->
                    val name = editText.text.toString()
                    if (name.trim().isEmpty()) return@setPositiveButton

                    mHabitViewModel.insert(Habit(0, name, R.drawable.ic_time, System.currentTimeMillis()))
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }
}