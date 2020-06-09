package com.android.id.peers.loans.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "collections")
data class Collection (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "koperasi_id") var koperasiId : Int = 0,
    @ColumnInfo(name = "member_id") var memberId : Int = 0,
    @ColumnInfo(name = "ao_id") var aoId : Int = 0,
    @ColumnInfo(name = "loan_id") var loanId : Int = 0,
    @ColumnInfo(name = "cicilan_ke") var cicilanKe : Int = 0,
    @ColumnInfo(name = "cicilan_jumlah") var cicilanJumlah : Long = 0,
    @ColumnInfo(name = "pokok") var pokok : Long = 0,
    @ColumnInfo(name = "sukarela") var sukarela : Long = 0
) {
}