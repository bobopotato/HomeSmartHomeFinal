package com.example.homesmarthome

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_window.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class WindowFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var resourcesList : MutableList<Resources>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_window, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resourcesList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("PI_01_20200904").child("20").child("4830")

        val count = 1

        if(count>0){
            ref.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    resourcesList.clear()
                    if(snapshot.exists()){
                        val resource = snapshot.getValue(Resources::class.java)
                        resourcesList.add(resource!!)
                        if(resourcesList.size>0){
                            humidityText1.text = "Current Humidity Outside : " + resourcesList[0].humid + " %"
                            if(resourcesList[0].humid.toFloat() >= 41 && resourcesList[0].humid.toInt() <= 79){
                                weatherText.text = "Today is a bit cloudy, it might be raining soon."
                            }
                            else if(resourcesList[0].humid.toFloat() <= 40){
                                weatherText.text = "Today is a sunny day, it won't be raining for the next few hours."
                            }
                            else{
                                weatherText.text = "It is raining outside, please close your window."
                            }
                        }
                    }
                }
            })


        }

        //humidityText.text = ref.toString()
        //resourcesList[0].humid.toString()
        //Toast.makeText(context, "hello world" + resourcesList[0].humid , Toast.LENGTH_SHORT).show()

    }



}