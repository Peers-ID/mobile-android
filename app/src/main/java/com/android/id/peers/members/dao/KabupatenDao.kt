package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.members.models.Kabupaten

@Dao
interface KabupatenDao {
    @Query("SELECT * FROM kabupaten")
    fun getAll(): LiveData<List<Kabupaten>>

    @Query("SELECT * FROM kabupaten WHERE id_provinsi = :idProvince ORDER BY nama ASC")
    fun getByProvinceId(idProvince: Int): List<Kabupaten>

    @Query("DELETE FROM kabupaten")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kabupaten: Kabupaten)

    @Delete
    suspend fun delete(province: Kabupaten)
}