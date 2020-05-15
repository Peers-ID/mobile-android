package com.android.id.peers.loans.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "loans")
data class Loan constructor (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "ao_id") var aoId : Int = 0,
    @ColumnInfo(name = "member_id") var memberId : Int = 0,
    @Ignore var memberName : String = "",
    @ColumnInfo(name = "member_handphone") var noHp : String = "",
    @ColumnInfo(name = "formula_id") var formulaId : Int = 0,
    @ColumnInfo(name = "total_disbursed") var totalDisbursed : Long = 0,
    @ColumnInfo(name = "cicilan_per_bln") var cicilanPerBulan : Long = 0,
    @ColumnInfo(name = "jumlah_loan") var numberOfLoan : Long = 0,
    @ColumnInfo(name = "tenor") var tenor : Int = 0,
    @Ignore var serviceFee : Long = 0,
    @Ignore var otherFees : ArrayList<Pair<String, Long>>
) : Parcelable {
    constructor() : this(0, 0, 0, "", "", 0, 0, 0, 0, 0, 0, ArrayList<Pair<String, Long>>())

}