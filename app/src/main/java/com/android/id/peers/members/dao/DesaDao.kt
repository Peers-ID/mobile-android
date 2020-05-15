package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.id.peers.members.models.Desa

@Dao
interface DesaDao {
    @Query("SELECT * FROM desa")
    fun getAll(): LiveData<List<Desa>>

    @Query("SELECT * FROM desa where id_kecamatan = :idKecamatan ORDER BY nama ASC")
    fun getByKecamatanId(idKecamatan: Int): List<Desa>

    @Query("DELETE FROM desa")
    suspend fun deleteAll()

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg desa: Desa)

    @Delete
    suspend fun delete(province: Desa)
}