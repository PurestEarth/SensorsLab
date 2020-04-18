package com.example.lab4sensors.ui.GPS

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lab4sensors.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var txtLocation: TextView? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        txtLocation = root.findViewById(R.id.text_dashboard)
        mFusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        activity?.let { settlePermissions(it) }
        return root
    }

    override fun onPause(){
        super.onPause()
        if (mFusedLocationClient != null) {
            // mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    override fun onResume(){
        super.onResume()
        //todo
    }

    private fun settlePermissions(activity: Activity) {
        return if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED
            && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            Log.i("BENIZ", "Dalej nie ma")
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
        Log.i("BENIZ", "on request permission result")
        if(requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            handleLocationDisplay()
        } else {
            Log.i("BENIZ", "Failure")
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLocationDisplay(){
        mFusedLocationClient?.lastLocation?.addOnSuccessListener {
                location: Location? ->
            run {
                Log.i("BENIZ", location.toString())
                if(location !== null){
                    val wayLatitude = location.latitude
                    val wayLongitude = location.longitude
                    txtLocation?.text = (String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude))
                } else {
                    Toast.makeText(context, "Make sure location is enabled", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mFusedLocationClient?.lastLocation?.addOnFailureListener{
            Toast.makeText(context, "Make sure location is enabled", Toast.LENGTH_SHORT).show()
        }
    }

}
