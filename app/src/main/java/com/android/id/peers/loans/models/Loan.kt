package com.android.id.peers.loans.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Loan (
    var aoId : Int = 0,
    var memberId : Int = 0,
    var memberName : String = "",
    var noHp : String = "",
    var formulaId : Int = 0,
    var totalDisbursed : Long = 0,
    var cicilanPerBulan : Long = 0,
    var numberOfLoan : Long = 0,
    var tenor : Int = 0,
    var serviceFee : Long = 0,
    var otherFees : ArrayList<Pair<String, Long>>
) : Parcelable