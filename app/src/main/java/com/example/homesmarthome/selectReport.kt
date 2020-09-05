package com.example.homesmarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_report.*
import kotlinx.android.synthetic.main.fragment_home.*

class selectReport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_report)

        lightningReport.setOnClickListener {
            startActivity(Intent(this, lightReport::class.java))
        }
        temperatureReport.setOnClickListener {
            startActivity(Intent(this, tempReport::class.java))
        }
        humidityReport.setOnClickListener {
            startActivity(Intent(this, smartWindowReport::class.java))
        }
    }
}