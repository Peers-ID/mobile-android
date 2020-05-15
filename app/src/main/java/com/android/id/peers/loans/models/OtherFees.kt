package com.android.id.peers.loans.models

import android.content.SharedPreferences
import com.google.gson.Gson

class OtherFees {
    var id: Int = 0
    var formulaId: Int = 0
    var serviceName: String = ""
    var serviceType: String = ""
    var serviceAmount: Long = 0
    var serviceCycle: String = ""

    companion object {
        fun saveOtherFees(configPreferences: SharedPreferences, result: List<OtherFees>) {
            val json = Gson().toJson(result)
            configPreferences.edit().putString("fees", json).apply()
        }
    }
}