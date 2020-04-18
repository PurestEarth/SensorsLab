package com.example.lab4sensors.ui.GPS

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lab4sensors.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.lang.Math.abs
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var txtLocation: TextView? = null
    private var homeTextViewt: TextView? = null
    private var errorTextView: TextView? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var homeLat = 0.0
    private var homeLong = 0.0

    var mLocationRequest: LocationRequest? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        txtLocation = root.findViewById(R.id.text_dashboard)
        homeTextViewt = root.findViewById(R.id.textView2)
        errorTextView = root.findViewById(R.id.textView3)

        mFusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        activity?.let { settlePermissions(it) }
        requestLocationUpdates()
        return root
    }

    override fun onPause(){
        super.onPause()
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume(){
        super.onResume()
        if (mFusedLocationClient != null) {
            requestLocationUpdates()
        }
    }

    private fun settlePermissions(activity: Activity) {
        return if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED
            && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000)
        } else if(context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED
                    && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED) {
            handleLocationDisplay()
        }
        else {
            Toast.makeText(context, "Uzurpator", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            handleLocationDisplay()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLocationDisplay(){
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val wayLatitude = location.latitude
                    val wayLongitude = location.longitude
                    txtLocation?.text = (String.format(Locale.US, "%s -- %s -- %s", "Location: ", wayLatitude, wayLongitude))
                    if (homeTextViewt?.text?.isEmpty()!!){
                        homeTextViewt?.text = (String.format(Locale.US, "%s -- %s -- %s", "Home: ", wayLatitude, wayLongitude))
                        homeTextViewt?.visibility = View.VISIBLE
                        homeLat = wayLatitude
                        homeLong = wayLongitude
                    }
                    if(kotlin.math.abs(wayLatitude - homeLat) > 0.1 || kotlin.math.abs(wayLongitude - homeLong) > 0.1){
                        errorTextView?.visibility = View.VISIBLE
                    }
                    if(errorTextView?.visibility == View.VISIBLE && kotlin.math.abs(wayLatitude - homeLat) < 0.1 || kotlin.math.abs(wayLongitude - homeLong) < 0.1){
                        errorTextView?.visibility = View.INVISIBLE
                    }
                }
                if(locationResult.locations.size == 0){
                    Toast.makeText(context, "Make sure location is enabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun requestLocationUpdates(){
        mLocationRequest = LocationRequest()
        // Read changes every 5 seconds, due to nature of the app
        mLocationRequest?.interval = 5000
        mLocationRequest?.fastestInterval = 5000
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        }
    }

}
