package com.ezfix.ezfixaplication.configuration

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

object HttpRequest {
    var constants = Constants();
    fun requerir(): Backend{
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
        val gson = GsonBuilder().setLenient().create();

        val retrofit = Retrofit.Builder()
            .baseUrl(constants.IP_BACKEND)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

        val api = retrofit.create(Backend::class.java);

        return api;
    }

}