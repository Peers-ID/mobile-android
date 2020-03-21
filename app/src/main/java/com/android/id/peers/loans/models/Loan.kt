package com.android.id.peers.loans.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Loan (
    var noHp : String = "",
    var numberOfLoan : Long = 0,
    var tenor : Long = 0,
    var serviceFee : Long = 0,
    var otherFees : ArrayList<Pair<String, Long>>
) : Parcelable