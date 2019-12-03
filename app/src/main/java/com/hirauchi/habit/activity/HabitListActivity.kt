package com.hirauchi.habit.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.hirauchi.appinfo.AppInfoActivity
import com.hirauchi.habit.R
import com.hirauchi.habit.fragment.HabitListFragment

class HabitListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_habit_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_app_info -> startActivity(Intent(this, AppInfoActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setBackButton(): Boolean {
        return false
    }

    override fun setTitle(): String {
        return getString(R.string.app_name)
    }

    override fun setFragment(): Fragment {
        return HabitListFragment()
    }
}
