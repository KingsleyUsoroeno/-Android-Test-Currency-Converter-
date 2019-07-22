package com.techgroup.currencyconverter.data

import io.realm.RealmObject

open class ConversionRates(
    var country: String? = null,
    var rate: Double? = null

) : RealmObject()



