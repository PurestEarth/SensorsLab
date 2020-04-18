package com.example.lab4sensors.ui.SensorManager

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4sensors.R


class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sensorManager: SensorManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sensorManager =
            activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val deviceSensors: List<Sensor> =
            sensorManager.getSensorList(Sensor.TYPE_ALL)


        val root = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView =
            root.findViewById<RecyclerView>(R.id.recycler)!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Adapter(
            deviceSensors as MutableList<Sensor>
        )
        return root
    }

    override fun onPause(){
        super.onPause()
        //todo unsubscribe
    }

    override fun onResume(){
        super.onResume()
        // todo subscribe
    }
}
