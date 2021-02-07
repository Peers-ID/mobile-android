package com.android.id.peers.loans_unused.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "loan_pictures")
data class LoanPicture constructor (
    @PrimaryKey (autoGenerate = true) var id: Int,
    @ColumnInfo (name = "member_id") var memberId: Int,
    @ColumnInfo (name = "path") var path: String
)