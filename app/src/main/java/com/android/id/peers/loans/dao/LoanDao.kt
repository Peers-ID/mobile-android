package com.android.id.peers.loans.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.loans.models.Loan

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans")
    fun getAll(): LiveData<List<Loan>>

    @Query("DELETE FROM loans")
    fun deleteAll()

    @Insert
    fun insertAll(vararg loans: Loan)

    @Delete
    fun delete(loan: Loan)
}