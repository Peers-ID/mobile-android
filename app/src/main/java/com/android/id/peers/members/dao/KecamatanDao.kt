package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.members.models.Kabupaten
import com.android.id.peers.members.models.Kecamatan

@Dao
interface KecamatanDao {
    @Query("SELECT * FROM kecamatan")
    fun getAll(): LiveData<List<Kecamatan>>

    @Query("SELECT * FROM kecamatan where kabupaten_id = :kabupatenId ORDER BY name ASC")
    fun getByKabupatenId(kabupatenId: String): List<Kecamatan>

    @Query("SELECT * FROM kecamatan WHERE name = :name AND kabupaten_id = :kabupatenId")
    fun getKecamatanByNameAndKabupatenId(name: String, kabupatenId: String): Kecamatan

    @Query("DELETE FROM kecamatan")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kecamatan: Kecamatan)

    @Delete
    suspend fun delete(province: Kecamatan)
}