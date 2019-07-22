package com.techgroup.currencyconverter.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//private val moshi = Moshi.Builder().
//    add(KotlinJsonAdapterFactory()) // the KotlinJsonAdapterFactory
//    // enables us to
//    // effectively work with Moshi annotations and kotlin
//    .build()

private const val BASE_URL = "http://data.fixer.io/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    // CoroutineCallAdapterFactory enables us to use Coroutine with Retrofit
    .baseUrl(BASE_URL)
    .build()

object FixerApi {
    val retrofitService: FixerApiService by lazy {
        retrofit.create(FixerApiService::class.java)
    }
}