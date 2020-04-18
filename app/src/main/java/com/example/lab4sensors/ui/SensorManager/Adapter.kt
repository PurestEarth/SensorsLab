package com.example.lab4sensors.ui.SensorManager

import android.content.Context
import android.content.res.Resources
import android.hardware.Sensor
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4sensors.MainActivity
import com.example.lab4sensors.R

class Adapter(
    var values: MutableList<Sensor>
): RecyclerView.Adapter<Adapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
        val adapter = this
        fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int) -> Unit): T {
            return this
        }
        return ViewHolder(itemView).listen { pos ->

        }
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            holder?.titleView?.text = values[position].name + " " + values[position].stringType
        }
    }

    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var titleView: TextView? = null
        init{
            titleView = itemView?.findViewById(R.id.textView_title)
        }
    }

    override fun getItemCount(): Int {
        return this.values.size
    }
}