package com.android.id.peers.util

import java.text.NumberFormat
import java.util.*

class CurrencyFormat {
    companion object {
        val localeId = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeId)
    }
}