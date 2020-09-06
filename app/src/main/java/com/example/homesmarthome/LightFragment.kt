package com.example.homesmarthome

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_light.*
import kotlinx.android.synthetic.main.fragment_light.view.*
import kotlinx.android.synthetic.main.fragment_window.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LightFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_light, container, false)

        ref1 = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")

        val mainHandler1 = Handler(Looper.getMainLooper())

        mainHandler1.post(object : Runnable {
            override fun run() {

                ref1.addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            if(snapshot.child("led").getValue().toString()=="1"){
                                root.autoControl.text = "Activated"
                            }
                            else{
                                root.autoControl.text = "Deactivated"
                            }
                        }
                    }
                })
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
                                root.showLight.text =h.child("light").getValue().toString()
                                Log.d("abc",h.key.toString() + "=" + h.child("light").getValue().toString())

                                if (h.child("light").getValue().toString().toInt() >= 0 && h.child("light").getValue().toString().toInt() <= 20) {

                                    if(root.autoControl.text == "Activated") {
                                        root.lightImage.setImageResource(R.drawable.on)
                                        root.status.text = "ON"
                                    }

                                }
                                if (h.child("light").getValue().toString().toInt() > 20) {

                                    if(root.autoControl.text == "Activated") {
                                        root.lightImage.setImageResource(R.drawable.off)
                                        root.status.text = "OFF"
                                    }
                                }

                                //root.textView5.text = h.child("light").getValue().toString()
                            }
                        }
                    }
                })
                if(root.autoControl.text == "Activated"){
                    if(root.status.text == "ON"){
                        ref1.child("lcdtext").setValue("Light Is On");
                    } else if (root.status.text == "OFF"){
                        ref1.child("lcdtext").setValue("Light Is Off");
                    } else
                        ref1.child("lcdtext").setValue("=App Is Running=");
                }

                mainHandler1.postDelayed(this, 10000)
            }
        })

        root.btnOn.setOnClickListener{
            root.status.text = "ON"
            root.lightImage.setImageResource(R.drawable.on)
            ref1.child("lcdtext").setValue("Light Is On");
            Toast.makeText(context, "Light Is On", Toast.LENGTH_SHORT).show()
        }

        root.btnOff.setOnClickListener{
            root.status.text = "OFF"
            root.lightImage.setImageResource(R.drawable.off)
            ref1.child("lcdtext").setValue("Light Is Off");
            Toast.makeText(context, "Light Is Off", Toast.LENGTH_SHORT).show()
        }

        root.btnActive.setOnClickListener{
            ref1.child("led").setValue(1);
            root.status.text = "ON"
            root.lightImage.setImageResource(R.drawable.on)
            root.btnActive.isEnabled = false
            root.btnOff.isEnabled = false
            root.btnOn.isEnabled = false
            root.btnDeactive.isEnabled = true
            root.autoControl.text = "Activated"
            ref1.child("lcdtext").setValue("Light Is On");
            Toast.makeText(context, "Button Activated Is Clicked!!", Toast.LENGTH_SHORT).show()
        }

        root.btnDeactive.setOnClickListener{
            ref1.child("led").setValue(0);
            root.status.text = "OFF"
            root.lightImage.setImageResource(R.drawable.off)
            ref1.child("lcdtext").setValue("Light Is Off");
            root.btnDeactive.isEnabled = false
            root.btnOn.isEnabled = true
            root.btnOff.isEnabled = true
            root.btnActive.isEnabled = true
            root.autoControl.text = "Deactivated"
            Toast.makeText(context, "Button Deactivated Is Clicked!!", Toast.LENGTH_SHORT).show()
        }

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