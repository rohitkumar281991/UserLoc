package com.cartrack.userlocation.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserLocationActivity : BaseActivity() {

    companion object {
        private const val TAG = "Ro_UserLocationActivity"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_EMAIL = "KEY_EMAIL"
        private const val KEY_CITY = "KEY_CITY"
        private const val KEY_WEBSITE = "KEY_WEBSITE"
        private const val KEY_COMPANY = "KEY_COMPANY"
        private const val KEY_LAT = "KEY_LAT"
        private const val KEY_LNG = "KEY_LNG"
        fun createIntent(
            context: Context,
            data: UserInfoRepositoryList?
        ): Intent {
            val intent = Intent(context, UserLocationActivity::class.java)
            intent.putExtra(KEY_NAME, data?.name)
            intent.putExtra(KEY_USER_NAME, data?.username)
            intent.putExtra(KEY_EMAIL, data?.email)
            intent.putExtra(KEY_CITY, data?.address?.city)
            intent.putExtra(KEY_WEBSITE, data?.website)
            intent.putExtra(KEY_COMPANY, data?.company?.name)
            intent.putExtra(KEY_LAT, data?.address?.geo?.lat.toString())
            intent.putExtra(KEY_LNG, data?.address?.geo?.lng.toString())
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate ")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDataToFragment(UserLocationFragment.newInstance())
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            UserLocationFragment.GpsReceiver(),
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    private fun setDataToFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString(KEY_NAME, intent.getStringExtra(KEY_NAME))
        bundle.putString(KEY_USER_NAME, intent.getStringExtra(KEY_USER_NAME))
        bundle.putString(KEY_EMAIL, intent.getStringExtra(KEY_EMAIL))
        bundle.putString(KEY_CITY, intent.getStringExtra(KEY_CITY))
        bundle.putString(KEY_WEBSITE, intent.getStringExtra(KEY_WEBSITE))
        bundle.putString(KEY_COMPANY, intent.getStringExtra(KEY_COMPANY))
        bundle.putString(KEY_LAT, intent.getStringExtra(KEY_LAT))
        bundle.putString(KEY_LNG, intent.getStringExtra(KEY_LNG))

        fragment.arguments = bundle

        setFragment(fragment)
    }
}