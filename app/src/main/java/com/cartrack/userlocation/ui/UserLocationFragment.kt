package com.cartrack.userlocation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper.myLooper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cartrack.userlocation.R
import com.cartrack.userlocation.SessionManagerUtil
import com.cartrack.userlocation.data.api.model.LocationModel
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.ClickListener
import com.cartrack.userlocation.databinding.LocationCallBack
import com.cartrack.userlocation.databinding.UserLocationBinding
import com.cartrack.userlocation.utils.Utility.isLocationEnabled
import com.cartrack.userlocation.viewmodel.UserLocationViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLocationFragment : Fragment(), OnMapReadyCallback, ClickListener, LocationCallBack {
    companion object {
        private const val TAG = "Ro_UserLocationFragment"
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_EMAIL = "KEY_EMAIL"
        private const val KEY_CITY = "KEY_CITY"
        private const val KEY_WEBSITE = "KEY_WEBSITE"
        private const val KEY_COMPANY = "KEY_COMPANY"
        private const val KEY_LAT = "KEY_LAT"
        private const val KEY_LNG = "KEY_LNG"
        fun newInstance(): UserLocationFragment {
            return UserLocationFragment()
        }
    }

    private lateinit var binding: UserLocationBinding
    private lateinit var activity: Activity
    private val viewModel: UserLocationViewModel by viewModels()
    private val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mMap: GoogleMap
    var currentLocation: LatLng = LatLng(1.290270, 103.851959) //Singapore location

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (context is Activity) activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        val bundle = arguments
        binding = DataBindingUtil.inflate(inflater, R.layout.user_location, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.cvCity.text = bundle!!.getString(KEY_CITY)
        binding.cvName.text = bundle.getString(KEY_NAME)
        binding.cvUserName.text = bundle.getString(KEY_USER_NAME)
        binding.cvWebsite.text = bundle.getString(KEY_WEBSITE)
        binding.cvEmail.text = bundle.getString(KEY_EMAIL)
        binding.cvCompany.text = bundle.getString(KEY_COMPANY)
        if (bundle.getString(KEY_LAT) == "null" || bundle.getString(KEY_LAT) == "null") {
            currentLocation =
                LatLng(1.290270, 103.851959)

        } else {
            currentLocation =
                LatLng(
                    bundle.getString(KEY_LAT)?.toDouble()!!,
                    bundle.getString(KEY_LNG)?.toDouble()!!
                )
        }


        val appCompatActivity = activity
        if (appCompatActivity is AppCompatActivity) {
            appCompatActivity.setSupportActionBar(binding.toolbar)
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ai: ApplicationInfo = activity.applicationContext.packageManager
            .getApplicationInfo(
                activity.applicationContext.packageName,
                PackageManager.GET_META_DATA
            )
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()

        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(activity.applicationContext, apiKey)
        }

        // Initializing Map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        mapFragment.getMapAsync(this)

        // Initializing fused location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        // Adding functionality to the button
        binding.currentLoc.setOnClickListener {
            getLastLocation()
        }

        viewModel.locationChanged.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "location updated in settings")
            currentLocation = LatLng(it.latitude, it.longitude)
            getLastLocation()
        })
    }

    // Services such as getLastLocation()
    // will only run once map is ready
    override fun onMapReady(p0: GoogleMap) {
        Log.d(TAG, "onMapReady")
        mMap = p0
        loadUserLocation(currentLocation)
    }

    private fun loadUserLocation(currentLocation: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(currentLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10F))
    }

    // Get current location
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d(TAG, "-1-")
        if (checkPermissions()) {
            Log.d(TAG, "-2-")
            if (isLocationEnabled(activity)) {
                Log.d(TAG, "-3-")
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(currentLocation))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10F))
                    }
                }
            } else {
                Log.d(TAG, "isLocationEnabled is false, going to settings to turn on")
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            Log.d(TAG, "checkPermission is false")
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback, myLooper()!!
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "last location callback")
            val mLastLocation: Location = locationResult.lastLocation
            currentLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            viewModel.udpateLocation(LocationModel(mLastLocation.latitude, mLastLocation.longitude))
        }
    }

    // Check if location permissions are
    // granted to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "Permission granted check")
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    class GpsReceiver() : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive location callback")
            if (intent.action!!.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
                Log.d(TAG, "onReceive location callback---")
                UserLocationFragment().getLastLocation()
            }
        }
    }

    override fun onClick(view: View?) {

    }

    override fun onRecyclerViewClick(view: UserInfoRepositoryList?) {
    }

    override fun getLocation() {
        getLastLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                activity.onBackPressed()
                return true
            }
            R.id.logout -> {
                Toast.makeText(activity, "logout", Toast.LENGTH_LONG).show()
                SessionManagerUtil.endUserSession(activity)
                startActivity(LoginActivity.createIntent(activity))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLocationTriggered() {
        Log.d(TAG, "trigger location")
        getLastLocation()
    }

}