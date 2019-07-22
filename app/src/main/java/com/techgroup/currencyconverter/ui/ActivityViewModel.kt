package com.techgroup.currencyconverter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techgroup.currencyconverter.data.ConversionRates
import com.techgroup.currencyconverter.data.RealmDatabase
import com.techgroup.currencyconverter.network.FixerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ActivityViewModel : ViewModel() {

    /**
     * Create a Job for our Coroutine.
     */
    private var viewModelJob = Job()

    private val API_KEY: String = "d15f7165cb573b87f755068793f0488d"

    private var coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var realmDatabase: RealmDatabase = RealmDatabase()

    private val _ratesMutableLiveData = MutableLiveData<List<ConversionRates>>()

    // getter
    val ratesMutableLiveData: LiveData<List<ConversionRates>>
        get() = _ratesMutableLiveData

    init {
        getCurrencyConversionRates()
        getConversionRate()

    }

    private fun getCurrencyConversionRates() {
        coroutineScope.launch {
            // launches a coroutineScope that will run on the Main Thread
            val result = FixerApi.retrofitService.getConversionRatesAsync(API_KEY)
            try {
                parseJson(result.await().string())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun parseJson(responseBody: String) {
        try {
            val responseObject = JSONObject(responseBody)
            val rates = responseObject.getJSONObject("rates")
            val ratesToString = rates.toString()
            realmDatabase.createRealmObjectFromHashMap(ratesToString)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getConversionRate() {
        val list = realmDatabase.getConversion()
        _ratesMutableLiveData.value = list
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        realmDatabase.clear()
    }
}