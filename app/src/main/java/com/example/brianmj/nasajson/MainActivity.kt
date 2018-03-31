package com.example.brianmj.nasajson

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    val NASA_JSON_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2014-06-29"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(AndroidUI) {
            textView_title.text = "Downloading data..."
        }

        val ma = MyAsync().apply { execute() }


    }

    inner class MyAsync : AsyncTask<Void, Void, Result2>() {
        override fun doInBackground(vararg params: Void?): Result2 {


            val text = retrieveHTTPString(NASA_JSON_URL)
            val result = readNasaJson(text!!)
            val bmp = decodeBitmap(result.url)
            return Result2(result.title, result.explanation, bmp)
        }

        override fun onPostExecute(result: Result2?) {
            super.onPostExecute(result)
            result?.let { updateUI(it) }
        }
    }

    fun updateUI(r: Result2) {
        textView_explanation.text = r.explanation
        textView_title.text = r.title
        imageView_url_display.setImageBitmap(r.bm)
    }
}
