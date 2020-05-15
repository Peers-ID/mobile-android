package com.android.id.peers.util.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanPicture
import com.android.id.peers.loans.models.RepaymentCollection
import com.android.id.peers.members.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OfflineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: OfflineRepository = OfflineRepository(OfflineDatabase.getDatabase(application, viewModelScope))
    val allMembers: LiveData<List<Member>>
    val allLoans: LiveData<List<Loan>>
    val allLoanPictures: LiveData<List<LoanPicture>>
    val allProvince: LiveData<List<Province>>
    val allKabupaten: LiveData<List<Kabupaten>>
    val allKecamatan: LiveData<List<Kecamatan>>
    val allDesa: LiveData<List<Desa>>
    val allCollections: LiveData<List<RepaymentCollection>>

    init {
        allMembers = repository.allMembers
        allLoans = repository.allLoans
        allLoanPictures = repository.allLoanPictures
        allProvince = repository.allProvince
        allKabupaten = repository.allKabupaten
        allKecamatan = repository.allKecamatan
        allDesa = repository.allDesa
        allCollections = repository.allCollections
    }

    fun insertMember(member: Member) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMember(member)
    }

    fun insertLoan(loan: Loan) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertLoan(loan)
    }

    fun insertLoanPictures(loanPicture: LoanPicture) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertLoanPicture(loanPicture)
    }

    fun insertCollection(collection: RepaymentCollection) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCollection(collection)
    }

    fun insertProvince(provinces: List<Province>) = viewModelScope.async(Dispatchers.IO) {
        repository.insertProvince(provinces)
    }

    suspend fun getKabupatenByProvinceId(idProvince: Int): List<Kabupaten> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.getKabupatenByProvinceId(idProvince)
        }.await()
    }

    fun insertKabupaten(kabupatens: List<Kabupaten>) = viewModelScope.async(Dispatchers.IO) {
        repository.insertKabupaten(kabupatens)
    }

    suspend fun getKecamatanByKabupatenId(idKabupaten: Int): List<Kecamatan> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.getKecamatanByKabupatenId(idKabupaten)
        }.await()
    }

    fun insertKecamatan(kecamatans: List<Kecamatan>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertKecamatan(kecamatans)
    }

    suspend fun getDesaByKecamatanId(idKecamatan: Int): List<Desa> {
        return viewModelScope.async(Dispatchers.IO) {
            repository.getDesaByKecamatanId(idKecamatan)
        }.await()
    }

    fun insertDesa(desa: Desa) = viewModelScope.launch(Dispatchers.Main) {
        repository.insertDesa(desa)
    }
}