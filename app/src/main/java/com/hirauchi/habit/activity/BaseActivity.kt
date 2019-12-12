package com.hirauchi.habit.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.hirauchi.habit.R

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mFragment: Fragment

    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setSupportActionBar(findViewById(R.id.tool_bar))
        setUpToolbar()

        mFragment = setFragment()
        showFragment()

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    abstract fun setUpToolbar()

    abstract fun setFragment(): Fragment

    private fun showFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.base_container, mFragment)
        fragmentTransaction.commit()
    }
}