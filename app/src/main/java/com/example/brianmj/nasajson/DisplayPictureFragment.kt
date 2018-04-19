package com.example.brianmj.nasajson

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.joda.time.LocalDate
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DisplayPictureFragment : Fragment() {

    private val NASA_URL_KEY = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date="

    lateinit var textView_title: TextView
    lateinit var textView_explanation: TextView
    lateinit var imageView_url_display: ImageView

    var selectedDate: LocalDate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedDate = it.getSerializable(ARG_PARAM1) as LocalDate
        }

        arguments?.let {  }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_picture_display_layout, container, false)

        textView_title = layout.findViewById<TextView>(R.id.textView_title) as TextView
        textView_explanation = layout.findViewById<TextView>(R.id.textView_explanation)
        imageView_url_display = layout.findViewById<ImageView>(R.id.imageView_url_display)

        // perform the look up
        selectedDate?.let {
            val dateStr = "${it.year}-${it.monthOfYear}-${it.dayOfMonth}"
            val LOOK_UP_STR = NASA_URL_KEY + dateStr

            launch(UI) {
                textView_title.text = getString(R.string.downloading_data)
                val res = async {
                    val text = retrieveHTTPString(LOOK_UP_STR)
                    var theResult: Result2 = Result2("", "", null)
                    text.let {
                        val res1 = readNasaJson(it)
                        res1?.let {
                            val bitmap = decodeBitmap(it.url)
                            theResult = Result2(it.title, it.explanation, bitmap)
                        }
                    }
                    theResult
                }

                val result = res.await()
                updateUI(result)
            }
        }

        return layout
    }

    fun updateUI(r: Result2) {
        textView_explanation.text = r.explanation
        textView_title.text = selectedDate?.toString() + " " + r.title
        imageView_url_display.setImageBitmap(r.bitmap)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DisplayPictureFragment.
         */
        @JvmStatic
        fun newInstance(param1: LocalDate) =
                DisplayPictureFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_PARAM1, param1)
                    }
                }
    }
}
