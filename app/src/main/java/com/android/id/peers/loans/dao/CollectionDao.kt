package com.android.id.peers.loans.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.loans.models.RepaymentCollection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections")
    fun getAll(): LiveData<List<RepaymentCollection>>

    @Query("DELETE FROM collections")
    fun deleteAll()

    @Insert
    fun insertAll(vararg loans: RepaymentCollection)

    @Delete
    fun delete(loan: RepaymentCollection)
}