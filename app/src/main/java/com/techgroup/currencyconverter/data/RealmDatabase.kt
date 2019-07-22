package com.techgroup.currencyconverter.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm


class RealmDatabase {

    private var realmDbInstance = Realm.getDefaultInstance()

    fun createRealmObjectFromHashMap(jsonString: String) {
        var rates: ConversionRates?
        val mapObj: HashMap<String, Double> = Gson().fromJson(
            jsonString, object : TypeToken<HashMap<String, Double>>() {}.type
        )
        for ((key, value) in mapObj) {
            rates = ConversionRates(key, value)
            realmDbInstance.executeTransaction { realm ->
                realm.insertOrUpdate(rates)
            }
        }
    }

    fun getConversion(): List<ConversionRates> {
        // Query our RealmDatabase using our ConversationRate Class as a Key
        // Store the Data retrieved into an ArrayList
        val realmData = ArrayList<ConversionRates>()
        val results = realmDbInstance.where(ConversionRates::class.java).findAll()
        results?.forEach { conversion ->
            realmData.add(conversion)
        }
        return realmData
    }

    fun clear() {
        if (realmDbInstance != null) {
            realmDbInstance.close()
            // close access to our Database to Save Resources
        }
    }
}