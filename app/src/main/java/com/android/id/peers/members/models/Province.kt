package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class Province (
    @PrimaryKey var id: String = "",
//    @ColumnInfo(name = "kode_wilayah") var kodeWilayah: String = "",
    @ColumnInfo(name = "name") var name: String = ""
) {
    override fun toString(): String {
        return name
    }
}