package com.treecio.hexplore.network

import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.IOException

class NetworkClient {

    companion object {

        val JSON = MediaType.parse("application/json; charset=utf-8")

        val BASE_ENDPOINT = "https://hex18.herokuapp.com/api/v1"
        val ENDPOINT_PROFILES = BASE_ENDPOINT + "/profiles"
        val ENDPOINT_ADDUSER = BASE_ENDPOINT + "/addUser"

    }

    var client = OkHttpClient()
    val gson = Gson()

    @Throws(IOException::class)
    fun <T> get(url: String, clazz: Class<T>, handler: (T) -> Unit) {
        Timber.d("GET: $url")
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response) {
                val responseString = response.body()?.string()
                Timber.i("Response: " + responseString)
                val obj = gson.fromJson(responseString, clazz)
                handler.invoke(obj)
            }

        })
    }

    @Throws(IOException::class)
    fun post(url: String, json: String) {
        Timber.d("POST: $url")
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                Timber.i("Response: " + response?.body()?.string())
            }

        })
    }



}
