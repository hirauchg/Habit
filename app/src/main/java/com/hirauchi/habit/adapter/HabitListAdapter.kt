package com.hirauchi.habit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.habit.R
import com.hirauchi.habit.database.entity.Habit

class HabitListAdapter: RecyclerView.Adapter<HabitListAdapter.ViewHolder>() {

    private var mHabitList: List<Habit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_habit, parent, false))
    }

    override fun getItemCount(): Int {
        return mHabitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = mHabitList.get(position).name
    }

    fun setHabitList(habitList: List<Habit>) {
        mHabitList = habitList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.name)
    }
}