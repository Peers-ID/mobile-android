package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kecamatan"/*, foreignKeys = [ForeignKey(entity = Kabupaten::class, parentColumns = ["id"], childColumns = ["id_kabupaten"], onDelete = ForeignKey.CASCADE)]*/)
data class Kecamatan (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "kode_wilayah") var kodeWilayah: String = "",
    @ColumnInfo(name = "master_code") var masterCode: String = "",
    @ColumnInfo(name = "nama") var nama: String = ""

) {
    override fun toString(): String {
        return nama
    }
}