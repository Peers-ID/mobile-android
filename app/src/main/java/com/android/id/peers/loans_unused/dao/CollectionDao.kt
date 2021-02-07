package com.android.id.peers.loans_unused.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.loans_unused.models.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections")
    fun getAll(): LiveData<List<Collection>>

    @Query("DELETE FROM collections")
    fun deleteAll()

    @Insert
    fun insertAll(vararg loans: Collection)

    @Delete
    fun delete(loan: Collection)
}