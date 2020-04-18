package com.example.lab4sensors.ui.Game

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lab4sensors.R
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment(), SensorEventListener {
    private var xPosition = 0.0f
    private var xAcceleration = 0.0f
    private var xVelocity = 0.0f
    private var yPosition = 0.0f
    private var yAcceleration = 0.0f
    private var yVelocity = 0.0f
    private var xmax = 0.0f
    private var game = true
    private var ymax = 0.0f
    private val frameTime = 5f
    private var pristine = true
    private var startTime = 0L
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var sensorManager: SensorManager
    private var sensorGRV: Sensor? = null
    private var ball: ImageView? = null
    private var feed: TextView? = null
    private var startAgain: Button? = null
    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sensorManager =
            activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorGRV =
            sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_notifications, container, false)
        ball = root?.findViewById(R.id.imageView)
        feed = root?.findViewById(R.id.textView)
        startAgain = root?.findViewById(R.id.button)
        startAgain?.setOnClickListener {
            setUp()
        }
        startAgain?.visibility = INVISIBLE
        feed?.text = "Waiting for sensor to set up"
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        root?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        xmax = root?.measuredWidth?.toFloat() ?: 0f
        ymax = root?.measuredHeight?.toFloat() ?: 0f
        xPosition = xmax/2f
        yPosition = ymax/2f
        root?.setBackgroundColor(Color.parseColor("#00ff00"))
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume(){
        super.onResume()
        sensorGRV?.also { vector ->
            sensorManager.registerListener(this, vector,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (pristine){
            feed?.visibility = INVISIBLE
            startTime = System.currentTimeMillis()
            pristine = false
        }
        if (event != null && game) {
            yAcceleration = event.values[0]
            xAcceleration = event.values[1]
            updateBall();
        }
    }

    private fun updateBall (){
        xVelocity += xAcceleration * frameTime
        yVelocity += yAcceleration * frameTime

        xPosition += xVelocity * frameTime
        yPosition += yVelocity * frameTime

        Log.i("BENIZ", xPosition.toString())
        Log.i("BENIZ", yPosition.toString())

        if (xPosition > xmax){
            xPosition = xmax
            xVelocity = 0f
            lost()
        } else if ( xPosition < 0f){
            xPosition = 0f
            xVelocity = 0f
            lost()
        }
        if (yPosition > ymax){
            yPosition = ymax
            yVelocity = 0f
            lost()
        } else if ( yPosition < 0f){
            yPosition = 0f
            yVelocity = 0f
            lost()
        }

        //ball?.clearAnimation()
        //ball?.y?.let { ball?.x?.let { it1 -> animateTransition(it1, it, xPosition, yPosition) } }
         ball?.x = xPosition
         ball?.y = yPosition
    }

    private fun lost(){
        game = false
        val endtime = System.currentTimeMillis()
        feed?.visibility = VISIBLE
        button?.visibility = VISIBLE
        feed?.text = " Congratulations, you lasted ${((endtime - startTime) / 1000)}s"
    }

    private fun setUp(){
        game = true
        pristine = true
        xPosition = xmax/2
        yPosition = ymax/2
        feed?.visibility = INVISIBLE
        button?.visibility = INVISIBLE
    }

    /*@SuppressLint("ObjectAnimatorBinding")
    private fun animateTransition(xStart: Float, yStart:Float, xFin: Float, yFin: Float){
        ObjectAnimator.ofFloat(ball, "translationX", xStart, xFin).apply {
            duration = 300
            start()
        }
    }*/


}
