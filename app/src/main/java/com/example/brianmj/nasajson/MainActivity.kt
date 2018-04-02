package com.example.brianmj.nasajson

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    val NASA_JSON_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2014-06-29"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(UI){
            textView_title.text = "Downloading data..."
            val res = async{
                val text = retrieveHTTPString(NASA_JSON_URL)
                var theResult: Result2 = Result2("", "", null)
                text?.let {
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

    fun updateUI(r: Result2) {
        textView_explanation.text = r.explanation
        textView_title.text = r.title
        imageView_url_display.setImageBitmap(r.bitmap)
    }
}
