package com.example.brianmj.nasajson

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    val NASA_JSON_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2014-06-29"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_select_date_container) ?: run{
            val frag = SelectDateFragment.newInstance()
            fragmentManager.beginTransaction().add(R.id.fragment_select_date_container, frag).commit()
            frag
        }

    }

}
