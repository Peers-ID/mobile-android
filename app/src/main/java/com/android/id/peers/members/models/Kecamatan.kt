package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "kecamatan"/*, foreignKeys = [ForeignKey(entity = Kabupaten::class, parentColumns = ["id"], childColumns = ["id_kabupaten"], onDelete = ForeignKey.CASCADE)]*/)
data class Kecamatan (
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "id_kabupaten") var idKabupaten: Int = 0,
    @ColumnInfo(name = "nama") var nama: String = ""

) {
    override fun toString(): String {
        return nama
    }
}