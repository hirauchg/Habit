package com.hirauchi.habit.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hirauchi.habit.R

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        supportActionBar?.setDisplayHomeAsUpEnabled(setBackButton())

        title = setTitle()

        mFragment = setFragment()
        showFragment()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    abstract fun setBackButton(): Boolean

    abstract fun setTitle(): String

    abstract fun setFragment(): Fragment

    private fun showFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.base_container, mFragment)
        fragmentTransaction.commit()
    }
}