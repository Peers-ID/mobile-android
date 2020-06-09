package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kecamatan"/*, foreignKeys = [ForeignKey(entity = Kabupaten::class, parentColumns = ["id"], childColumns = ["id_kabupaten"], onDelete = ForeignKey.CASCADE)]*/)
data class Kecamatan (
    @PrimaryKey var id: String = "",
//    @ColumnInfo(name = "kode_wilayah") var kodeWilayah: String = "",
    @ColumnInfo(name = "kabupaten_id") var kabupatenId: String = "",
    @ColumnInfo(name = "name") var name: String = ""

) {
    override fun toString(): String {
        return name
    }
}