package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kabupaten"/*, foreignKeys = [ForeignKey(entity = Province::class, parentColumns = ["id"], childColumns = ["id_provinsi"], onDelete = ForeignKey.CASCADE)]*/)
data class Kabupaten(
    @PrimaryKey var id: String = "",
//    @ColumnInfo(name = "kode_wilayah") var kodeWilayah: String = "",
    @ColumnInfo(name = "province_id") var provinceId: String = "",
    @ColumnInfo(name = "name") var name: String = ""
) {
    override fun toString(): String {
        return name
    }
}