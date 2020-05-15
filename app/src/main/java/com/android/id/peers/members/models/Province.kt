package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class Province (
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "nama") var nama: String = ""
) {
    override fun toString(): String {
        return nama
    }
}