package com.cartrack.userlocation.ui

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Ro_BaseActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setFragment(fragment: Fragment) {
        setFragment(android.R.id.content, fragment)
    }

    fun setFragment(@IdRes containerViewId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(containerViewId, fragment, fragment.javaClass.simpleName)
            .commitNowAllowingStateLoss()
    }

}