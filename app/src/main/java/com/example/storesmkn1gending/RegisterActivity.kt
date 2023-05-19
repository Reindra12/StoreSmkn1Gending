package com.example.storesmkn1gending

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.storesmkn1gending.databinding.ActivityRegisterBinding
import com.example.storesmkn1gending.network.RegisterAsyncTask

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding


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
                RegisterAsyncTask(this).execute(fullname, email, password, phone, address)
                if (fullname.equals("roby")){
                    Toast.makeText(this, "benar roby", Toast.LENGTH_SHORT).show()
                }
            }
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
            setEmailError("Password tidak boleh kosong")
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