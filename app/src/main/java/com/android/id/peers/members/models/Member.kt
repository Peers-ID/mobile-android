package com.android.id.peers.members.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "members")
data class Member constructor (
    /* personal */
    @PrimaryKey var id: Int = 0,

    @ColumnInfo(name = "jenis_identitas") var jenisIdentitas: Int = -1,
    @ColumnInfo(name = "no_identitas") var noIdentitas : String = "",
    @ColumnInfo(name = "nama_lengkap") var namaLengkap : String = "",
    @ColumnInfo(name = "member_handphone") var noHp : String = "",
    @ColumnInfo(name = "tanggal_lahir") var tanggalLahir : String = "",
    @ColumnInfo(name = "tempat_lahir") var tempatLahir : String = "",
    @ColumnInfo(name = "jenis_kelamin") var jenisKelamin: Int = -1,
    @ColumnInfo(name = "nama_gadis_ibu") var namaGadisIbuKandung : String = "",
    @ColumnInfo(name = "status_perkawinan") var statusPerkawinan : Int = -1,
    @ColumnInfo(name = "pendidikan_terakhir") var pendidikanTerakhir : Int = -1,

    /*address*/
    @ColumnInfo(name = "alamat_ktp_jalan") var jalanSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_nomer") var nomorSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_rt") var rtSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_rw") var rwSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kelurahan") var kelurahanSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kecamatan_posisi") var kecamatanSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_kecamatan") var kecamatanSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kota_posisi") var kotaSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_kota") var kotaSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_provinsi_posisi") var provinsiSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_provinsi") var provinsiSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_status_tempat_tinggal") var statusTempatTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "alamat_ktp_lama_tinggal_bulan") var lamaBulanTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "alamat_ktp_lama_tinggal_tahun") var lamaTahunTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "domisili_sesuai_ktp") var domisiliSesuaiKtp : Boolean = false,
    @ColumnInfo(name = "alamat_domisili_jalan") var jalanDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_nomer") var nomorDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_rt") var rtDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_rw") var rwDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_kelurahan") var kelurahanDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_kecamatan_posisi") var kecamatanDomisiliPosisi : Int = -1,
    @ColumnInfo(name = "alamat_domisili_kecamatan") var kecamatanDomisili: String = "",
    @ColumnInfo(name = "alamat_domisili_kota_posisi") var kotaDomisiliPosisi: Int = -1,
    @ColumnInfo(name = "alamat_domisili_kota") var kotaDomisili: String = "",
    @ColumnInfo(name = "alamat_domisili_provinsi_posisi") var provinsiDomisiliPosisi : Int = -1,
    @ColumnInfo(name = "alamat_domisili_provinsi") var provinsiDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_status_tempat_tinggal") var statusTempatTinggalDomisili : Int = -1,
    @ColumnInfo(name = "alamat_domisili_lama_bulan_tinggal") var lamaBulanTinggalDomisili : Int = -1,
    @ColumnInfo(name = "alamat_domisili_lama_tahun_tinggal") var lamaTahunTinggalDomisili : Int = -1,

    /*occupation*/
    @ColumnInfo(name = "memiliki_npwp") var memilikiNpwp : Int = -1,
    @ColumnInfo(name = "nomer_npwp") var nomorNpwp : String = "",
    @ColumnInfo(name = "pekerja_usaha") var pekerjaan : Int = -1,
    @ColumnInfo(name = "bidang_pekerja") var bidangPekerjaan : String = "",
    @ColumnInfo(name = "posisi_jabatan") var posisiPekerjaan : String = "",
    @ColumnInfo(name = "nama_perusahaan") var namaPerusahaan : String = "",
    @ColumnInfo(name = "lama_bulan_bekerja") var lamaBulanBekerja : Int = -1,
    @ColumnInfo(name = "lama_tahun_bekerja") var lamaTahunBekerja : Int = -1,
    @ColumnInfo(name = "penghasilan_omset") var penghasilan: Long = 0,
    @ColumnInfo(name = "alamat_kantor_jalan") var jalanKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_nomer") var nomorKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_rt") var rtKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_rw") var rwKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_kelurahan") var kelurahanKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_kecamatan_posisi") var kecamatanKantorPosisi : Int = -1,
    @ColumnInfo(name = "alamat_kantor_kecamatan") var kecamatanKantor: String = "",
    @ColumnInfo(name = "alamat_kantor_kota_posisi") var kotaKantorPosisi: Int = -1,
    @ColumnInfo(name = "alamat_kantor_kota") var kotaKantor: String = "",
    @ColumnInfo(name = "alamat_kantor_provinsi_posisi") var provinsiKantorPosisi : Int = -1,
    @ColumnInfo(name = "alamat_kantor_provinsi") var provinsiKantor : String = "",

    /*emergency contact*/
    @ColumnInfo(name = "nama") var namaEmergency : String = "",
    @ColumnInfo(name = "no_hp") var noHpEmergency : String = "",
    @ColumnInfo(name = "hubungan") var hubunganEmergency : Int = -1

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