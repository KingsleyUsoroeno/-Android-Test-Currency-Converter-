package com.techgroup.currencyconverter.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApiService {
    @GET("latest")
    fun getConversionRatesAsync(@Query("access_key") apiKey: String):
            Deferred<ResponseBody>
}