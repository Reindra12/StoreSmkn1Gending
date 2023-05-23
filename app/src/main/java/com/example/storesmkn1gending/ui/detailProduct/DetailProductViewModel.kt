package com.example.storesmkn1gending.ui.detailProduct

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class DetailProductViewModel : ViewModel() {

    fun fetchAddMyCart(accessToken: String, idProduct: Int) {
        val asynctask = FetchAddMyCartTask(accessToken, idProduct)
        asynctask.execute()
    }

    class FetchAddMyCartTask(private val accessToken: String, private val productId: Int) :
        AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val url = URL("https://market-final-project.herokuapp.com/buyer/wishlist")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("accept", "body")
            connection.setRequestProperty("access_token", accessToken)
            connection.setRequestProperty("Content-Type", "multipart/form-data")
            connection.doOutput = true

            val requestBody = "product_id=60643"
            val outputStream = connection.outputStream
            outputStream.write(requestBody.toByteArray())
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                return response.toString()
            } else {
                Log.d("TAG", "doInBackground: "+responseCode)
                throw IOException("Failed to perform POST request. Response Code: $responseCode")
            }

    }

    override fun onPostExecute(result: String) {
        Log.d("TAG", "onPostExecute: wishlish" + result)
    }

}
// TODO: Implement the ViewModel
}