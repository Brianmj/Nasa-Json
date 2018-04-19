package com.example.brianmj.nasajson

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import org.joda.time.Instant
import org.joda.time.LocalDate

class SelectDateFragment : Fragment(){

    lateinit var datePicker: DatePicker
    lateinit var buttonInitiate: Button
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_select_date_layout, container, false)

        datePicker = layout?.findViewById(R.id.datepick_select) as DatePicker
        datePicker.maxDate = Instant.now().millis

        buttonInitiate = layout?.findViewById(R.id.btn_initiate_view) as Button

        buttonInitiate.setOnClickListener {
            val month = datePicker.month + 1   // DatePicker uses 0 index for month. Here we just add 1 for it dislay correctly

            val date = LocalDate(datePicker.year, month, datePicker.dayOfMonth)
            val newIntent = Intent(activity, DisplayPictureActivity::class.java)
            newIntent.apply { putExtra(DisplayPictureActivity.EXTRA_INTENT_DATE, date) }
            startActivity(newIntent)
        }



        return layout
    }

    companion object {
        fun newInstance(): SelectDateFragment {
            return SelectDateFragment()
        }
    }
}