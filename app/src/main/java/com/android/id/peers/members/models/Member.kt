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
    var statusPerkawinan : Int = -1,
    var pendidikanTerakhir : Int = -1,

    /*address*/
    var jalanSesuaiKtp : String = "",
    var nomorSesuaiKtp : String = "",
    var rtSesuaiKtp : String = "",
    var rwSesuaiKtp : String = "",
    var kelurahanSesuaiKtp : String = "",
    var kecamatanSesuaiKtp : String = "",
    var kotaSesuaiKtp : String = "",
    var provinsiSesuaiKtp : String = "",
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
    var provinsiDomisili : String = "",
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
    var penghasilan: Long = 0,
    var jalanKantor : String = "",
    var nomorKantor : String = "",
    var rtKantor : String = "",
    var rwKantor : String = "",
    var kelurahanKantor : String = "",
    var kecamatanKantor : String = "",
    var kotaKantor : String = "",
    var provinsiKantor : String = "",
    var statusTempatTinggalKantor : Int = -1,

    /*emergency contact*/
    var namaEmergency : String = "",
    var noHpEmergency : String = "",
    var hubunganEmergency : Int = -1

): Parcelable {
    fun memberJenisIdentitasString() : String {
        return when (jenisIdentitas) {
            0 -> "KTP"
            1 -> "SIM"
            2 -> "Paspor"
            else -> ""
        }
    }

    fun setMemberJenisIdentitas(str: String) {
        jenisIdentitas = when (str) {
            "KTP" -> 0
            "SIM" -> 1
            "Paspor" -> 2
            else -> -1
        }
    }

    fun jenisKelaminString() : String {
        return when (jenisKelamin) {
            0 -> "Pria"
            1 -> "Wanita"
            else -> ""
        }
    }

    fun setMemberJenisKelamin(str: String) {
        jenisKelamin = when (str) {
            "Pria" -> 0
            "Wanita" -> 1
            else -> -1
        }
    }

    fun statusPerkawinanString() : String {
        return when (statusPerkawinan) {
            0 -> "Kawin"
            1 -> "Belum Kawin"
            2 -> "Cerai"
            else -> ""
        }
    }

    fun setMemberStatusPerkawinan(str: String) {
        statusPerkawinan = when (str) {
            "Kawin" -> 0
            "Belum Kawin" -> 1
            "Cerai" -> 2
            else -> -1
        }
    }

    fun pendidikanTerakhirString() : String {
        return when (pendidikanTerakhir) {
            0 ->"SD"
            1 ->"SMP"
            2 -> "SMA"
            3 -> "D3"
            4 -> "S1"
            5 -> "Lainnya"
            else -> ""
        }
    }

    fun setMemberPendidikanTerakhir(str: String) {
        pendidikanTerakhir = when (str) {
            "SD" -> 0
            "SMP" -> 1
            "SMA" -> 2
            "D3" -> 3
            "S1" -> 4
            "Lainnya" -> 5
            else -> -1
        }
    }

    fun statusTempatTinggalSesuaiKtpString() : String {
        return when (statusTempatTinggalSesuaiKtp) {
            0 -> "Milik Sendiri"
            1 -> "Milik Keluarga"
            2 -> "Rumah Dinas"
            3 -> "Sewa/Kontrak"
            4 -> "Kost"
            else -> ""
        }
    }

    fun setStatusTempatTinggalSesuaiKtp(str: String) {
        statusTempatTinggalSesuaiKtp = when (str) {
            "Milik Sendiri" -> 0
            "Milik Keluarga" -> 1
            "Rumah Dinas" -> 2
            "Sewa/Kontrak" -> 3
            "Kost" -> 4
            else -> -1
        }
    }

    fun statusTempatTinggalDomisiliString() : String {
        return when (statusTempatTinggalDomisili) {
            0 -> "Milik Sendiri"
            1 -> "Milik Keluarga"
            2 -> "Rumah Dinas"
            3 -> "Sewa/Kontrak"
            4 -> "Kost"
            else -> ""
        }
    }

    fun setStatusTempatTinggalDomisili(str: String) {
        statusTempatTinggalDomisili = when (str) {
            "Milik Sendiri" -> 0
            "Milik Keluarga" -> 1
            "Rumah Dinas" -> 2
            "Sewa/Kontrak" -> 3
            "Kost" -> 4
            else -> -1
        }
    }

    fun memilikiNpwpString() : String {
        return when (memilikiNpwp) {
            0 -> "Ada"
            1 -> "Tidak Ada"
            else -> ""
        }
    }

    fun pekerjaanString() : String {
        return when (pekerjaan) {
            0 -> "Pelajar/Mahasiswa"
            1 -> "Pegawai Swasta"
            2 -> "Pegawai Negeri"
            3 -> "Guru/Dosen"
            4 -> "TNI/POLRI"
            5 -> "Pensiunan"
            6 -> "Wiraswasta"
            7 -> "Profesional Lainnya"
            8 -> "Ibu Rumah Tangga"
            else -> ""
        }
    }

    fun setPekerjaan(str: String) {
        pekerjaan = when (str) {
            "Pelajar/Mahasiswa" -> 0
            "Pegawai Swasta" -> 1
            "Pegawai Negeri" -> 2
            "Guru/Dosen" -> 3
            "TNI/POLRI" -> 4
            "Pensiunan" -> 5
            "Wiraswasta" -> 6
            "Profesional Lainnya" -> 7
            "Ibu Rumah Tangga" -> 8
            else -> -1
        }
    }

    fun hubunganEmergencyString() : String {
        return when (hubunganEmergency) {
            0 -> "Suami/Istri"
            1 -> "Saudara Kandung"
            2 -> "Anak"
            3 -> "Orang Tua"
            4 -> "Lainnya"
            else -> ""
        }
    }

    fun setHubunganEmergency(str: String) {
        hubunganEmergency = when (str) {
            "Suami/Istri" -> 0
            "Saudara Kandung" -> 1
            "Anak" -> 2
            "Orang Tua" -> 3
            "Lainnya" -> 4
            else -> -1
        }
    }
}