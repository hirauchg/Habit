package com.hirauchi.habit.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
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
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import com.hirauchi.habit.Constants
import com.hirauchi.habit.activity.RecordActivity

class HabitListFragment : Fragment(), HabitListAdapter.OnHabitListAdapter {

    private lateinit var mContext: Context
    private lateinit var mHabitListAdapter: HabitListAdapter
    private lateinit var mHabitViewModel: HabitViewModel

    val mHabitIcons = arrayOf(
        arrayOf(R.drawable.ic_fitness, R.drawable.ic_book, R.drawable.ic_eat, R.drawable.ic_car),
        arrayOf(R.drawable.ic_heart, R.drawable.ic_camera, R.drawable.ic_time, R.drawable.ic_game),
        arrayOf(R.drawable.ic_sports, R.drawable.ic_study, R.drawable.ic_muscle, R.drawable.ic_music),
        arrayOf(R.drawable.ic_pc, R.drawable.ic_pet, R.drawable.ic_phone, R.drawable.ic_drink)
    )

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
        mHabitListAdapter = HabitListAdapter(mContext, this, this)
        recyclerView.setAdapter(mHabitListAdapter)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showInputNameDialog()
        }
    }

    override fun onCardClicked(habit: Habit) {
        val intent = Intent(mContext, RecordActivity::class.java)
        intent.putExtra(Constants.KEY_HABIT, habit)
        startActivityForResult(intent, Constants.REQUEST_CODE_RECORD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mHabitListAdapter.notifyDataSetChanged()
    }

    override fun onNameClicked(habit: Habit) {
        showInputNameDialog(habit)
    }

    private fun showInputNameDialog(habit: Habit? = null) {
        var title = getString(R.string.add_abit_title)
        var message: String? = getString(R.string.add_habit_message)

        val editText = EditText(mContext)
        habit?.let {
            editText.setText(it.name)
            title = getString(R.string.edit_habit_title)
            message = null
        }

        val linearLayout = LinearLayout(mContext).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(dpToPx(24), 0, dpToPx(24), 0)
            }
            addView(editText, params)
        }

        AlertDialog.Builder(mContext)
            .setTitle(title).setMessage(message).setCancelable(false).setView(linearLayout)
            .setPositiveButton(R.string.ok, { _, _ ->
                val name = editText.text.toString()
                if (name.trim().isEmpty()) return@setPositiveButton

                habit?.also {
                    it.name = name
                    mHabitViewModel.update(it)
                } ?: run {
                    mHabitViewModel.insert(Habit(0, name, R.drawable.ic_fitness, System.currentTimeMillis()))
                }
            })
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onIconClicked(habit: Habit) {
        lateinit var alertDialog: AlertDialog

        val container = LinearLayout(mContext).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(10), dpToPx(10), dpToPx(10), 0)

            for (row in 0..3) {
                val rowRayout = LinearLayout(mContext).apply {
                    orientation = LinearLayout.HORIZONTAL
                    val rowRayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    rowRayoutParams.topMargin = dpToPx(10)
                    layoutParams = rowRayoutParams

                    for (col in 0..3) {
                        val imageView = ImageView(mContext).apply {
                            setImageResource(mHabitIcons[row][col])
                            setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary))
                            setOnClickListener {
                                habit.icon = mHabitIcons[row][col]
                                mHabitViewModel.update(habit)
                                alertDialog.dismiss()
                            }
                            if (mHabitIcons[row][col].equals(habit.icon)) {
                                isEnabled = false
                                alpha = 0.3F
                            }
                            layoutParams = LinearLayout.LayoutParams(dpToPx(56), dpToPx(56), 1F)
                        }

                        addView(imageView)
                    }
                }

                addView(rowRayout)
            }
        }

        alertDialog = AlertDialog.Builder(mContext)
            .setCancelable(false)
            .setView(container)
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDeleteClicked(habit: Habit) {
        AlertDialog.Builder(mContext)
                .setTitle(R.string.delete_habit_title)
                .setMessage(getString(R.string.delete_habit_message, habit.name))
                .setCancelable(false)
                .setPositiveButton(R.string.ok, { _, _ ->
                    mHabitViewModel.delete(habit)
                })
                .setNegativeButton(R.string.cancel, null)
                .show()
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = mContext.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}