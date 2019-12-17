package com.hirauchi.habit.activity

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.hirauchi.appinfolibrary.AppInfoActivity
import com.hirauchi.habit.R
import com.hirauchi.habit.fragment.HabitListFragment

class HabitListActivity : BaseActivity() {

    override fun setContentView(@LayoutRes layoutResID: Int) {
        delegate.setContentView(R.layout.activity_habit_list)
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

    override fun setUpToolbar() {
        supportActionBar?.setTitle(R.string.app_name)
    }

    override fun setFragment(): Fragment {
        return HabitListFragment()
    }
}
