package com.example.storesmkn1gending

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.storesmkn1gending.databinding.ActivityRegisterBinding
import com.example.storesmkn1gending.network.RegisterAsyncTask
import com.example.storesmkn1gending.network.RegisterAsyncTask2
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            val fullname = binding.nameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()
            val phone = binding.phoneEdt.text.toString().trim()
            val address = binding.addressEdt.text.toString().trim()

            if (CheckingData(fullname, email, password, phone, address)) {
                registerAsyncTask(this).execute(fullname, email, password, phone, address)

//                RegisterAsyncTask2(this).execute(fullname, email, password)
            }
        }
    }

    private class registerAsyncTask(private val context: Context) :
        AsyncTask<String, Void, String>() {
        private val restApi = "https://market-final-project.herokuapp.com/auth/register"

        override fun doInBackground(vararg params: String?): String {
            val name = params[0]
            val email = params[1]
            val password = params[2]
            val phoneNumber = params[3]
            val address = params[4]


            val request = JSONObject()
            try {
                request.put("full_name", name)
                request.put("email", email)
                request.put("password", password)
                request.put("phone_number", phoneNumber)
                request.put("address", address)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            var response = ""
            try {
                val url = URL(restApi)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val write = OutputStreamWriter(conn.outputStream)
                write.write(request.toString())
                write.flush()

                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response += line

                }

                write.close()
                reader.close()
                conn.disconnect()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            try {
                Log.d("TAG", "onPostExecute: " + result)
                val response = JSONObject(result)
                val email = response.getString("email")
                goToLogin(email)
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d("TAG", "onPostExecute: " + result)
            }
        }

        private fun goToLogin(email: String) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("email", email)
            context.startActivity(intent)
            (context as Activity).finish()
        }


    }


    private fun CheckingData(
        fullname: String, email: String, password: String, phone: String, address: String
    ): Boolean {
        resetAllError()
        if (fullname.isEmpty()) {
            setFullNameError("Nama tidak boleh kosong")
            return false
        }
        if (email.isEmpty()) {
            setEmailError("Email tidak boleh kosong")
            return false
        }
        if (password.isEmpty()) {
            setPasswordError("Password tidak boleh kosong")
            return false
        }
        if (phone.isEmpty()) {
            setPhoneError("phone tidak boleh kosong")
            return false
        }
        if (address.isEmpty()) {
            setAddressError("Alamat tidak boleh kosong")
            return false
        }
        return true
    }

    private fun resetAllError() {
        setFullNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setPhoneError(null)
        setAddressError(null)
    }

    private fun setAddressError(message: String?) {
        binding.addressInput.error = message
    }

    private fun setPhoneError(message: String?) {
        binding.phoneInput.error = message
    }

    private fun setPasswordError(message: String?) {
        binding.passwordInput.error = message
    }

    private fun setEmailError(message: String?) {
        binding.emailInput.error = message
    }

    private fun setFullNameError(message: String?) {
        binding.nameInput.error = message

    }
}