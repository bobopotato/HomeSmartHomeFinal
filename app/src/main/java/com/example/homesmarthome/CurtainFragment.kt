package com.example.homesmarthome

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_curtain.*
import kotlinx.android.synthetic.main.fragment_curtain.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurtainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurtainFragment : Fragment() {
    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_curtain, container, false)

        ref1 = FirebaseDatabase.getInstance().getReference("PI_01_CONTROL")



        val mainHandler1 = Handler(Looper.getMainLooper())

        root.textView7.text = " Current mode Manual"
        root.button3.setOnClickListener{
            root.textView7.text = "Current mode Auto"
        }

        root.button4.setOnClickListener(){
            root.textView7.text = " Current mode Manual"
        }

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
                                root.textView12.text = "Light: " + h.child("light").getValue().toString()

                                Log.d("abc",h.key.toString() + "=" + h.child("light").getValue().toString())
                                if(root.textView7.text == "Current mode Auto"){
                                    if (h.child("light").getValue().toString().toInt() >= 0 && h.child("light").getValue().toString().toInt() <= 50){
                                        root.textView22.text = "The Curtain is close"
                                        root.textView42.text = "Moon"
                                        root.imageView.setImageResource(R.drawable.moon)


                                    }else {
                                        root.textView22.text = "The Curtain is open"
                                        root.textView42.text = "Sun"
                                        root.imageView.setImageResource(R.drawable.sun)
                                    }
                                }

                            }
                        }
                    }
                })
                mainHandler1.postDelayed(this, 10000)
            }
        })


        root.button.setOnClickListener(){

            var map = mutableMapOf<String,Any>()
            map ["led"] = "1"


            Toast.makeText(context,"Button is Clicked", Toast.LENGTH_LONG).show()

            ref1.updateChildren(map)
            root.textView.text = "Curtain is OPEN"

        }


        root.button2.setOnClickListener(){

            var map = mutableMapOf<String,Any>()
            map ["led"] = "0"

            Toast.makeText(context,"Button is Clicked", Toast.LENGTH_LONG).show()

            ref1.updateChildren(map)
            root.textView.text = "Curtain is CLOSE"

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