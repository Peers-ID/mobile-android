package com.android.id.peers.members.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.android.id.peers.members.models.Member

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAll(): LiveData<List<Member>>

    @Query("DELETE FROM members")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg members: Member)

    @Delete
    suspend fun delete(member: Member)
}