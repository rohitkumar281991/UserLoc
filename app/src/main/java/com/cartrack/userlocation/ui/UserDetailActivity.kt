package com.cartrack.userlocation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.cartrack.userlocation.R
import com.cartrack.userlocation.SessionManagerUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : BaseActivity() {
    companion object {
        private const val TAG = "Ro_UserDetailActivity"
        fun createIntent(
            context: Context
        ): Intent {
            return Intent().apply {
                setClass(context, UserDetailActivity::class.java)
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setFragment(UserDetailFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Toast.makeText(this, "logout", Toast.LENGTH_LONG).show()
            SessionManagerUtil.endUserSession(this)
            startActivity(LoginActivity.createIntent(this))
            finish()
            return true
        }
        return false
    }

}
