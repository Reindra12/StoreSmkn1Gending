package com.example.storesmkn1gending.network

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.example.storesmkn1gending.MainActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class RegisterAsyncTask(private val context: Context) : AsyncTask<String, Void, String>() {

    private val REGISTER_URL =
        "https://market-final-project.herokuapp.com/auth/register" // Ganti dengan URL registrasi yang sesuai

    private lateinit var progressDialog: ProgressDialog

    override fun onPreExecute() {
        // Menampilkan progress dialog sebelum permintaan registrasi dimulai
//            progressDialog = ProgressDialog(this@RegisterAsyncTask)
//            progressDialog.setMessage("Registering...")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
    }

    override fun doInBackground(vararg params: String): String {
        val fullName = params[0]
        val email = params[1]
        val password = params[2]
        val phoneNumber = params[3]
        val address = params[4]
//        val city = params[5]

        // Membuat objek JSON yang berisi data registrasi
        val postData = JSONObject()
        try {
            postData.put("full_name", fullName)
            postData.put("email", email)
            postData.put("password", password)
            postData.put("phone_number", phoneNumber)
            postData.put("address", address)
//            postData.put("city", city)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Melakukan permintaan HTTP POST ke server untuk registrasi
        var response = ""
        try {
            val url = URL(REGISTER_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(postData.toString())
            writer.flush()

            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response += line
            }

            writer.close()
            reader.close()
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    override fun onPostExecute(result: String) {
        // Menutup progress dialog dan melakukan penanganan respon JSON setelah registrasi selesai
//        progressDialog.dismiss()

        try {
            val jsonResponse = JSONObject(result)
            val id = jsonResponse.getInt("id")
            val fullName = jsonResponse.getString("full_name")
            val email = jsonResponse.getString("email")
            val phoneNumber = jsonResponse.getString("phone_number")
            val password = jsonResponse.getString("password")

            navigateToMainActivity(email, password)
            Log.d("TAG", "onPostExecute: " + "berhasil" + jsonResponse)

            // Lakukan penanganan sesuai dengan data yang diterima dari server
            // Misalnya, menampilkan pesan registrasi berhasil atau berpindah ke aktivitas selanjutnya.
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d("TAG", "onPostExecute: " + result)
        }
    }

    private fun navigateToMainActivity(email: String, password: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        context.startActivity(intent)
        (context as Activity).finish()
    }


}
