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

    @Query("SELECT * FROM kabupaten WHERE master_code = :masterCode ORDER BY nama ASC")
    fun getByProvinceId(masterCode: String): List<Kabupaten>

    @Query("DELETE FROM kabupaten")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg kabupaten: Kabupaten)

    @Delete
    suspend fun delete(province: Kabupaten)
}