package com.android.id.peers.util.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.id.peers.loans.dao.CollectionDao
import com.android.id.peers.loans.dao.LoanDao
import com.android.id.peers.loans.dao.LoanPictureDao
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanPicture
import com.android.id.peers.loans.models.RepaymentCollection
import com.android.id.peers.members.dao.*
import com.android.id.peers.members.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Member::class, Loan::class, LoanPicture::class, Province::class, Kabupaten::class, Kecamatan::class, Desa::class, RepaymentCollection::class], version = 1)
abstract class OfflineDatabase : RoomDatabase() {
    abstract fun memberDao() : MemberDao
    abstract fun loanDao() : LoanDao
    abstract fun loanPictureDao(): LoanPictureDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun kabupatenDao(): KabupatenDao
    abstract fun kecamatanDao(): KecamatanDao
    abstract fun desaDao(): DesaDao
    abstract fun collectionDao(): CollectionDao

    private class OfflineDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OfflineDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): OfflineDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OfflineDatabase::class.java,
                    "offline_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}