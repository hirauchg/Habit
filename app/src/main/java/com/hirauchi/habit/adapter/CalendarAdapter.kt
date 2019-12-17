package com.hirauchi.habit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.hirauchi.habit.R
import com.hirauchi.habit.database.entity.Record
import com.hirauchi.habit.model.CalendarDate
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(val mContext: Context, val mListener: OnClickListener): BaseAdapter() {

    private var mDateList = listOf<CalendarDate>()
    private var mMonth = Calendar.getInstance()

    interface OnClickListener {
        fun onItemClicked(date: Date, record: Record?)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val calendarDate = mDateList.get(position)

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val newConvertView = inflater.inflate(R.layout.list_item_calendar_date, null)

        ViewHolder().apply {
            dateContainer = newConvertView.findViewById(R.id.container)
            dateContainer.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = calendarDate.date
                if (calendar.before(Calendar.getInstance())) {
                    mListener.onItemClicked(calendarDate.date, calendarDate.record)
                }
            }

            val height : Int = (50 * mContext.getResources().getDisplayMetrics().density).toInt()
            val params = AbsListView.LayoutParams(parent!!.width / 7, height)
            newConvertView.layoutParams = params

            val colorId = if (isCurrentMonth(calendarDate.date, mMonth.time)) {
                when (getDayOfWeek(calendarDate.date)) {
                    1 -> R.color.colorThisMonthSunText
                    7 -> R.color.colorThisMonthSatText
                    else -> R.color.colorText1
                }
            } else {
                when (getDayOfWeek(calendarDate.date)) {
                    1 -> R.color.colorOtherMonthSunText
                    7 -> R.color.colorOtherMonthSatText
                    else -> R.color.colorText2
                }
            }

            dateText = newConvertView.findViewById(R.id.date_text)
            dateText.apply {
                setTextColor(ContextCompat.getColor(mContext, colorId))
                setText(SimpleDateFormat("d", Locale.US).format(calendarDate.date))
                if (calendarDate.isToday) {
                    setText(R.string.calendar_today)
                }
            }

            recordMark = newConvertView.findViewById(R.id.record_image)
            recordMark.apply {
                calendarDate.record?.also {
                    visibility = View.VISIBLE
                    if (it.status == 0) {
                        setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_check))
                    } else {
                        setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_skip))
                    }
                } ?:run {
                    visibility = View.GONE
                }
            }
        }

        return newConvertView
    }

    override fun getItem(position: Int): Any {
        return mDateList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mDateList.count()
    }

    fun isCurrentMonth(date: Date, month: Date): Boolean {
        val format = SimpleDateFormat("yyyy/MM", Locale.US)
        return format.format(month).equals(format.format(date))
    }

    fun getDayOfWeek(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    inner class ViewHolder {
        lateinit var dateContainer: RelativeLayout
        lateinit var dateText: TextView
        lateinit var recordMark: ImageView
    }

    fun setUpCalendar(dateList: List<CalendarDate>, month: Calendar) {
        mDateList = dateList
        mMonth = month
        notifyDataSetChanged()
    }
}