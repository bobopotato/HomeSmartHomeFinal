package com.example.homesmarthome

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.autofill.Dataset
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_smart_window_report.*
import kotlinx.android.synthetic.main.fragment_window.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class smartWindowReport : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_window_report)

        val barEntries: ArrayList<BarEntry> = ArrayList()
        val theDates: ArrayList<String> = ArrayList()


        bargraph.setNoDataText("Loading...")

        val string1 = "PI_01_" + getDate()
        var stop = 0
        var stop1 = 0
        ref = FirebaseDatabase.getInstance().getReference(string1)

        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists() && stop ==0) {
                    var count = 0;

                    for (h in snapshot.children) {
                        ref1 = FirebaseDatabase.getInstance().getReference(string1).child(h.key.toString()) //01 - 22
                        ref1.addValueEventListener(object: ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    var total = 0.0
                                    var dataCount = 0
                                    for (i in snapshot.children){
                                        //total = total + h.child("ultra2").getValue().toString().toFloat();

                                        dataCount = dataCount + 1
                                        total = total + i.child("ultra2").getValue().toString().toFloat()

                                    }
                                    if(stop1 < count){
                                        //Log.d("testingmou1", count.toString() + " aa " + snapshot.key.toString() + " = " + total + "/" + dataCount.toString() +" = " + (total/dataCount).toString())
                                        barEntries.add(BarEntry((total/dataCount).toFloat(), stop1))
                                        //barEntries.add(BarEntry(12f, stop1))
                                        theDates.add(snapshot.key.toString()+"00")

                                    }
                                    else if(stop1 == count){
                                        Log.d("testingmou11", barEntries.toString())
                                        val barDataSet = BarDataSet(barEntries, "Cells")
                                        val theData =BarData(theDates, barDataSet)
                                        Log.d("testingmou11", theDates.toString())

                                        bargraph.setData(theData)
                                        bargraph.invalidate()
                                    }
                                    stop1 = stop1 +1
                                }
                            }
                        })
                        count++;
                    }
                    stop = stop +1
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate():String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatted = current.format(formatter)
        return formatted
    }

}