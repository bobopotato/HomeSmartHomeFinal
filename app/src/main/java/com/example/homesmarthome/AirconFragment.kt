package com.example.homesmarthome

import android.graphics.Color
import android.graphics.Color.rgb
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_aircon.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AirconFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AirconFragment : Fragment() {
    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference
    lateinit var ref2 : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root: View = inflater.inflate(R.layout.fragment_aircon, container, false)
        var checkActive : Int = 0
        var stop : Int = 0
        ref1 = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        ref2 = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {
                    checkActive = snapshot.child("relay").getValue().toString().toInt()
                    if (stop == 0) {
                        stop+=1 ;
                        if (checkActive == 1) {
                            root.closeBtn.isEnabled = false
                            root.openBtn.isEnabled = false
                            root.activateBtn.isEnabled = false
                            root.deactivateBtn.isEnabled = true
                            root.activateTxt.text = "Activated"
                            root.activateTxt2.text = "Closed"
                        } else {
                            root.closeBtn.isEnabled = true
                            root.openBtn.isEnabled = true
                            root.activateBtn.isEnabled = true
                            root.deactivateBtn.isEnabled = false
                            root.activateTxt.text = "Deactivated"
                            root.activateTxt2.text = "Closed"
                        }

                    }
                }
            }

        })



        root.openBtn.setOnClickListener {
            ref1.child("lcdtext").setValue("Aircon Is Opened");
            root.activateTxt2.text="Opened"
        }

        root.closeBtn.setOnClickListener {
            ref1.child("lcdtext").setValue("Aircon Is Closed");
            root.activateTxt2.text="Closed"
        }

        root.activateBtn.setOnClickListener{
            ref1.child("relay").setValue("1");
            ref1.child("lcdtext").setValue("Aircon Is Closed");
            root.activateTxt.text="Activated"
            root.closeBtn.isEnabled=false
            root.openBtn.isEnabled=false
            root.deactivateBtn.isEnabled=true
            root.activateBtn.isEnabled=false
            root.activateTxt2.text="Closed"
        }

        root.deactivateBtn.setOnClickListener {
            ref1.child("relay").setValue("0");
            ref1.child("lcdtext").setValue("Aircon Is Closed");
            root.activateTxt.text="Deactivated"
            root.closeBtn.isEnabled=true
            root.openBtn.isEnabled=true
            root.activateBtn.isEnabled=true
            root.deactivateBtn.isEnabled=false
            root.activateTxt2.text="Closed"
        }



        val mainHandler2 = Handler(Looper.getMainLooper())

        mainHandler2.post(object : Runnable {
            override fun run() {
                val string1 = "PI_01_" + getDate()
                getHour()
                ref = FirebaseDatabase.getInstance().getReference(string1).child(getHour())

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (h in snapshot.children) {
                                if(h.child("ultra2").getValue().toString().toDouble() <= 20){
                                    root.tempTxt.text =
                                        h.child("ultra2").getValue().toString() + "°"
                                    root.progress_circular.apply {
                                        progressMax = 100f
                                        setProgressWithAnimation(
                                            h.child("ultra2").getValue().toString().toFloat(),
                                            1000
                                        )
                                        progressBarWidth = 7f
                                        backgroundProgressBarWidth = 7f
                                        progressBarColor = Color.BLUE
                                    }

                                    if(root.activateTxt.text=="Activated") {
                                        root.activateTxt2.text="Closed"
                                    }
                                }else if(h.child("ultra2").getValue().toString().toDouble() > 30){
                                    if(root.activateTxt.text=="Activated") {
                                        root.activateTxt2.text="Opened"
                                    }
                                    root.tempTxt.text =
                                        h.child("ultra2").getValue().toString() + "°"
                                    root.progress_circular.apply {
                                        progressMax = 100f
                                        setProgressWithAnimation(
                                            h.child("ultra2").getValue().toString().toFloat(),
                                            1000
                                        )
                                        progressBarWidth = 7f
                                        backgroundProgressBarWidth = 7f
                                        progressBarColor = Color.RED
                                    }
                                }else{

                                    root.tempTxt.text =
                                        h.child("ultra2").getValue().toString() + "°"
                                    root.progress_circular.apply {
                                        progressMax = 100f
                                        setProgressWithAnimation(
                                            h.child("ultra2").getValue().toString().toFloat(),
                                            1000
                                        )
                                        progressBarWidth = 7f
                                        backgroundProgressBarWidth = 7f
                                        progressBarColor = rgb(255,165,0)
                                    }
                                }
                            }
                        }
                    }
                })

                if(root.activateTxt.text=="Activated") {
                    if(root.activateTxt2.text=="Closed") {
                        ref1.child("lcdtext").setValue("Aircon Is Closed");
                    }
                    else {
                        ref1.child("lcdtext").setValue("Aircon Is Opened");
                    }
                }
                mainHandler2.postDelayed(this, 10000)
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