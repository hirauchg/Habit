package com.hirauchi.habit.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hirauchi.habit.R
import com.hirauchi.habit.adapter.CalendarAdapter
import com.hirauchi.habit.database.entity.Record
import com.hirauchi.habit.database.viewModel.RecordViewModel
import com.hirauchi.habit.model.CalendarDate
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment(), CalendarAdapter.OnClickListener {

    private lateinit var mCalendarAdapter: CalendarAdapter
    private lateinit var mRecordViewModel: RecordViewModel
    private lateinit var mContext: Context
    private lateinit var mRecordList: List<Record>
    private lateinit var mMonthTextView: TextView
    private var mMonth = Calendar.getInstance()

    var mHabitId = -1

    companion object {
        const val KEY_HABIT_ID = "key_habit_id"

        fun newInstance(habitId : Int): RecordFragment {
            val recordFragment = RecordFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_HABIT_ID, habitId)
            recordFragment.arguments = bundle
            return recordFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mHabitId = it.getInt(KEY_HABIT_ID)
        }
    }

    override fun onAttach(context: Context){
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMonthTextView = view.findViewById<TextView>(R.id.month_text).apply {
            text = SimpleDateFormat(context!!.getString(R.string.calendar_month_format), Locale.US).format(mMonth.time)
        }

        view.findViewById<RelativeLayout>(R.id.left_arrow).apply {
            setOnClickListener { changeMonth(-1) }
        }

        view.findViewById<RelativeLayout>(R.id.right_arrow).apply {
            setOnClickListener { changeMonth(1) }
        }

        mCalendarAdapter = CalendarAdapter(mContext, this)
        view.findViewById<GridView>(R.id.calendar_view).apply {
            adapter = mCalendarAdapter
        }

        mRecordViewModel = ViewModelProviders.of(this, RecordViewModel.Factory(activity!!.application, mHabitId)).get(RecordViewModel::class.java)
        mRecordViewModel.recordList.observe(this, Observer { recordList ->
            mRecordList = recordList
            val dateList = getDateList(mRecordList)
            mCalendarAdapter.setUpCalendar(dateList, mMonth)

            if (recordList.isEmpty()) {
                view.findViewById<TextView>(R.id.start_date).text = mContext.getString(R.string.habit_start_date_none)
                view.findViewById<TextView>(R.id.achievement_rate).text = mContext.getString(R.string.habit_achievement_rate, 0)
                view.findViewById<TextView>(R.id.continued_days).text = mContext.getString(R.string.habit_continued_days, 0)
            } else {
                view.findViewById<TextView>(R.id.start_date).text = SimpleDateFormat(mContext.getString(R.string.habit_start_date), Locale.US).format(Date(recordList.last().date))
                view.findViewById<TextView>(R.id.continued_days).text = mContext.getString(R.string.habit_continued_days, 0)

                var last = 0L
                var beforeCalendar = Calendar.getInstance().apply {
                    clear(Calendar.MINUTE)
                    clear(Calendar.SECOND)
                    clear(Calendar.MILLISECOND)
                    set(Calendar.HOUR_OF_DAY, 0)
                    last = timeInMillis
                }

                for (i in 0..recordList.size - 1) {
                    val nextCalendar = Calendar.getInstance().apply {
                        timeInMillis = recordList[i].date
                        clear(Calendar.MINUTE)
                        clear(Calendar.SECOND)
                        clear(Calendar.MILLISECOND)
                        set(Calendar.HOUR_OF_DAY, 0)
                    }

                    if (nextCalendar.before(beforeCalendar)) {
                        view.findViewById<TextView>(R.id.continued_days).text = mContext.getString(R.string.habit_continued_days, i)
                        break
                    } else if (i == recordList.size - 1) {
                        view.findViewById<TextView>(R.id.continued_days).text = mContext.getString(R.string.habit_continued_days, i + 1)
                    }

                    nextCalendar.add(Calendar.DAY_OF_MONTH, -1)
                    beforeCalendar = nextCalendar
                }

                val c = Calendar.getInstance().apply {
                    timeInMillis = recordList.last().date
                    clear(Calendar.MINUTE)
                    clear(Calendar.SECOND)
                    clear(Calendar.MILLISECOND)
                    set(Calendar.HOUR_OF_DAY, 0)
                }

                val start = c.timeInMillis
                val diff = (last - start) / (1000 * 60 * 60 * 24) + 1
                val rate = ((recordList.size.toDouble() / diff.toDouble()) * 100).toInt()
                view.findViewById<TextView>(R.id.achievement_rate).text = mContext.getString(R.string.habit_achievement_rate, rate)
            }
        })
    }

    private fun changeMonth(amount: Int) {
        mMonth.add(Calendar.MONTH, amount)
        mMonthTextView.text = SimpleDateFormat(context!!.getString(R.string.calendar_month_format), Locale.US).format(mMonth.time)
        val dateList = getDateList(mRecordList)
        mCalendarAdapter.setUpCalendar(dateList, mMonth)
    }

    fun getDateList(recordList: List<Record>): List<CalendarDate> {
        val calendarDateList = arrayListOf<CalendarDate>()

        Calendar.getInstance().apply {
            set(mMonth.get(Calendar.YEAR), mMonth.get(Calendar.MONTH), 1)
            val dayOfWeek = get(Calendar.DAY_OF_WEEK) - 1
            add(Calendar.DATE, -dayOfWeek)

            for (i in 0..41) {
                val cal = Calendar.getInstance()
                var record: Record? = null
                val isToday = isSameDate(time, cal.time)

                for (r in recordList) {
                    val c = Calendar.getInstance()
                    c.timeInMillis = r.date
                    if (isSameDate(time, c.time)) {
                        record = r
                    }
                }

                val calendarDate = CalendarDate(time, record, isToday)
                calendarDateList.add(calendarDate)
                add(Calendar.DATE, 1)
            }
        }

        return calendarDateList
    }

    fun isSameDate(date: Date, diaryDate: Date): Boolean {
        val format = SimpleDateFormat("MM/dd", Locale.US)
        return format.format(date).equals(format.format(diaryDate))
    }

    override fun onItemClicked(date: Date, record: Record?) {
        record?.also {
            if (it.status == 0) {
                it.status = 1
                mRecordViewModel.update(it)
            } else {
                mRecordViewModel.delete(it)
            }
        } ?:run {
            mRecordViewModel.insert(Record(0, mHabitId, 0, date.time))
        }
    }
}