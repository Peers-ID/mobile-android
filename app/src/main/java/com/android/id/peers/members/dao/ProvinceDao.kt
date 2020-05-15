package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.members.models.Province

@Dao
interface ProvinceDao {
    @Query("SELECT * FROM provinces ORDER BY nama ASC")
    fun getAll(): LiveData<List<Province>>

    @Query("DELETE FROM provinces")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg province: Province)

    @Delete
    suspend fun delete(province: Province)
}