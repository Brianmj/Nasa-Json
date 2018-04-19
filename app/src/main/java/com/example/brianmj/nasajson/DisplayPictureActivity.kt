package com.example.brianmj.nasajson

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import org.joda.time.LocalDate

class DisplayPictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_picture)

        val dateIntent = intent
        val date = dateIntent.getSerializableExtra(EXTRA_INTENT_DATE) as LocalDate


        val fragmentManager: FragmentManager = supportFragmentManager

        // find the container that hosts the fragment
        var fragmentContainer = fragmentManager.findFragmentById(R.id.fragment_picture_display_container) ?: run{
            val fragment = DisplayPictureFragment.newInstance(date)
            fragmentManager.beginTransaction().add(R.id.fragment_picture_display_container, fragment).commit()
            fragment
        }
    }

    companion object {
        val EXTRA_INTENT_DATE = "EXTRA_INTENT_DATE_KEY"
    }
}
