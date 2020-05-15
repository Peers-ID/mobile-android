package com.android.id.peers.loans.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.loans.models.LoanPicture

@Dao
interface LoanPictureDao {
    @Query("SELECT * FROM loan_pictures")
    fun getAll(): LiveData<List<LoanPicture>>

    @Query("DELETE FROM loan_pictures")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg loanPictures: LoanPicture)

    @Delete
    suspend fun delete(member: LoanPicture)
}