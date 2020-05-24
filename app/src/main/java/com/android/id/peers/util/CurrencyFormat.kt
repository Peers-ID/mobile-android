package com.android.id.peers.util

import android.text.TextUtils
import androidx.annotation.NonNull
import java.text.NumberFormat
import java.util.*

class CurrencyFormat {
    companion object {
        val localeId = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeId)

        fun formatText(s: String): String {
            val chr : CharSequence = s
            val str = chr.replace(Regex("[Rp,.]"), "")
            var formatted = ""
            if(str.isNotEmpty())
                formatted = formatRupiah.format(str.toLong())
            return formatted
        }

        fun removeCurrencyFormat(s: String): String {
            return s.replace(Regex("[Rp,.]"), "")
        }

        fun formatAmount(@NonNull amount: String): String {

            val result = removeNonNumeric(amount)
            val amt = if (!TextUtils.isEmpty(result) && TextUtils.isDigitsOnly(result)) result.toLong() else 0
            val formatter = NumberFormat.getNumberInstance()
            return "Rp".plus(formatter.format(amt))
        }

        private fun removeNonNumeric(@NonNull numberString: String) : String {
            var numbers : String = ""
            for (i in numberString){
                if (i.isDigit())
                    numbers += i
            }
            return numbers
        }


        fun getNewCursorPosition(digitCountToRightOfCursor : Int, numberString : String) : Int{
            var position = 0
            var c = digitCountToRightOfCursor
            for (i in numberString.reversed()) {
                if (c == 0)
                    break

                if (i.isDigit())
                    c --
                position ++


            }
            return numberString.length - position
        }

        fun getNumberOfDigits(@NonNull text : String) : Int{
            var count = 0
            for (i in text)
                if (i.isDigit())
                    count++
            return count
        }
    }
}