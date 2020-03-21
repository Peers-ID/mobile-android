package com.android.id.peers.members.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member (
    /* personal */
    var jenisIdentitas: Int = -1,
    var noIdentitas : String = "",
    var namaLengkap : String = "",
    var noHp : String = "",
    var tanggalLahir : String = "",
    var tempatLahir : String = "",
    var jenisKelamin: Int = -1,
    var namaGadisIbuKandung : String = "",
    var statusPernikahan : Int = -1,
    var pendidikanTerakhir : Int = -1,

    /*address*/
    var jalanSesuaiKtp : String = "",
    var nomorSesuaiKtp : String = "",
    var rtSesuaiKtp : String = "",
    var rwSesuaiKtp : String = "",
    var kelurahanSesuaiKtp : String = "",
    var kecamatanSesuaiKtp : String = "",
    var kotaSesuaiKtp : String = "",
    var statusTempatTinggalSesuaiKtp : Int = -1,
    var lamaBulanTinggalSesuaiKtp : Int = -1,
    var lamaTahunTinggalSesuaiKtp : Int = -1,
    var domisiliSesuaiKtp : Boolean = false,
    var jalanDomisili : String = "",
    var nomorDomisili : String = "",
    var rtDomisili : String = "",
    var rwDomisili : String = "",
    var kelurahanDomisili : String = "",
    var kecamatanDomisili : String = "",
    var kotaDomisili : String = "",
    var statusTempatTinggalDomisili : Int = -1,
    var lamaBulanTinggalDomisili : Int = -1,
    var lamaTahunTinggalDomisili : Int = -1,

    /*occupation*/
    var memilikiNpwp : Int = -1,
    var nomorNpwp : String = "",
    var pekerjaan : Int = -1,
    var bidangPekerjaan : String = "",
    var posisiPekerjaan : String = "",
    var namaPerusahaan : String = "",
    var lamaBulanBekerja : Int = -1,
    var lamaTahunBekerja : Int = -1,
    var penghasilan : String = "",
    var jalanKantor : String = "",
    var nomorKantor : String = "",
    var rtKantor : String = "",
    var rwKantor : String = "",
    var kelurahanKantor : String = "",
    var kecamatanKantor : String = "",
    var kotaKantor : String = "",
    var statusTempatTinggalKantor : Int = -1,
    var lamaBulanTinggalKantor : Int = -1,
    var lamaTahunTinggalKantor : Int = -1,

    /*emergency contact*/
    var namaEmergency : String = "",
    var noHpEmergency : String = "",
    var hubunganEmergeny : Int = -1

) : Parcelable