package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.id.peers.members.models.Desa
import com.android.id.peers.members.models.Kecamatan

@Dao
interface DesaDao {
    @Query("SELECT * FROM desa")
    fun getAll(): LiveData<List<Desa>>

    @Query("SELECT * FROM desa where kecamatan_id = :kecamatanId ORDER BY name ASC")
    fun getByKecamatanId(kecamatanId: String): List<Desa>

    @Query("SELECT * FROM desa WHERE name = :name AND kecamatan_id = :kecamatanId")
    fun getDesaByNameAndKecamatanId(name: String, kecamatanId: String): Desa

    @Query("DELETE FROM desa")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg desa: Desa)

    @Delete
    suspend fun delete(province: Desa)
}