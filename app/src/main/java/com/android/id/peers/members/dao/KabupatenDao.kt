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

    @Query("SELECT * FROM kabupaten WHERE province_id = :provinceId ORDER BY name ASC")
    fun getByProvinceId(provinceId: String): List<Kabupaten>

    @Query("SELECT * FROM kabupaten WHERE name = :name AND province_id = :provinceId")
    fun getKabupatenByNameAndProvinceId(name: String, provinceId: String): Kabupaten

    @Query("DELETE FROM kabupaten")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kabupaten: Kabupaten)

    @Delete
    suspend fun delete(province: Kabupaten)
}