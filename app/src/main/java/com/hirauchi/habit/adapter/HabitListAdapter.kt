package com.hirauchi.habit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.hirauchi.habit.R
import com.hirauchi.habit.database.entity.Habit
import com.hirauchi.habit.database.viewModel.RecordViewModel
import java.text.SimpleDateFormat
import java.util.*

class HabitListAdapter(val mContext: Context, val mFragment: Fragment, val mListener: OnHabitListAdapter): RecyclerView.Adapter<HabitListAdapter.ViewHolder>() {

    private var mHabitList: List<Habit> = ArrayList()

    interface OnHabitListAdapter {
        fun onCardClicked(habit: Habit)
        fun onNameClicked(habit: Habit)
        fun onIconClicked(habit: Habit)
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

        holder.apply {
            card.setOnClickListener {
                mListener.onCardClicked(habit)
            }

            icon.apply {
                setImageResource(habit.icon)
                setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary))
                setOnClickListener {
                    mListener.onIconClicked(habit)
                }
            }

            name.apply {
                text = mHabitList.get(position).name
                setOnClickListener {
                    mListener.onNameClicked(habit)
                }
            }

            delete.setOnClickListener {
                mListener.onDeleteClicked(habit)
            }

            val recordViewModel = ViewModelProviders.of(mFragment, RecordViewModel.Factory(mFragment.activity!!.application, habit.id)).get(RecordViewModel::class.java)
            val recordList = recordViewModel.getRecordList(habit.id)
            if (recordList.isEmpty()) {
                startDate.text = SimpleDateFormat(mContext.getString(R.string.habit_start_date), Locale.US).format(Date(habit.start))
                achievementRate.text = mContext.getString(R.string.habit_achievement_rate, 0)
                continuedDays.text = mContext.getString(R.string.habit_continued_days, 0)
            } else {
                startDate.text = SimpleDateFormat(mContext.getString(R.string.habit_start_date), Locale.US).format(Date(recordList.last().date))
                continuedDays.text = mContext.getString(R.string.habit_continued_days, 0)

                var beforeCalendar = Calendar.getInstance().apply {
                    clear(Calendar.MINUTE)
                    clear(Calendar.SECOND)
                    clear(Calendar.MILLISECOND)
                    set(Calendar.HOUR_OF_DAY, 0)
                }

                for (i in 0..recordList.size - 1) {
                    val nextCalendar = Calendar.getInstance().apply {
                        timeInMillis = recordList[i].date
                        clear(Calendar.MINUTE)
                        clear(Calendar.SECOND)
                        clear(Calendar.MILLISECOND)
                        set(Calendar.HOUR_OF_DAY, 0)
                        add(Calendar.DAY_OF_MONTH, 1)
                    }

                    if (nextCalendar.before(beforeCalendar) || i == recordList.size - 1) {
                        continuedDays.text = mContext.getString(R.string.habit_continued_days, i + 1)
                        break
                    }

                    nextCalendar.add(Calendar.DAY_OF_MONTH, -1)
                    beforeCalendar = nextCalendar
                }

                val start = recordList.last().date
                val last = System.currentTimeMillis()
                val diff = (last - start) / (1000 * 60 * 60 * 24) + 1
                val rate = ((recordList.size.toDouble() / diff.toDouble()) * 100).toInt()
                achievementRate.text = mContext.getString(R.string.habit_achievement_rate, rate)
            }
        }
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