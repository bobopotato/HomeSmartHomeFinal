package com.example.homesmarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val lightFragment = LightFragment()
        val airconFragment = AirconFragment()
        val windowFragment = WindowFragment()
        val curtainFragment = CurtainFragment()

        makeCurrentFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_light -> makeCurrentFragment(lightFragment)
                R.id.ic_air_conditioner -> makeCurrentFragment(airconFragment)
                R.id.ic_window -> makeCurrentFragment(windowFragment)
                R.id.ic_curtains -> makeCurrentFragment(curtainFragment)

            }
            true
        }


    }

    private fun makeCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}