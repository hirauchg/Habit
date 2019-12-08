package com.hirauchi.habit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.habit.R
import com.hirauchi.habit.database.entity.Habit
import java.text.SimpleDateFormat
import java.util.*

class HabitListAdapter(val mContext: Context, val mListener: OnHabitListAdapter): RecyclerView.Adapter<HabitListAdapter.ViewHolder>() {

    private var mHabitList: List<Habit> = ArrayList()

    interface OnHabitListAdapter {
        fun onCardClicked()
        fun onNameClicked(habit: Habit)
        fun onIconClicked()
        fun onDeleteClicked(habit: Habit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_habit, parent, false))
    }

    override fun getItemCount(): Int {
        return mHabitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = mHabitList.get(position)

        holder.card.setOnClickListener {
            mListener.onCardClicked()
        }

        holder.icon.setImageResource(habit.icon)
        holder.icon.setOnClickListener {
            mListener.onIconClicked()
        }

        holder.name.text = mHabitList.get(position).name
        holder.name.setOnClickListener {
            mListener.onNameClicked(habit)
        }

        holder.delete.setOnClickListener {
            mListener.onDeleteClicked(habit)
        }

        // TODO
        holder.startDate.text = SimpleDateFormat(mContext.getString(R.string.habit_start_date), Locale.US).format(Date(habit.start))
        holder.achievementRate.text = mContext.getString(R.string.habit_achievement_rate, 90)
        holder.continuedDays.text = mContext.getString(R.string.habit_continued_days, 0)
    }

    fun setHabitList(habitList: List<Habit>) {
        mHabitList = habitList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
        val delete: ImageView = view.findViewById(R.id.delete)
        val startDate: TextView = view.findViewById(R.id.start_date)
        val achievementRate: TextView = view.findViewById(R.id.achievement_rate)
        val continuedDays: TextView = view.findViewById(R.id.continued_days)

    }
}