package com.example.storesmkn1gending.ui.home

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storesmkn1gending.model.ResponseProductItem
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HomeViewModel : ViewModel() {
    private val _productList = MutableLiveData<List<ResponseProductItem>>()
    val productList: LiveData<List<ResponseProductItem>> get() = _productList

    fun fetchProductList(accessToken: String) {
        val asyncTask = FetchProductAsyncTask(accessToken)
        asyncTask.execute()
    }

    inner class FetchProductAsyncTask(private val accessToken: String) :
        AsyncTask<String, Void, String>() {
        private val apiUrl = "https://market-final-project.herokuapp.com/seller/product"
        private val acceptHeader = "accept: body"
        private val accessTokenHeader = "access_token"


        override fun doInBackground(vararg params: String?): String? {
            var response: String? = null
            var connection: HttpURLConnection? = null

            try {
                val url = URL(apiUrl)
                connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.setRequestProperty(
                    acceptHeader.split(":")[0],
                    acceptHeader.split(":")[1].trim()
                )
//                val tok = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImpvaG5kb2VAbWFpbC5jb20iLCJpYXQiOjE2NTQ5MjcxODZ9.fghFryd8OPEHztZlrN50PtZj0EC7NWFVj2iPPN9xi1M"
                connection.setRequestProperty(accessTokenHeader, accessToken)

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val responseBuilder = StringBuilder()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        responseBuilder.append(line)
                    }

                    response = responseBuilder.toString()
                    Log.d("TAG", "doInBackground: " + response)
                } else {
                    Log.e(
                        "ProductAsyncTask",
                        "Failed to retrieve product list. Response code: $responseCode"
                    )
                }
            } catch (e: Exception) {
                Log.e("ProductAsyncTask", "Error occurred while retrieving product list", e)
            } finally {
                connection?.disconnect()
            }

            return response

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                try {
                    val jsonArray = JSONArray(result)
                    val productList = mutableListOf<ResponseProductItem>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val product = parseProduct(jsonObject)
                        productList.add(product)
                        Log.d("TAG", "onPostExecute: " + product)
                    }
                    _productList.value = productList
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }

        private fun parseProduct(jsonObject: JSONObject?): ResponseProductItem {
            val id = jsonObject!!.getInt("id")
            val name = jsonObject.getString("name")
            val description = jsonObject.getString("description")
            val price = jsonObject.getInt("base_price")
            val imageUrl = jsonObject.getString("image_url")
            val imageName = jsonObject.getString("image_name")
            val location = jsonObject.getString("location")
            val userId = jsonObject.getInt("user_id")
            val status = jsonObject.getString("status")
            val createdAt = jsonObject.getString("createdAt")
            val updatedAt = jsonObject.getString("updatedAt")



            return ResponseProductItem(
                imageName,
                createdAt,
                userId,
                imageUrl,
                name,
                price,
                description,
                location,
                id,
                status,
                updatedAt
            )
        }
    }
}