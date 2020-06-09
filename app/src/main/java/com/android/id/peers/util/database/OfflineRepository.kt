package com.android.id.peers.util.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanPicture
import com.android.id.peers.loans.models.Collection
import com.android.id.peers.members.models.*

class OfflineRepository(private val offlineDatabase: OfflineDatabase) {

    val allMembers: LiveData<List<Member>> = offlineDatabase.memberDao().getAll()

    suspend fun insertMember(member: Member) {
        offlineDatabase.memberDao().insertAll(member)
    }

    val allLoans: LiveData<List<Loan>> = offlineDatabase.loanDao().getAll()

    suspend fun insertLoan(loan: Loan) {
        offlineDatabase.loanDao().insertAll(loan)
    }

    val allCollections: LiveData<List<Collection>> = offlineDatabase.collectionDao().getAll()

    suspend fun insertCollection(collection: Collection) {
        offlineDatabase.collectionDao().insertAll(collection)
    }

    val allLoanPictures: LiveData<List<LoanPicture>> = offlineDatabase.loanPictureDao().getAll()

    suspend fun insertLoanPicture(loanPicture: LoanPicture) {
        offlineDatabase.loanPictureDao().insertAll(loanPicture)
    }

    val allProvince: LiveData<List<Province>> = offlineDatabase.provinceDao().getAll()

    suspend fun insertProvince(provinces: List<Province>) {
        offlineDatabase.provinceDao().insertAll(*provinces.toTypedArray())
    }

    val allKabupaten: LiveData<List<Kabupaten>> = offlineDatabase.kabupatenDao().getAll()

    fun getKabupatenByProvinceId(idProvince: String): List<Kabupaten> {
        return offlineDatabase.kabupatenDao().getByProvinceId(idProvince)
    }

    suspend fun insertKabupaten(kabupatens: List<Kabupaten>) {
        offlineDatabase.kabupatenDao().insertAll(*kabupatens.toTypedArray())
    }

    val allKecamatan: LiveData<List<Kecamatan>> = offlineDatabase.kecamatanDao().getAll()

    fun getKecamatanByKabupatenId(idKabupaten: String): List<Kecamatan> {
        return offlineDatabase.kecamatanDao().getByKabupatenId(idKabupaten)
    }

    suspend fun insertKecamatan(kecamatans: List<Kecamatan>) {
        offlineDatabase.kecamatanDao().insertAll(*kecamatans.toTypedArray())
    }

    val allDesa: LiveData<List<Desa>> = offlineDatabase.desaDao().getAll()

    fun getDesaByKecamatanId(idKecamatan: String): List<Desa> {
        return offlineDatabase.desaDao().getByKecamatanId(idKecamatan)
    }

    suspend fun insertDesa(desas: List<Desa>) {
        offlineDatabase.desaDao().insertAll(*desas.toTypedArray())
    }
}