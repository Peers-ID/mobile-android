package com.android.id.peers.members.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "desa"/*, foreignKeys = [ForeignKey(entity = Kabupaten::class, parentColumns = ["id"], childColumns = ["id_kecamatan"], onDelete = ForeignKey.CASCADE)]*/)
data class Desa (
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "kecamatan_id") var kecamatanId: String = "",
    @ColumnInfo(name = "name") var name: String = ""
) {
    override fun toString(): String {
        return name
    }
}