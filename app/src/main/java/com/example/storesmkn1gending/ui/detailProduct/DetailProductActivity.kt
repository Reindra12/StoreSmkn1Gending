package com.example.storesmkn1gending.ui.detailProduct

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storesmkn1gending.databinding.ActivityDetailProductBinding
import com.example.storesmkn1gending.model.ResponseProductItem
import com.example.storesmkn1gending.network.ImagesAsynctask
import com.example.storesmkn1gending.utils.SharedPrefs
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var viewModel: DetailProductViewModel
    lateinit var product: ResponseProductItem
    private lateinit var sharedPrefs: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefs = SharedPrefs(this)
        val bundle = intent.extras
        if (bundle != null) {
            product = bundle.getSerializable("product") as ResponseProductItem
        }
        val token = sharedPrefs.accessToken
        viewModel = ViewModelProvider(this).get(DetailProductViewModel::class.java)
        getData()
        addToMyCart(token.toString(), product.id)

    }

    private fun addToMyCart(accessToken: String, id: Int) {
        binding.addtomycartBtn.setOnClickListener {
            if (accessToken != null) {
                viewModel.fetchAddMyCart(accessToken, id)

                Log.d("TAG", "token: "+accessToken)
            }
        }
    }

    private fun getData() {
        binding.productTv.text = product.name
        val imageLoaderTask = ImagesAsynctask(binding.detailImageview)
        imageLoaderTask.execute(product.imageUrl)
        binding.discTv.text = product.description
        binding.locationTv.text = product.location
    }


}