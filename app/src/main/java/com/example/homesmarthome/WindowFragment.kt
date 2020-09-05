package com.example.homesmarthome

import android.content.Context
import android.graphics.Color.rgb
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_window.*
import kotlinx.android.synthetic.main.fragment_window.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.fixedRateTimer

class WindowFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_window, container, false)

        ref1 = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")

        val mainHandler1 = Handler(Looper.getMainLooper())

        mainHandler1.post(object : Runnable {
            override fun run() {

                val string1 = "PI_01_" + getDate()
                getHour()
                ref = FirebaseDatabase.getInstance().getReference(string1).child(getHour())

                ref.addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            for (h in snapshot.children) {
                                //var map = mutableMapOf<String,Any>()
                                //map["humid"] = "45"
                                //ref.child(h.key.toString()).updateChildren(map)

                                root.percentage.text = h.child("ultra2").getValue().toString() + " %"

                                root.humidPercen.apply {
                                    progressMax = 100f
                                    setProgressWithAnimation(h.child("ultra2").getValue().toString().toFloat(), 1000)
                                    progressBarWidth = 10f
                                    backgroundProgressBarWidth = 4f
                                    progressBarColor = rgb(38, 153, 251)
                                }
                                if (h.child("ultra2").getValue().toString().toFloat() >= 41 && h.child("ultra2").getValue().toString().toFloat() <= 60) {
                                    root.weatherText.text = "Today is a bit cloudy, it might be raining soon."
                                    root.weatherType.text = "Cloudy"
                                    root.weatherImage.setImageResource(R.drawable.cloudy)
                                } else if (h.child("ultra2").getValue().toString().toFloat() <= 40) {
                                    root.weatherText.text = "Today is a sunny day, it won't be raining for the next few hours."
                                    root.weatherType.text = "Sunny"
                                    root.weatherImage.setImageResource(R.drawable.sunny)
                                } else {
                                    root.weatherText.text = "It is raining outside, please close your window."
                                    root.weatherType.text = "Raining"
                                    root.weatherImage.setImageResource(R.drawable.raining)
                                }
                            }
                        }
                    }
                })
                mainHandler1.postDelayed(this, 10000)
            }
        })

        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatted = current.format(formatter)
        return formatted
    }

    private fun getHour():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = current.format(formatter)
        return formatted
    }



}