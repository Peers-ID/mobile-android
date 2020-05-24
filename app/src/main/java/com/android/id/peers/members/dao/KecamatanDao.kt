package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.members.models.Kecamatan

@Dao
interface KecamatanDao {
    @Query("SELECT * FROM kecamatan")
    fun getAll(): LiveData<List<Kecamatan>>

    @Query("SELECT * FROM kecamatan where master_code = :masterCode ORDER BY nama ASC")
    fun getByKabupatenId(masterCode: String): List<Kecamatan>

    @Query("DELETE FROM kecamatan")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kecamatan: Kecamatan)

    @Delete
    suspend fun delete(province: Kecamatan)
}