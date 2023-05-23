package com.example.storesmkn1gending.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.example.storesmkn1gending.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class RegisterAsyncTask2(private val context: Context) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String? {
        val name = params[0]
        val email = params[1]
        val password = params[2]

        try {
            val url = URL("http://127.0.0.1:5000/Api/Users")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val data = JSONObject()
            data.put("name", name)
            data.put("email", email)
            data.put("password", password)


            val outputStream = connection.outputStream
            outputStream.write(data.toString().toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use(BufferedReader::readText)
                inputStream.close()
                return response
            } else {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            try {
                val jsonResponse = JSONObject(result)
                val fullName = jsonResponse.getString("name")
                val email = jsonResponse.getString("email")
                val password = jsonResponse.getString("password")

                navigateToMainActivity(email, password)
                Log.d("TAG", "onPostExecute: " + "berhasil" + jsonResponse)
                navigateToMainActivity(email, password)

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("TAG", "onPostExecute: " + result)
            }

        } else {

        }
    }

    private fun navigateToMainActivity(email: String, password: String) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        context.startActivity(intent)
        (context as Activity).finish()
    }
}