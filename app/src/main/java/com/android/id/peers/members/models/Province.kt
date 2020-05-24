package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class Province (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "kode_wilayah") var kodeWilayah: String = "",
    @ColumnInfo(name = "nama") var nama: String = ""
) {
    override fun toString(): String {
        return nama
    }
}