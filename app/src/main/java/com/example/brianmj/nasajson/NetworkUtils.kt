package com.example.brianmj.nasajson

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.Charset

val NETOWRK_UTILS_TAG = "NETWORK_UTILS_TAG"
val EXPLANATION_KEY = "explanation"
val TITLE_KEY = "title"
val URL_KEY = "url"

fun retrieveHTTPString(urlString: String): String  {
    val httpURLConnection = openConnectionToURL(urlString)

    // make sure we got a good response code
    (httpURLConnection?.responseCode != HttpURLConnection.HTTP_OK)

    return getHttpText(httpURLConnection!!) ?: ""
}

fun openConnectionToURL(urlString: String): HttpURLConnection? {
    var url: URL? = null
    var httpConnection: HttpURLConnection? = null

    try
    {
        url = URL(urlString)
        httpConnection = url?.openConnection() as HttpURLConnection
        httpConnection?.requestMethod = "GET"
        httpConnection?.connectTimeout = 4000
        httpConnection?.readTimeout = 5000
        httpConnection?.connect()

    }catch(ioException: IOException){
        ioException.printStackTrace()
        httpConnection = null
    }catch (socketException: SocketTimeoutException){
        socketException.printStackTrace()
        httpConnection = null
    }

    return httpConnection
}

fun getHttpText(http: HttpURLConnection): String? {
    val sb = StringBuilder()
    var stream = http.inputStream
    val reader = BufferedReader(InputStreamReader(stream, Charset.forName("utf-8")))

    var text: String? = reader.readLine()

    while(text != null){
        sb.append(text)
        text = reader.readLine()
    }

    reader.close()

    if(sb.isEmpty())
        return null

    return sb.toString()
}

data class Result(val title: String, val explanation: String, val url: String)
data class Result2(val title: String, val explanation: String, val bitmap: Bitmap?)

fun readNasaJson(jsonString: String): Result?{

    if(jsonString.isEmpty())
        return null
    var title = String()
    var explanation = String()
    var url = String()

    try{
        val baseJsonObject = JSONObject(jsonString)

        title = baseJsonObject.getString(TITLE_KEY)
        explanation = baseJsonObject.getString(EXPLANATION_KEY)
        url = baseJsonObject.getString(URL_KEY)


    }catch (e: JSONException){
        Log.e(NETOWRK_UTILS_TAG, e.localizedMessage)
        throw (e)       // rethrow the exception
    }

    return Result(title, explanation, url)
}

fun decodeBitmap(urlString: String): Bitmap {
    val http = openConnectionToURL(urlString)
    val inputStream = http?.inputStream
    val bm = BitmapFactory.decodeStream(inputStream)

    inputStream?.close()
    http?.disconnect()
    return bm
}