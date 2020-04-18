package com.example.lab4sensors.ui.GPS

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lab4sensors.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


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
        mFusedLocationClient?.lastLocation?.addOnSuccessListener {
                location: Location? -> Log.i("BENIZ", location.toString()) }
                //location: Location? -> txtLocation?.text = location.toString() }
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onPause(){
        super.onPause()
        //todo
    }

    override fun onResume(){
        super.onResume()
        //todo
    }
}
