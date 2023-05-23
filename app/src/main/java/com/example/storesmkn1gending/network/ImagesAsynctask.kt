package com.example.storesmkn1gending.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import org.json.JSONException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImagesAsynctask(private val images: ImageView) : AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg params: String?): Bitmap? {
        val imageUrl = params[0]

        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            images.setImageBitmap(result)
        }
    }
}