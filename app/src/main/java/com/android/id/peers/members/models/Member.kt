package com.android.id.peers.members.models

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.id.peers.pinjaman.pengajuan.Pinjaman
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.postPinjaman
import com.android.id.peers.simpanan.Simpanan.Companion.getSimpanan
import com.android.id.peers.util.callback.*
import com.android.id.peers.util.connection.ApiConnections.Companion.API_HOSTNAME
import com.android.id.peers.util.connection.FileDataPart
import com.android.id.peers.util.connection.VolleyFileUploadRequest
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Parcelize
@Entity(tableName = "members")
data class Member constructor (
    /* personal */
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

//    @ColumnInfo(name = "jenis_identitas") var jenisIdentitas: Int = -1,
    @ColumnInfo(name = "member_id") var memberId : Int = 0,
    @ColumnInfo(name = "no_identitas") var noIdentitas : String = "",
    @ColumnInfo(name = "member_handphone") var noHp : String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "nama_lengkap") var namaLengkap : String = "",
    @ColumnInfo(name = "tanggal_lahir") var tanggalLahir : String = "",
    @ColumnInfo(name = "usia") var usia : String = "",
    @ColumnInfo(name = "tempat_lahir") var tempatLahir : String = "",
    @ColumnInfo(name = "jenis_kelamin") var jenisKelamin: Int = -1,
    @ColumnInfo(name = "status_perkawinan") var statusPerkawinan : Int = -1,
    @ColumnInfo(name = "pendidikan_terakhir") var pendidikanTerakhir : Int = -1,
    @ColumnInfo(name = "nama_gadis_ibu") var namaGadisIbuKandung : String = "",

    /*address*/
    @ColumnInfo(name = "alamat_ktp_jalan") var jalanSesuaiKtp : String = "",
//    @ColumnInfo(name = "alamat_ktp_nomer") var nomorSesuaiKtp : String = "",
//    @ColumnInfo(name = "alamat_ktp_rt") var rtSesuaiKtp : String = "",
//    @ColumnInfo(name = "alamat_ktp_rw") var rwSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kelurahan_posisi") var kelurahanSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_kelurahan") var kelurahanSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kecamatan_posisi") var kecamatanSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_kecamatan") var kecamatanSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kota_posisi") var kotaSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_kota") var kotaSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_provinsi_posisi") var provinsiSesuaiKtpPosisi : Int = -1,
    @ColumnInfo(name = "alamat_ktp_provinsi") var provinsiSesuaiKtp : String = "",
    @ColumnInfo(name = "alamat_ktp_kode_pos") var kodePosSesuaiKtp: Int = 0,
    @ColumnInfo(name = "alamat_ktp_status_tempat_tinggal") var statusTempatTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "alamat_ktp_lama_tinggal_bulan") var lamaBulanTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "alamat_ktp_lama_tinggal_tahun") var lamaTahunTinggalSesuaiKtp : Int = -1,
    @ColumnInfo(name = "domisili_sesuai_ktp") var domisiliSesuaiKtp : Boolean = false,
    @ColumnInfo(name = "alamat_domisili_jalan") var jalanDomisili : String = "",
//    @ColumnInfo(name = "alamat_domisili_nomer") var nomorDomisili : String = "",
//    @ColumnInfo(name = "alamat_domisili_rt") var rtDomisili : String = "",
//    @ColumnInfo(name = "alamat_domisili_rw") var rwDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_kelurahan_posisi") var kelurahanDomisiliPosisi : Int = -1,
    @ColumnInfo(name = "alamat_domisili_kelurahan") var kelurahanDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_kecamatan_posisi") var kecamatanDomisiliPosisi : Int = -1,
    @ColumnInfo(name = "alamat_domisili_kecamatan") var kecamatanDomisili: String = "",
    @ColumnInfo(name = "alamat_domisili_kota_posisi") var kotaDomisiliPosisi: Int = -1,
    @ColumnInfo(name = "alamat_domisili_kota") var kotaDomisili: String = "",
    @ColumnInfo(name = "alamat_domisili_provinsi_posisi") var provinsiDomisiliPosisi : Int = -1,
    @ColumnInfo(name = "alamat_domisili_provinsi") var provinsiDomisili : String = "",
    @ColumnInfo(name = "alamat_domisili_kode_pos") var kodePosDomisili: Int = 0,
    @ColumnInfo(name = "alamat_domisili_status_tempat_tinggal") var statusTempatTinggalDomisili : Int = -1,
    @ColumnInfo(name = "alamat_domisili_lama_bulan_tinggal") var lamaBulanTinggalDomisili : Int = -1,
    @ColumnInfo(name = "alamat_domisili_lama_tahun_tinggal") var lamaTahunTinggalDomisili : Int = -1,

    /*occupation*/
    @ColumnInfo(name = "memiliki_npwp") var memilikiNpwp : Int = -1,
    @ColumnInfo(name = "nomer_npwp") var nomorNpwp : String = "",
    @ColumnInfo(name = "pekerja_usaha") var pekerjaan : Int = -1,
//    @ColumnInfo(name = "bidang_pekerja") var bidangPekerjaan : String = "",
//    @ColumnInfo(name = "posisi_jabatan") var posisiPekerjaan : String = "",
    @ColumnInfo(name = "jenis_umkm") var jenisUmkm: String = "",
    @ColumnInfo(name = "nama_perusahaan") var namaPerusahaan : String = "",
    @ColumnInfo(name = "lama_bulan_bekerja") var lamaBulanBekerja : Int = -1,
    @ColumnInfo(name = "lama_tahun_bekerja") var lamaTahunBekerja : Int = -1,
    @ColumnInfo(name = "penghasilan_omset") var penghasilan: Long = 0,
    @ColumnInfo(name = "alamat_kantor_jalan") var jalanKantor : String = "",
//    @ColumnInfo(name = "alamat_kantor_nomer") var nomorKantor : String = "",
//    @ColumnInfo(name = "alamat_kantor_rt") var rtKantor : String = "",
//    @ColumnInfo(name = "alamat_kantor_rw") var rwKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_kelurahan_posisi") var kelurahanKantorPosisi : Int = -1,
    @ColumnInfo(name = "alamat_kantor_kelurahan") var kelurahanKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_kecamatan_posisi") var kecamatanKantorPosisi : Int = -1,
    @ColumnInfo(name = "alamat_kantor_kecamatan") var kecamatanKantor: String = "",
    @ColumnInfo(name = "alamat_kantor_kota_posisi") var kotaKantorPosisi: Int = -1,
    @ColumnInfo(name = "alamat_kantor_kota") var kotaKantor: String = "",
    @ColumnInfo(name = "alamat_kantor_provinsi_posisi") var provinsiKantorPosisi : Int = -1,
    @ColumnInfo(name = "alamat_kantor_provinsi") var provinsiKantor : String = "",
    @ColumnInfo(name = "alamat_kantor_kode_pos") var kodePosKantor: Int = 0,

    /*emergency contact*/
    @ColumnInfo(name = "nama") var namaEmergency : String = "",
    @ColumnInfo(name = "no_hp") var noHpEmergency : String = "",
    @ColumnInfo(name = "hubungan") var hubunganEmergency : Int = -1,

    /*pasangan*/
    @ColumnInfo(name = "nama_pasangan") var namaPasangan: String = "",
    @ColumnInfo(name = "no_identitas_pasangan") var noIdentitasPasangan: String = "",
    @ColumnInfo(name = "pekerjaan_pasangan") var pekerjaanPasangan: String = "",
    @ColumnInfo(name = "no_hp_pasangan") var noHpPasangan: String = "",

    /*penjamin*/
    @ColumnInfo(name = "nama_penjamin") var namaPenjamin: String = "",
    @ColumnInfo(name = "no_hp_penjamin") var noHpPenjamin: String = "",
    @ColumnInfo(name = "hubungan_penjamin") var hubunganPenjamin: String = "",

    /*dokumen*/
    @ColumnInfo(name = "dokumen_ktp") var dokumenKtp: String = "",
    @ColumnInfo(name = "dokumen_ktp_byte") var dokumenKtpByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_sim") var dokumenSim: String = "",
    @ColumnInfo(name = "dokumen_sim_byte") var dokumenSimByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_kk") var dokumenKk: String = "",
    @ColumnInfo(name = "dokumen_kk_byte") var dokumenKkByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_keterangan_kerja") var dokumenKetKerja: String = "",
    @ColumnInfo(name = "dokumen_keterangan_kerja_byte") var dokumenKetKerjaByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_slip_gaji") var dokumenSlipGaji: String = "",
    @ColumnInfo(name = "dokumen_slip-gaji_byte") var dokumenSlipGajiByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_akta_nikah") var dokumenAktaNikah: String = "",
    @ColumnInfo(name = "dokumen_akta_nikah_byte") var dokumenAktaNikahByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_bpkb") var dokumenBpkb: String = "",
    @ColumnInfo(name = "dokumen_bpkb_byte") var dokumenBpkbByteArrayString: String = "",
    @ColumnInfo(name = "dokumen_lainnya") var dokumenLainnya: String = "",
    @ColumnInfo(name = "dokumen_lainnya_byte") var dokumenLainnyaByteArrayString: String = "",

    /*survey*/
    @ColumnInfo(name = "survey_luas_rumah") var surveyLuasRumah: Int = -1,
    @ColumnInfo(name = "survey_jenis_atap") var surveyJenisAtap: Int = -1,
    @ColumnInfo(name = "survey_jenis_dinding") var surveyJenisDinding: Int = -1,
    @ColumnInfo(name = "survey_kondisi_rumah") var surveyKondisiRumah: Int = -1,
    @ColumnInfo(name = "survey_letak_rumah") var surveyLetakRumah: Int = -1,
    @ColumnInfo(name = "survey_tanggungan_keluarga") var surveyTanggunganKeluarga: String = "",
    @ColumnInfo(name = "survey_data_fisik_perabot") var surveyDataFisikPerabot: Int = -1,
    @ColumnInfo(name = "survey_akses_lembaga_keuangan") var surveyAksesLembagaKeuangan: Int = -1,
    @ColumnInfo(name = "survey_info_ttg_usaha") var surveyInfoTtgUsaha: Int = -1,
    @ColumnInfo(name = "survey_index_rumah") var surveyIndexRumah: Int = -1,
    @ColumnInfo(name = "survey_index_asset") var surveyIndexAsset: Int = -1,
    @ColumnInfo(name = "survey_kepemilikan_asset") var surveyKepemilikanAsset: Int = -1,
    @ColumnInfo(name = "survey_pendapatan_luar_usaha") var surveyPendapatanLuarUsaha: Int = -1,
    @ColumnInfo(name = "survey_perkembangan_asset") var surveyPerkembanganAsset: Int = -1,
    @ColumnInfo(name = "survey_perkembangan_usaha") var surveyPerkembanganUsaha: Int = -1,

    /*verified*/
    @ColumnInfo(name = "is_verified") var isVerified : Boolean = false

): Parcelable {
//    fun memberJenisIdentitasString() : String {
//        return when (jenisIdentitas) {
//            0 -> "KTP"
//            1 -> "SIM"
//            2 -> "Paspor"
//            else -> ""
//        }
//    }

//    fun setMemberJenisIdentitas(str: String) {
//        jenisIdentitas = when (str) {
//            "KTP" -> 0
//            "SIM" -> 1
//            "Paspor" -> 2
//            else -> -1
//        }
//    }

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
            4 -> "Pensiunan"
            5 -> "Wiraswasta"
            6 -> "UMKM"
            7 -> "Pekerja Lepas/Harian"
            8 -> "Ibu Rumah Tangga"
            9 -> "Tidak Bekerja"
            10 -> "Lainnya"
            else -> ""
        }
    }

    fun setPekerjaan(str: String) {
        pekerjaan = when (str) {
            "Pelajar/Mahasiswa" -> 0
            "Pegawai Swasta" -> 1
            "Pegawai Negeri" -> 2
            "Guru/Dosen" -> 3
            "Pensiunan" -> 4
            "Wiraswasta" -> 5
            "UMKM" -> 6
            "Pekerja Lepas/Harian" -> 7
            "Ibu Rumah Tangga" -> 8
            "Tidak Bekerja" -> 9
            "Lainnya" -> 10
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

    fun luasRumah(): String {
        return when(surveyLuasRumah) {
            0 -> "Besar Sekali"
            1 -> "Sedang"
            2 -> "Kecil"
            else -> ""
        }
    }

    fun setLuasRumah(str: String) {
        surveyLuasRumah = when(str) {
            "Besar Sekali" -> 0
            "Sedang" -> 1
            "Kecil" -> 2
            else -> -1
        }
    }

    fun jenisAtap(): String {
        return when(surveyJenisAtap) {
            0 -> "Genteng"
            1 -> "Seng Karat"
            2 -> "Rumbia"
            else -> ""
        }
    }

    fun setJenisAtap(str: String) {
        surveyJenisAtap = when(str) {
            "Genteng" -> 0
            "Seng Karat" -> 1
            "Rumbia" -> 2
            else -> -1
        }
    }

    fun jenisDinding(): String {
        return when(surveyJenisDinding) {
            0 -> "Tembok"
            1 -> "Papan"
            2 -> "Bambu"
            else -> ""
        }
    }

    fun setJenisDinding(str: String) {
        surveyJenisDinding = when(str) {
            "Tembok" -> 0
            "Papan" -> 1
            "Bambu" -> 2
            else -> -1
        }
    }

    fun kondisiRumah(): String {
        return when(surveyKondisiRumah) {
            0 -> "Baik Sekali"
            1 -> "Sedang"
            2 -> "Rusak"
            else -> ""
        }
    }

    fun setKondisiRumah(str: String) {
        surveyKondisiRumah = when(str) {
            "Baik Sekali" -> 0
            "Sedang" -> 1
            "Rusak" -> 2
            else -> -1
        }
    }

    fun letakRumah(): String {
        return when(surveyLetakRumah) {
            0 -> "Jalan Aspal"
            1 -> "Jalan Setapak"
            2 -> "Tidak Jelas"
            else -> ""
        }
    }

    fun setLetakRumah(str: String)  {
        surveyLetakRumah = when(str) {
            "Jalan Aspal" -> 0
            "Jalan Setapak" -> 1
            "Tidak Jelas" -> 2
            else -> -1
        }
    }

    fun dataFisikPerabotElektronik(): String {
        return when(surveyDataFisikPerabot) {
            0 -> "Baik"
            1 -> "Kurang Baik"
            2 -> "Tidak ada"
            else -> ""
        }
    }

    fun setDataFisikPerabotElekronik(str: String) {
        surveyDataFisikPerabot = when(str) {
            "Baik" -> 0
            "Kurang Baik" -> 1
            "Tidak Ada" -> 2
            else -> -1
        }
    }

    fun aksesLembagaKeuangan(): String {
        return when(surveyAksesLembagaKeuangan) {
            0 -> "Sering"
            1 -> "Jarang"
            2 -> "Tidak ada"
            else -> ""
        }
    }

    fun setAksesLembagaKeuangan(str: String) {
        surveyAksesLembagaKeuangan = when(str) {
            "Sering" -> 0
            "Jarang" -> 1
            "Tidak ada" -> 2
            else -> -1
        }
    }

    fun infoTtgUsaha(): String {
        return when(surveyInfoTtgUsaha) {
            0 -> "Berjalan"
            1 -> "Sudah berhenti"
            2 -> "Tidak ada usaha"
            else -> ""
        }
    }

    fun setInfoTtgUsaha(str: String) {
        surveyInfoTtgUsaha = when(str) {
            "Berjalan" -> 0
            "Sudah berhenti" -> 1
            "Tidak ada usaha" -> 2
            else -> -1
        }
    }

    fun indeksRumah(): String {
        return when(surveyIndexRumah) {
            0 -> "< 18"
            1 -> "18 s/d 24"
            2 -> "25 s/d 28"
            else -> ""
        }
    }

    fun setIndeksRumah(str: String) {
        surveyIndexRumah = when(str) {
            "< 18" -> 0
            "18 s/d 24" -> 1
            "25 s/d 28" -> 2
            else -> -1
        }
    }

    fun indeksAsset(): String {
        return when(surveyIndexAsset) {
            0 -> "< 15"
            1 -> "15 s/d 18"
            2 -> "18 s/d 20"
            else -> ""
        }
    }

    fun setIndeksAsset(str: String) {
        surveyIndexAsset = when(str) {
            "< 15" -> 0
            "15 s/d 18" -> 1
            "18 s/d 20" -> 2
            else -> -1
        }
    }

    fun kepemilikanAsset(): String {
        return when(surveyKepemilikanAsset) {
            0 -> "Mobil"
            1 -> "Sepeda Motor"
            2 -> "Perabot & TV"
            else -> ""
        }
    }

    fun setKepemilikanAsset(str: String) {
        surveyKepemilikanAsset = when(str) {
            "Mobil" -> 0
            "Sepeda Motor" -> 1
            "Perabot & TV" -> 2
            else -> -1
        }
    }

    fun pendapatanLuarUsaha(): String {
        return when(surveyPendapatanLuarUsaha) {
            0 -> "Meningkat"
            1 -> "Menurun"
            2 -> "Tetap"
            else -> ""
        }
    }

    fun setPendapatanLuarUsaha(str: String) {
        surveyPendapatanLuarUsaha = when(str) {
            "Meningkat" -> 0
            "Menurun" -> 1
            "Tetap" -> 2
            else -> -1
        }
    }

    fun perkembanganAsset(): String {
        return when(surveyPerkembanganAsset) {
            0 -> "Meningkat"
            1 -> "Menurun"
            2 -> "Tetap"
            else -> ""
        }
    }

    fun setPerkembanganAsset(str: String) {
        surveyPerkembanganAsset = when(str) {
            "Meningkat" -> 0
            "Menurun" -> 1
            "Tetap" -> 2
            else -> -1
        }
    }

    fun perkembanganUsaha(): String {
        return when(surveyPerkembanganUsaha) {
            0 -> "Meningkat"
            1 -> "Menurun"
            2 -> "Tetap"
            else -> ""
        }
    }

    fun setPerkembanganUsaha(str: String) {
        surveyPerkembanganUsaha = when(str) {
            "Meningkat" -> 0
            "Menurun" -> 1
            "Tetap" -> 2
            else -> -1
        }
    }

    companion object {
        /**
         * Get Member Config using REST API from the HOST
         * @param[preferences] of SharedPreferences type to pass the saved Bearer token
         * @param[context] of Context type to pass the application context
         * @param[callback] of SplashScreen is a callback class which functions as an interface between the function and the UI calling the functio
         * @return [callback] of SplashScreen, a callback interface containing the member config as a response from API call
         */
        fun getConfig(preferences: SharedPreferences, context: Context, callback: SplashScreen) {
            var url = "${API_HOSTNAME}member_config/"
            val id = preferences.getInt("koperasi_id", 0)
//        val id = 4
            url += id

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getConfig", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val config = MemberAcquisitionConfig(email = 0, usia = 1)
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val firstObj = data.getJSONObject(0)
                            val idKoperasi = firstObj.getInt("koperasi_id")
//                            config.jenisIdentitas = firstObj.getInt("jenis_identitas")
                            config.noIdentitas = firstObj.getInt("no_identitas")
                            config.memberHandphone = firstObj.getInt("member_handphone")
                            config.email = firstObj.getInt("email")
                            config.namaLengkap = firstObj.getInt("nama_lengkap")
                            config.tanggalLahir = firstObj.getInt("tanggal_lahir")
                            config.usia = firstObj.getInt("usia")
                            config.tempatLahir = firstObj.getInt("tempat_lahir")
                            config.jenisKelamin = firstObj.getInt("jenis_kelamin")
                            config.statusPerkawinan = firstObj.getInt("status_perkawinan")
                            config.pendidikanTerakhir = firstObj.getInt("pendidikan_terakhir")
                            config.namaGadisIbu = firstObj.getInt("nama_gadis_ibu")
                            config.alamatKtpJalan = firstObj.getInt("alamat_ktp_jalan")
//                            config.alamatKtpNo = firstObj.getInt("alamat_ktp_nomer")
//                            config.alamatKtpRt = firstObj.getInt("alamat_ktp_rt")
//                            config.alamatKtpRw = firstObj.getInt("alamat_ktp_rw")
                            config.alamatKtpKelurahan = firstObj.getInt("alamat_ktp_kelurahan")
                            config.alamatKtpKecamatan = firstObj.getInt("alamat_ktp_kecamatan")
                            config.alamatKtpKota = firstObj.getInt("alamat_ktp_kota")
                            config.alamatKtpProvinsi = firstObj.getInt("alamat_ktp_provinsi")
                            config.alamatKtpKodePos = firstObj.getInt("alamat_ktp_kode_pos")
                            config.alamatKtpStatusTempatTinggal = firstObj.getInt("alamat_ktp_status_tempat_tinggal")
                            config.alamatKtpLamaTinggal = firstObj.getInt("alamat_ktp_lama_tinggal")
                            config.domisiliSesuaiKtp = firstObj.getInt("domisili_sesuai_ktp")
                            config.alamatDomisiliJalan = firstObj.getInt("alamat_domisili_jalan")
//                            config.alamatDomisiliNo = firstObj.getInt("alamat_domisili_nomer")
//                            config.alamatDomisiliRt = firstObj.getInt("alamat_domisili_rt")
//                            config.alamatDomisiliRw = firstObj.getInt("alamat_domisili_rw")
                            config.alamatDomisiliKelurahan = firstObj.getInt("alamat_domisili_kelurahan")
                            config.alamatDomisiliKecamatan = firstObj.getInt("alamat_domisili_kecamatan")
//                    config.alamatDomisiliKotaProvinsi = firstObj.getInt("alamat_domisili_kota_provinsi")
                            config.alamatDomisiliKota = firstObj.getInt("alamat_domisili_kota")
                            config.alamatDomisiliProvinsi = firstObj.getInt("alamat_domisili_provinsi")
                            config.alamatDomisiliKodePos = firstObj.getInt("alamat_domisili_kode_pos")
                            config.alamatDomisiliStatusTempatTinggal = firstObj.getInt("alamat_domisili_status_tempat_tinggal")
                            config.alamatDomisiliLamaTempatTinggal = firstObj.getInt("alamat_domisili_lama_tempat_tinggal")
                            config.memilikiNpwp = firstObj.getInt("memiliki_npwp")
                            config.nomerNpwp = firstObj.getInt("nomer_npwp")
                            config.pekerjaUsaha = firstObj.getInt("pekerja_usaha")
                            config.jenisUmkm = firstObj.getInt("jenis_umkm")
//                            config.bidangPekerja = firstObj.getInt("bidang_pekerja")
//                            config.posisiJabatan = firstObj.getInt("posisi_jabatan")
                            config.namaPerusahaan = firstObj.getInt("nama_perusahaan")
                            config.lamaBekerja = firstObj.getInt("lama_bekerja")
                            config.penghasilanOmset = firstObj.getInt("penghasilan_omset")
                            config.alamatKantorJalan = firstObj.getInt("alamat_kantor_jalan")
//                            config.alamatKantorNo = firstObj.getInt("alamat_kantor_nomer")
//                            config.alamatKantorRt = firstObj.getInt("alamat_kantor_rt")
//                            config.alamatKantorRw = firstObj.getInt("alamat_kantor_rw")
                            config.alamatKantorKelurahan = firstObj.getInt("alamat_kantor_kelurahan")
                            config.alamatKantorKecamatan = firstObj.getInt("alamat_kantor_kecamatan")
//                    config.alamatKantorKotaProvinsi = firstObj.getInt("alamat_kantor_kota_provinsi")
                            config.alamatKantorKota = firstObj.getInt("alamat_kantor_kota")
                            config.alamatKantorProvinsi = firstObj.getInt("alamat_kantor_provinsi")
                            config.alamatKantorKodePos = firstObj.getInt("alamat_kantor_kode_pos")

                            config.namaEmergency = firstObj.getInt("nama_kontak_darurat")
                            config.noHpEmergency = firstObj.getInt("nomor_ponsel_darurat")
                            config.hubungan = firstObj.getInt("hubungan_kontak_darurat")

                            config.namaPasangan = firstObj.getInt("nama_pasangan")
                            config.noIdentitasPasangan = firstObj.getInt("no_identitas_pasangan")
                            config.pekerjaanPasangan = firstObj.getInt("pekerjaan_pasangan")
                            config.noHpPasangan = firstObj.getInt("no_hp_pasangan")

                            config.namaPenjamin = firstObj.getInt("nama_penjamin")
                            config.noHpPenjamin = firstObj.getInt("no_hp_penjamin")
                            config.hubunganPenjamin = firstObj.getInt("hubungan_penjamin")

                            config.dokumenKtp = firstObj.getInt("dokumen_ktp")
                            config.dokumenSim = firstObj.getInt("dokumen_sim")
                            config.dokumenKk = firstObj.getInt("dokumen_kk")
                            config.dokumenKetKerja = firstObj.getInt("dokumen_keterangan_kerja")
                            config.dokumenSlipGaji = firstObj.getInt("dokumen_slip_gaji")
                            config.dokumenAktaNikah = firstObj.getInt("dokumen_akta_nikah")
                            config.dokumenBpkb = firstObj.getInt("dokumen_bpkb")
                            config.dokumenLainnya = firstObj.getInt("dokumen_lainnya")

                            config.surveyLuasRumah = firstObj.getInt("survey_luas_rumah")
                            config.surveyJenisAtap = firstObj.getInt("survey_jenis_atap")
                            config.surveyJenisDinding = firstObj.getInt("survey_jenis_dinding")
                            config.surveyKondisiRumah = firstObj.getInt("survey_kondisi_rumah")
                            config.surveyLetakRumah = firstObj.getInt("survey_letak_rumah")
                            config.surveyTanggunganKeluarga = firstObj.getInt("survey_tanggungan_keluarga")
                            config.surveyDataFisikPerabot = firstObj.getInt("survey_data_fisik_perabot")
                            config.surveyAksesLembagaKeuangan = firstObj.getInt("survey_akses_lembaga_keuangan")
                            config.surveyInfoTtgUsaha = firstObj.getInt("survey_info_ttg_usaha")
                            config.surveyIndexRumah = firstObj.getInt("survey_index_rumah")
                            config.surveyIndexAsset = firstObj.getInt("survey_index_asset")
                            config.surveyKepemilikanAsset = firstObj.getInt("survey_kepemilikan_asset")
                            config.surveyPendapatanLuarUsaha = firstObj.getInt("survey_pendapatan_luar_usaha")
                            config.surveyPerkembanganAsset = firstObj.getInt("survey_perkembangan_asset")
                            config.surveyPerkembanganUsaha = firstObj.getInt("survey_perkembangan_usaha")
                            Log.d("getConfig", "$idKoperasi")
                        }
                        callback.onSuccess(config)
                    }
                },
                Response.ErrorListener { error ->
                    //                Log.e("getConfig", error.toString())
                    val response = error.networkResponse
                    if (error is ServerError && response != null) {
                        try {
                            val res = String(
                                response.data,
                                Charset.forName(
                                    HttpHeaderParser.parseCharset(
                                        response.headers,
                                        "utf-8"
                                    )
                                )
                            )
                            // Now you can use any deserializer to make sense of data
//                        val obj = JSONObject(res);
                            Log.d("getConfig", "RES : $res")
                        } catch (e1: UnsupportedEncodingException) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (e2: JSONException) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }

                    }
                }
            )
            {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    headers["Content-Type"] = "application/json"
                    return headers
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        fun getMemberFromJsonObject(memberJsonObj: JSONObject): Member {
            val member = Member()
            member.memberId = memberJsonObj.getInt("member_id")
            member.noIdentitas = memberJsonObj.getString("no_identitas")
            member.noHp = memberJsonObj.getString("member_handphone")
            member.email = memberJsonObj.getString("email")
            member.namaLengkap = memberJsonObj.getString("nama_lengkap")
            member.tanggalLahir = memberJsonObj.getString("tanggal_lahir")
            member.tempatLahir = memberJsonObj.getString("tempat_lahir")
            member.usia = memberJsonObj.getString("usia")
            Log.d("getMember", "Jenis Kelamin : ${memberJsonObj.getString("jenis_kelamin")}")
            member.setMemberJenisKelamin(memberJsonObj.getString("jenis_kelamin"))
            member.setMemberStatusPerkawinan(memberJsonObj.getString("status_perkawinan"))
            member.setMemberPendidikanTerakhir(memberJsonObj.getString("pendidikan_terakhir"))
            member.namaGadisIbuKandung = memberJsonObj.getString("nama_gadis_ibu")

            member.jalanSesuaiKtp = memberJsonObj.getString("alamat_ktp_jalan")
            Log.d("getMember", member.jalanSesuaiKtp)
//                            member.nomorSesuaiKtp = memberJsonObj.getString("alamat_ktp_nomer")
//                            member.rtSesuaiKtp = memberJsonObj.getString("alamat_ktp_rt")
//                            member.rwSesuaiKtp = memberJsonObj.getString("alamat_ktp_rw")
            member.kelurahanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kelurahan")
            member.kecamatanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kecamatan")
            member.kotaSesuaiKtp = memberJsonObj.getString("alamat_ktp_kota")
            member.provinsiSesuaiKtp = memberJsonObj.getString("alamat_ktp_provinsi")
            member.kodePosSesuaiKtp = memberJsonObj.getInt("alamat_ktp_kode_pos")
            member.setStatusTempatTinggalSesuaiKtp(memberJsonObj.getString("alamat_ktp_status_tempat_tinggal"))
            val lamaTinggalSesuaiKtp = memberJsonObj.getString("alamat_ktp_lama_tinggal").split(" ").toTypedArray()

            Log.d("getMember", "Lama Tinggal KTP : ${memberJsonObj.getString("alamat_ktp_lama_tinggal")}")

            member.lamaTahunTinggalSesuaiKtp = lamaTinggalSesuaiKtp[0].toInt()
            member.lamaBulanTinggalSesuaiKtp = lamaTinggalSesuaiKtp[2].toInt()

            member.jalanDomisili = memberJsonObj.getString("alamat_domisili_jalan")
//                            member.nomorDomisili = memberJsonObj.getString("alamat_domisili_nomer")
//                            member.rtDomisili = memberJsonObj.getString("alamat_domisili_rt")
//                            member.rwDomisili = memberJsonObj.getString("alamat_domisili_rw")
            member.kelurahanDomisili = memberJsonObj.getString("alamat_domisili_kelurahan")
            member.kecamatanDomisili = memberJsonObj.getString("alamat_domisili_kecamatan")
            member.kotaDomisili = memberJsonObj.getString("alamat_domisili_kota")
            member.provinsiDomisili = memberJsonObj.getString("alamat_domisili_provinsi")
            member.kodePosDomisili = memberJsonObj.getInt("alamat_domisili_kode_pos")
            member.setStatusTempatTinggalDomisili(memberJsonObj.getString("alamat_domisili_status_tempat_tinggal"))
            val lamaTinggalDomisili = memberJsonObj.getString("alamat_domisili_lama_tempat_tinggal").split(" ").toTypedArray()
            Log.d("Member", "Lama Tahun Tinggal Domisili : ${lamaTinggalDomisili[0]}")
            Log.d("Member", "Lama Bulan Tinggal Domisili : ${lamaTinggalDomisili[2]}")
            member.lamaTahunTinggalDomisili = lamaTinggalDomisili[0].toInt()
            member.lamaBulanTinggalDomisili = lamaTinggalDomisili[2].toInt()

            member.memilikiNpwp = if(memberJsonObj.getInt("memiliki_npwp") == 0) 1 else 0
            member.nomorNpwp = memberJsonObj.getString("nomer_npwp")
            member.setPekerjaan(memberJsonObj.getString("pekerja_usaha"))
            member.jenisUmkm = memberJsonObj.getString("jenis_umkm")
//                            member.bidangPekerjaan = memberJsonObj.getString("bidang_pekerja")
//                            member.posisiPekerjaan = memberJsonObj.getString("posisi_jabatan")
            member.namaPerusahaan = memberJsonObj.getString("nama_perusahaan")

            val lamaBekerja = memberJsonObj.getString("lama_bekerja").split(" ").toTypedArray()
            member.lamaTahunBekerja = lamaBekerja[0].toInt()
            member.lamaBulanBekerja = lamaBekerja[2].toInt()
            member.penghasilan = memberJsonObj.getLong("penghasilan_omset")
            member.jalanKantor = memberJsonObj.getString("alamat_kantor_jalan")
//                            member.nomorKantor = memberJsonObj.getString("alamat_kantor_nomer")
//                            member.rtKantor = memberJsonObj.getString("alamat_kantor_rt")
//                            member.rwKantor = memberJsonObj.getString("alamat_kantor_rw")
            member.kelurahanKantor = memberJsonObj.getString("alamat_kantor_kelurahan")
            member.kecamatanKantor = memberJsonObj.getString("alamat_kantor_kecamatan")
            member.kotaKantor = memberJsonObj.getString("alamat_kantor_kota")
            member.provinsiKantor = memberJsonObj.getString("alamat_kantor_provinsi")
            member.kodePosKantor = memberJsonObj.getInt("alamat_kantor_kode_pos")

            member.namaEmergency = memberJsonObj.getString("nama_kontak_darurat")
            member.noHpEmergency = memberJsonObj.getString("nomor_ponsel_darurat")
            member.setHubunganEmergency(memberJsonObj.getString("hubungan_kontak_darurat"))

            member.namaPasangan = memberJsonObj.getString("nama_pasangan")
            member.noIdentitasPasangan = memberJsonObj.getString("no_identitas_pasangan")
            member.pekerjaanPasangan = memberJsonObj.getString("pekerjaan_pasangan")
            member.noHpPasangan = memberJsonObj.getString("no_hp_pasangan")

            member.namaPenjamin = memberJsonObj.getString("nama_penjamin")
            member.noHpPenjamin = memberJsonObj.getString("no_hp_penjamin")
            member.hubunganPenjamin = memberJsonObj.getString("hubungan_penjamin")

            member.dokumenKtp = memberJsonObj.getString("dokumen_ktp")
            member.dokumenSim = memberJsonObj.getString("dokumen_sim")
            member.dokumenKk = memberJsonObj.getString("dokumen_kk")
            member.dokumenAktaNikah = memberJsonObj.getString("dokumen_akta_nikah")
            member.dokumenKetKerja = memberJsonObj.getString("dokumen_keterangan_kerja")
            member.dokumenSlipGaji = memberJsonObj.getString("dokumen_slip_gaji")
            member.dokumenBpkb = memberJsonObj.getString("dokumen_bpkb")
            member.dokumenLainnya = memberJsonObj.getString("dokumen_lainnya")

            member.setLuasRumah(memberJsonObj.getString("survey_luas_rumah"))
            member.setJenisAtap(memberJsonObj.getString("survey_jenis_atap"))
            member.setJenisDinding(memberJsonObj.getString("survey_jenis_dinding"))
            member.setKondisiRumah(memberJsonObj.getString("survey_kondisi_rumah"))
            member.setLetakRumah(memberJsonObj.getString("survey_letak_rumah"))
            member.surveyTanggunganKeluarga = memberJsonObj.getString("survey_tanggungan_keluarga")
            member.setDataFisikPerabotElekronik(memberJsonObj.getString("survey_data_fisik_perabot"))
            member.setAksesLembagaKeuangan(memberJsonObj.getString("survey_akses_lembaga_keuangan"))
            member.setInfoTtgUsaha(memberJsonObj.getString("survey_info_ttg_usaha"))
            member.setIndeksRumah(memberJsonObj.getString("survey_index_rumah"))
            member.setIndeksAsset(memberJsonObj.getString("survey_index_asset"))
            member.setKepemilikanAsset(memberJsonObj.getString("survey_kepemilikan_asset"))
            member.setPendapatanLuarUsaha(memberJsonObj.getString("survey_pendapatan_luar_usaha"))
            member.setPerkembanganAsset(memberJsonObj.getString("survey_perkembangan_asset"))
            member.setPerkembanganUsaha(memberJsonObj.getString("survey_perkembangan_usaha"))
            return member
        }

        fun getMember(preferences: SharedPreferences, context: Context, isSimpananList: Boolean,
                      membersCallback: MembersCallback? = null, simpananItemCallback: SimpananItemCallback? = null) {
            val url = "${API_HOSTNAME}member"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status == 200) {
                        val members = ArrayList<Member>()
                        var member: Member?
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            var length = data.length() - 1
                            for (i in 0 until data.length()) {
                                val memberJsonObj = data.getJSONObject(i)
                                member = getMemberFromJsonObject(memberJsonObj)
                                members.add(member)
                                if (isSimpananList) {
                                    length -= 1
                                    Log.d("getMember", "MEMBER ID : ${member.memberId}, MEMBER NAME : ${member.namaLengkap}")
                                    getSimpanan(preferences, context, member.memberId, simpananItemCallback!!, length)
                                }
                            }
                            if (!isSimpananList) membersCallback?.onSuccess(members)
                        }
                    }
                },
                Response.ErrorListener { error ->
                    //                Log.e("getConfig", error.toString())
                    val response = error.networkResponse
                    if (error is ServerError && response != null) {
                        try {
                            val res = String(
                                response.data,
                                Charset.forName(
                                    HttpHeaderParser.parseCharset(
                                        response.headers,
                                        "utf-8"
                                    )
                                )
                            )
                            // Now you can use any deserializer to make sense of data
//                        val obj = JSONObject(res);
                            Log.d("getMember", "RES : $res")
                        } catch (e1: UnsupportedEncodingException) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (e2: JSONException) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }

                    }
                }
            )
            {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    headers["Content-Type"] = "application/json"
                    return headers
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

//        fun getMemberByPhone(preferences: SharedPreferences, context: Context, callback: MemberCallback, memberPhone: String) {
//            val url = "${API_HOSTNAME}member/phone/$memberPhone"
//
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.GET, url, null,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//                    val jsonObj = JSONObject(strResp)
//                    val status = jsonObj.getString("status")
//                    Log.d("getMember", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
//                    if (status.toInt() == 200) {
//                        val member = Member()
//                        val data = jsonObj.getJSONArray("data")
//                        if (data.length() > 0) {
//                            val memberJsonObj = data.getJSONObject(0)
//                            member.id = memberJsonObj.getInt("member_id")
//                            member.noIdentitas = memberJsonObj.getString("no_identitas")
//                            member.namaLengkap = memberJsonObj.getString("nama_lengkap")
//
//                            callback.onSuccess(member)
//                        } else {
//                            callback.onSuccess(Member())
//                        }
//                    }
//                },
//                Response.ErrorListener { error ->
//                    //                Log.e("getConfig", error.toString())
//                    val response = error.networkResponse
//                    if (error is ServerError && response != null) {
//                        try {
//                            val res = String(
//                                response.data,
//                                Charset.forName(
//                                    HttpHeaderParser.parseCharset(
//                                        response.headers,
//                                        "utf-8"
//                                    )
//                                )
//                            )
//                            // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                            Log.d("getMemberByPhone", "RES : $res")
//                        } catch (e1: UnsupportedEncodingException) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (e2: JSONException) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//
//                    }
//                }
//            )
//            {
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    val container = "Bearer " + preferences.getString("token", null)!!
//                    headers["Authorization"] = container
//                    headers["Content-Type"] = "application/json"
//                    return headers
//                }
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//        }

        fun getMemberByNik(preferences: SharedPreferences, context: Context, callback: MemberCallback, nik: String) {
            val url = "${API_HOSTNAME}member/nik/$nik"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    val data = jsonObj.getJSONObject("data")
                    val message = jsonObj.getString("message")
                    Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
//                        val data = jsonObj.getJSONArray("data")
                    val memberNikStatus = MemberNikStatus()
                    memberNikStatus.status = status
                    memberNikStatus.message = message
                    val member = when (status) {
                        203 -> getMemberFromJsonObject(data)
                        else -> null
                    }
                    memberNikStatus.member = member
                    callback.onSuccess(memberNikStatus)
                },
                Response.ErrorListener { error ->
                    //                Log.e("getConfig", error.toString())
                    val response = error.networkResponse
                    if (error is ServerError && response != null) {
                        try {
                            val res = String(
                                response.data,
                                Charset.forName(
                                    HttpHeaderParser.parseCharset(
                                        response.headers,
                                        "utf-8"
                                    )
                                )
                            )
                            // Now you can use any deserializer to make sense of data
//                        val obj = JSONObject(res);
                            Log.d("getMemberByNik", "RES : $res")
                        } catch (e1: UnsupportedEncodingException) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (e2: JSONException) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }

                    }
                }
            )
            {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    headers["Content-Type"] = "application/json"
                    return headers
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

//        fun postMemberAndPicturesAndLoan(member: Member, preferences: SharedPreferences, context: Context, callback: PinjamanResponseCallback, pinjaman: Pinjaman, isExisting: Boolean) {
//            var threshold = 0
//            if (member.dokumenKtp.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenSim.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenKk.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenBpkb.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenAktaNikah.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenSlipGaji.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenKetKerja.isNotEmpty()) {
//                threshold++
//            }
//            if (member.dokumenLainnya.isNotEmpty()) {
//                threshold++
//            }
//
//            var done: Int by Delegates.observable(0) { property, oldValue, newValue ->
//                if (newValue == threshold)
//                    if (isExisting)
//                        putMember(member, preferences, context, callback, pinjaman)
//                    else
//                        postMember(member, preferences, context, callback, pinjaman)
//            }
//
//            if (member.dokumenKtp.isNotEmpty()) {
//                postMemberPicture(member.dokumenKtpByteArrayString.toByteArray(), preferences, context, member.dokumenKtp, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenKtp = result
//                        done++
//                    }
//
//                })
//            }
//            if (member.dokumenSim.isNotEmpty()) {
//                postMemberPicture(member.dokumenSimByteArrayString.toByteArray(), preferences, context, member.dokumenSim, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenSim = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenKk.isNotEmpty()) {
//                postMemberPicture(member.dokumenKkByteArrayString.toByteArray(), preferences, context, member.dokumenKk, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenKk = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenKetKerja.isNotEmpty()) {
//                postMemberPicture(member.dokumenKetKerjaByteArrayString.toByteArray(), preferences, context, member.dokumenKetKerja, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenKetKerja = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenBpkb.isNotEmpty()) {
//                postMemberPicture(member.dokumenBpkbByteArrayString.toByteArray(), preferences, context, member.dokumenBpkb, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenBpkb = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenAktaNikah.isNotEmpty()) {
//                postMemberPicture(member.dokumenAktaNikahByteArrayString.toByteArray(), preferences, context, member.dokumenAktaNikah, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenAktaNikah = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenSlipGaji.isNotEmpty()) {
//                postMemberPicture(member.dokumenSlipGajiByteArrayString.toByteArray(), preferences, context, member.dokumenSlipGaji, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenSlipGaji = result
//                        done--
//                    }
//                })
//            }
//            if (member.dokumenLainnya.isNotEmpty()) {
//                postMemberPicture(member.dokumenLainnyaByteArrayString.toByteArray(), preferences, context, member.dokumenLainnya, object: PostPictureCallback {
//                    override fun onPictureUploaded(result: String) {
//                        member.dokumenLainnya = result
//                        done++
//                    }
//                })
//            }
//        }

        private fun setMemberParams(params : HashMap<String, Any>, member : Member) : HashMap<String, Any> {
//            params["token"] = preferences.getString("token", null)!!
//            params["koperasi_id"] = preferences.getInt("koperasi_id", 0).toString()
//            params["jenis_identitas"] = member.memberJenisIdentitasString()
            params["no_identitas"] = member.noIdentitas
            params["member_handphone"] = member.noHp
            params["email"] = member.email
            params["nama_lengkap"] = member.namaLengkap
//        Log.d("postMember", "Member Tgl Lahir : ${member.tanggalLahir}")
            val oldPattern = "dd-MM-yyyy"
            val pattern = "yyyy-MM-dd HH:mm:ss"
            val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
            val oldDate: Date = oldDateFormat.parse(member.tanggalLahir)!!
            val date: String = simpleDateFormat.format(oldDate)
            params["tanggal_lahir"] = date
            params["tempat_lahir"] = member.tempatLahir
            params["usia"] = member.usia
            params["jenis_kelamin"] = member.jenisKelaminString()
            params["nama_gadis_ibu"] = member.namaGadisIbuKandung
            params["status_perkawinan"] = member.statusPerkawinanString()
            params["pendidikan_terakhir"] = member.pendidikanTerakhirString()

            params["alamat_ktp_jalan"] = member.jalanSesuaiKtp
//            params["alamat_ktp_nomer"] = member.nomorSesuaiKtp
//            params["alamat_ktp_rt"] = member.rtSesuaiKtp
//            params["alamat_ktp_rw"] = member.rwSesuaiKtp
            params["alamat_ktp_kelurahan"] = member.kelurahanSesuaiKtp
            params["alamat_ktp_kecamatan"] = member.kecamatanSesuaiKtp //tidak ada di API
//        params["alamat_ktp_kota_provinsi"] = member.kotaSesuaiKtp //tidak ada di API
            params["alamat_ktp_kota"] = member.kotaSesuaiKtp
            params["alamat_ktp_provinsi"] = member.provinsiSesuaiKtp
            params["alamat_ktp_kode_pos"] = member.kodePosSesuaiKtp
            params["alamat_ktp_status_tempat_tinggal"] = member.statusTempatTinggalSesuaiKtpString()
            member.lamaTahunTinggalSesuaiKtp =  if (member.lamaTahunTinggalSesuaiKtp < 0) 0 else member.lamaTahunTinggalSesuaiKtp
            member.lamaBulanTinggalSesuaiKtp =  if (member.lamaBulanTinggalSesuaiKtp < 0) 0 else member.lamaBulanTinggalSesuaiKtp
            params["alamat_ktp_lama_tinggal"] = member.lamaTahunTinggalSesuaiKtp.toString() + " tahun " + member.lamaBulanTinggalSesuaiKtp.toString() + " bulan"
            params["domisili_sesuai_ktp"] = if (member.domisiliSesuaiKtp) "1" else "0"
            params["alamat_domisili_jalan"] = member.jalanDomisili
//            params["alamat_domisili_nomer"] = member.nomorDomisili
//            params["alamat_domisili_rt"] = member.rtDomisili
//            params["alamat_domisili_rw"] = member.rwDomisili
            params["alamat_domisili_kelurahan"] = member.kelurahanDomisili
            params["alamat_domisili_kecamatan"] = member.kecamatanDomisili
//        params["alamat_domisili_kota_provinsi"] = member.kotaDomisili
            params["alamat_domisili_kota"] = member.kotaDomisili
            params["alamat_domisili_provinsi"] = member.provinsiDomisili
            params["alamat_domisili_kode_pos"] = member.kodePosDomisili
            params["alamat_domisili_status_tempat_tinggal"] = member.statusTempatTinggalDomisiliString()
            member.lamaTahunTinggalDomisili =  if (member.lamaTahunTinggalDomisili < 0) 0 else member.lamaTahunTinggalDomisili
            member.lamaBulanTinggalDomisili =  if (member.lamaBulanTinggalDomisili < 0) 0 else member.lamaBulanTinggalDomisili
            params["alamat_domisili_lama_tempat_tinggal"] = member.lamaTahunTinggalDomisili.toString() + " tahun " + member.lamaBulanTinggalDomisili.toString() + " bulan"

            params["memiliki_npwp"] = if (member.memilikiNpwp == 0) "1" else "0"
            params["nomer_npwp"] = member.nomorNpwp
            params["pekerja_usaha"] = member.pekerjaanString()
            params["jenis_umkm"] = member.jenisUmkm
//            params["bidang_pekerja"] = member.bidangPekerjaan
//            params["posisi_jabatan"] = member.posisiPekerjaan
            params["nama_perusahaan"] = member.namaPerusahaan
            member.lamaTahunBekerja =  if (member.lamaTahunBekerja < 0) 0 else member.lamaTahunBekerja
            member.lamaBulanBekerja =  if (member.lamaBulanBekerja < 0) 0 else member.lamaBulanBekerja
            params["lama_bekerja"] = member.lamaTahunBekerja.toString() + " tahun " + member.lamaBulanBekerja.toString() + " bulan"
            params["penghasilan_omset"] = member.penghasilan.toString()
            params["alamat_kantor_jalan"] = member.jalanKantor
//            params["alamat_kantor_nomer"] = member.nomorKantor
//            params["alamat_kantor_rt"] = member.rtKantor
//            params["alamat_kantor_rw"] = member.rwKantor
            params["alamat_kantor_kelurahan"] = member.kelurahanKantor
            params["alamat_kantor_kecamatan"] = member.kecamatanKantor
//        params["alamat_kantor_kota_provinsi"] = member.kotaKantor
            params["alamat_kantor_kota"] = member.kotaKantor
            params["alamat_kantor_provinsi"] = member.provinsiKantor
            params["alamat_kantor_kode_pos"] = member.kodePosKantor

            params["nama_kontak_darurat"] = member.namaEmergency
            params["nomor_ponsel_darurat"] = member.noHpEmergency
            params["hubungan_kontak_darurat"] = member.hubunganEmergencyString()

            params["nama_pasangan"] = member.namaPasangan
            params["no_identitas_pasangan"] = member.noIdentitasPasangan
            params["pekerjaan_pasangan"] = member.pekerjaanPasangan
            params["no_hp_pasangan"] = member.noHpPasangan

            params["nama_penjamin"] = member.namaPenjamin
            params["no_hp_penjamin"] = member.noHpPenjamin
            params["hubungan_penjamin"] = member.hubunganPenjamin

            params["dokumen_ktp"] = member.dokumenKtp
            params["dokumen_kk"] = member.dokumenKk
            params["dokumen_sim"] = member.dokumenSim
            params["dokumen_keterangan_kerja"] = member.dokumenKetKerja
            params["dokumen_bpkb"] = member.dokumenBpkb
            params["dokumen_akta_nikah"] = member.dokumenAktaNikah
            params["dokumen_slip_gaji"] = member.dokumenSlipGaji
            params["dokumen_lainnya"] = member.dokumenLainnya

            params["survey_luas_rumah"] = member.luasRumah()
            params["survey_jenis_atap"] = member.jenisAtap()
            params["survey_jenis_dinding"] = member.jenisDinding()
            params["survey_kondisi_rumah"] = member.kondisiRumah()
            params["survey_letak_rumah"] = member.letakRumah()
            params["survey_tanggungan_keluarga"] = member.surveyTanggunganKeluarga
            params["survey_data_fisik_perabot"] = member.dataFisikPerabotElektronik()
            params["survey_akses_lembaga_keuangan"] = member.aksesLembagaKeuangan()
            params["survey_info_ttg_usaha"] = member.infoTtgUsaha()
            params["survey_index_rumah"] = member.indeksRumah()
            params["survey_index_asset"] = member.indeksAsset()
            params["survey_kepemilikan_asset"] = member.kepemilikanAsset()
            params["survey_pendapatan_luar_usaha"] = member.pendapatanLuarUsaha()
            params["survey_perkembangan_asset"] = member.perkembanganAsset()
            params["survey_perkembangan_usaha"] = member.perkembanganUsaha()

            params["is_verified"] = (if(member.isVerified) 1 else 0)
            return params
        }

        fun postMember(member: Member, preferences: SharedPreferences, context: Context, callback : PinjamanResponseCallback, pinjaman: Pinjaman) {
            val url = "${API_HOSTNAME}member"

            var params = HashMap<String, Any>()
            params = setMemberParams(params, member)
            Log.d("Member", "Parameter : $params")

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
                val jsonObj = JSONObject(strResp)
                val loginStatus = jsonObj.getInt("status")
                Log.d("postMember", strResp)
                if (loginStatus == 201) {
                    val data = jsonObj.getJSONObject("data")
                    pinjaman.idMember = data.getInt("member_id")
                    postPinjaman(preferences, context, callback, pinjaman)
                }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("postMember", error.toString())
                }
            )
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        fun putMember(member : Member, preferences: SharedPreferences, context: Context, callback: PinjamanResponseCallback, pinjaman: Pinjaman) {
            val url = "${API_HOSTNAME}member/${member.memberId}"

            var params = HashMap<String, Any>()
            params = setMemberParams(params, member)

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.PUT, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
                    val jsonObj = JSONObject(strResp)
                    val loginStatus = jsonObj.getInt("status")
                    Log.d("postMember", strResp)
                    if (loginStatus == 201) {
//                        val data = jsonObj.getJSONObject("data")
//                        pinjaman.idMember = data.getInt("member_id")

                        pinjaman.idMember = member.memberId
                        postPinjaman(preferences, context, callback, pinjaman)
                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("postMember", error.toString())
                }
            )
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

//        fun postMember(member: Member, preferences: SharedPreferences, context: Context, loan: Loan? = null) {
//            val url = "${API_HOSTNAME}member"
//
//            val params = HashMap<String, Any>()
////        params["token"] = preferences.getString("token", null)!!
//
//            params["koperasi_id"] = preferences.getInt("koperasi_id", 0).toString()
////            params["jenis_identitas"] = member.memberJenisIdentitasString()
//            params["no_identitas"] = member.noIdentitas
//            params["member_handphone"] = member.noHp
//            params["email"] = member.email
//            params["nama_lengkap"] = member.namaLengkap
////        Log.d("postMember", "Member Tgl Lahir : ${member.tanggalLahir}")
//            val oldPattern = "dd-MM-yyyy"
//            val pattern = "yyyy-MM-dd HH:mm:ss"
//            val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
//            val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
//            val oldDate: Date = oldDateFormat.parse(member.tanggalLahir)!!
//            val date: String = simpleDateFormat.format(oldDate)
//            params["tanggal_lahir"] = date
//            params["tempat_lahir"] = member.tempatLahir
//            params["usia"] = member.usia
//            params["jenis_kelamin"] = member.jenisKelaminString()
//            params["nama_gadis_ibu"] = member.namaGadisIbuKandung
//            params["status_perkawinan"] = member.statusPerkawinanString()
//            params["pendidikan_terakhir"] = member.pendidikanTerakhirString()
//
//            params["alamat_ktp_jalan"] = member.jalanSesuaiKtp
////            params["alamat_ktp_nomer"] = member.nomorSesuaiKtp
////            params["alamat_ktp_rt"] = member.rtSesuaiKtp
////            params["alamat_ktp_rw"] = member.rwSesuaiKtp
//            params["alamat_ktp_kelurahan"] = member.kelurahanSesuaiKtp
//            params["alamat_ktp_kecamatan"] = member.kecamatanSesuaiKtp //tidak ada di API
////        params["alamat_ktp_kota_provinsi"] = member.kotaSesuaiKtp //tidak ada di API
//            params["alamat_ktp_kota"] = member.kotaSesuaiKtp
//            params["alamat_ktp_provinsi"] = member.provinsiSesuaiKtp
//            params["alamat_ktp_kode_pos"] = member.kodePosSesuaiKtp
//            params["alamat_ktp_status_tempat_tinggal"] = member.statusTempatTinggalSesuaiKtpString()
//            member.lamaTahunTinggalSesuaiKtp =  if (member.lamaTahunTinggalSesuaiKtp < 0) 0 else member.lamaTahunTinggalSesuaiKtp
//            member.lamaBulanTinggalSesuaiKtp =  if (member.lamaBulanTinggalSesuaiKtp < 0) 0 else member.lamaBulanTinggalSesuaiKtp
//            params["alamat_ktp_lama_tinggal"] = member.lamaTahunTinggalSesuaiKtp.toString() + "tahun" + member.lamaBulanTinggalSesuaiKtp.toString() + "bulan"
//            params["domisili_sesuai_ktp"] = if (member.domisiliSesuaiKtp) "1" else "0"
//            params["alamat_domisili_jalan"] = member.jalanDomisili
////            params["alamat_domisili_nomer"] = member.nomorDomisili
////            params["alamat_domisili_rt"] = member.rtDomisili
////            params["alamat_domisili_rw"] = member.rwDomisili
//            params["alamat_domisili_kelurahan"] = member.kelurahanDomisili
//            params["alamat_domisili_kecamatan"] = member.kecamatanDomisili
////        params["alamat_domisili_kota_provinsi"] = member.kotaDomisili
//            params["alamat_domisili_kota"] = member.kotaDomisili
//            params["alamat_domisili_provinsi"] = member.provinsiDomisili
//            params["alamat_domisil_kode_pos"] = member.kodePosDomisili
//            params["alamat_domisili_status_tempat_tinggal"] = member.statusTempatTinggalDomisiliString()
//            member.lamaTahunTinggalDomisili =  if (member.lamaTahunTinggalDomisili < 0) 0 else member.lamaTahunTinggalDomisili
//            member.lamaBulanTinggalDomisili =  if (member.lamaBulanTinggalDomisili < 0) 0 else member.lamaBulanTinggalDomisili
//            params["alamat_domisili_lama_tinggal"] = member.lamaTahunTinggalDomisili.toString() + "tahun" + member.lamaBulanTinggalDomisili.toString() + "bulan"
//
//            params["memiliki_npwp"] = if (member.memilikiNpwp == 0) "1" else "0"
//            params["nomer_npwp"] = member.nomorNpwp
//            params["pekerja_usaha"] = member.pekerjaanString()
//            params["jenis_umkm"] = member.jenisUmkm
////            params["bidang_pekerja"] = member.bidangPekerjaan
////            params["posisi_jabatan"] = member.posisiPekerjaan
//            params["nama_perusahaan"] = member.namaPerusahaan
//            member.lamaTahunBekerja =  if (member.lamaTahunBekerja < 0) 0 else member.lamaTahunBekerja
//            member.lamaBulanBekerja =  if (member.lamaBulanBekerja < 0) 0 else member.lamaBulanBekerja
//            params["lama_bekerja"] = member.lamaTahunBekerja.toString() + "tahun" + member.lamaBulanBekerja.toString() + "bulan"
//            params["penghasilan_omset"] = member.penghasilan.toString()
//            params["alamat_kantor_jalan"] = member.jalanKantor
////            params["alamat_kantor_nomer"] = member.nomorKantor
////            params["alamat_kantor_rt"] = member.rtKantor
////            params["alamat_kantor_rw"] = member.rwKantor
//            params["alamat_kantor_kelurahan"] = member.kelurahanKantor
//            params["alamat_kantor_kecamatan"] = member.kecamatanKantor
////        params["alamat_kantor_kota_provinsi"] = member.kotaKantor
//            params["alamat_kantor_kota"] = member.kotaKantor
//            params["alamat_kantor_provinsi"] = member.provinsiKantor
//            params["alamat_kantor_kode_pos"] = member.kodePosKantor
//
//            params["nama_kontak_darurat"] = member.namaEmergency
//            params["nomor_ponsel_darurat"] = member.noHpEmergency
//            params["hubungan_kontak_darurat"] = member.hubunganEmergencyString()
//
//            params["nama_pasangan"] = member.namaPasangan
//            params["no_identitas_pasangan"] = member.noIdentitasPasangan
//            params["pekerjaan_pasangan"] = member.pekerjaanPasangan
//            params["no_hp_pasangan"] = member.noHpPasangan
//
//            params["nama_penjamin"] = member.namaPenjamin
//            params["no_hp_penjamin"] = member.noHpPenjamin
//            params["hubungan_penjamin"] = member.hubunganPenjamin
//
//            params["dokumen_ktp"] = member.dokumenKtp
//            params["dokumen_kk"] = member.dokumenKk
//            params["dokumen_sim"] = member.dokumenSim
//            params["dokumen_keterangan_kerja"] = member.dokumenKetKerja
//            params["dokumen_bpkb"] = member.dokumenBpkb
//            params["dokumen_akta_nikah"] = member.dokumenAktaNikah
//            params["dokumen_slip_gaji"] = member.dokumenSlipGaji
//            params["dokumen_lainnya"] = member.dokumenLainnya
//
//            params["survey_luas_rumah"] = member.luasRumah()
//            params["survey_jenis_atap"] = member.jenisAtap()
//            params["survey_jenis_dinding"] = member.jenisDinding()
//            params["survey_kondisi_rumah"] = member.kondisiRumah()
//            params["survey_letak_rumah"] = member.letakRumah()
//            params["survey_tanggungan_keluarga"] = member.surveyTanggunganKeluarga
//            params["survey_data_fisik_perabot"] = member.dataFisikPerabotElektronik()
//            params["survey_akses_lembaga_keuangan"] = member.aksesLembagaKeuangan()
//            params["survey_info_ttg_usaha"] = member.infoTtgUsaha()
//            params["survey_index_rumah"] = member.indeksRumah()
//            params["survey_index_asset"] = member.indeksAsset()
//            params["survey_kepemilikan_asset"] = member.kepemilikanAsset()
//            params["survey_pendapatan_luar_usaha"] = member.pendapatanLuarUsaha()
//            params["survey_perkembangan_asset"] = member.perkembanganAsset()
//            params["survey_perkembangan_usaha"] = member.perkembanganUsaha()
//
//            params["is_verified"] = (if(member.isVerified) 1 else 0).toString()
//
//            val parameters = JSONObject(params as Map<*, *>)
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.POST, url, parameters,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
////                val jsonObj = JSONObject(strResp)
////                val loginStatus = jsonObj.getString("status")
//                    Log.d("postMember", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
//                    if (loan != null) {
//                        postLoan(loan, preferences, context)
//                    }
//                },
//                Response.ErrorListener { error ->
//                    Log.e("postMember", error.toString())
//                }
//            )
//            {
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    val container = "Bearer " + preferences.getString("token", null)!!
//                    headers["Authorization"] = container
//                    //..add other headers
//                    return headers
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//        }

        fun postMemberPicture(imageData: ByteArray, preferences: SharedPreferences, context: Context, fileName: String, callback: PostPictureCallback) {
            val url = "${API_HOSTNAME}member/picture"
//        imageData?: return

//        val params = HashMap<String, String>()
//        params["image"] = String(imageData, charset = Charset.forName("utf-8"))
            val request = object : VolleyFileUploadRequest(
                Method.POST,
                url,
                Response.Listener {response ->
//                    println("response is: $it")
                    Log.d("postPicture", "Response : $response")
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    if (status == 201) {
                        val data = jsonObj.getJSONObject("data")
                        val responseFileName = data.getString("file_name")
                        callback.onPictureUploaded(responseFileName)
                    }
//                    val loginStatus = jsonObj.getString("status")

                },
                Response.ErrorListener {error ->
                    println("error is: $error")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
                override fun getByteData(): MutableMap<String, FileDataPart> {
                    val params = HashMap<String, FileDataPart>()
                    params["image"] = FileDataPart(fileName, imageData, "jpeg")
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        }

        fun postPicture(imageData: ByteArray, preferences: SharedPreferences, context: Context, memberId: Int, fileName: String) {
            val url = "${API_HOSTNAME}loan/member/picture/$memberId"
//        imageData?: return

//        val params = HashMap<String, String>()
//        params["image"] = String(imageData, charset = Charset.forName("utf-8"))
            val request = object : VolleyFileUploadRequest(
                Method.POST,
                url,
                Response.Listener {
                    println("response is: $it")
                    Log.d("postPicture", "Response : $it")
                },
                Response.ErrorListener {
                    println("error is: $it")
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
                override fun getByteData(): MutableMap<String, FileDataPart> {
                    val params = HashMap<String, FileDataPart>()
                    params["image"] = FileDataPart(fileName, imageData, "jpeg")
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        }

//        @SuppressLint("RestrictedApi")
//        fun putMemberOnDataBuilder(member: Member): Data.Builder {
//            val data = Data.Builder()
//
//            data.put("jenis_identitas", member.jenisIdentitas)
//            data.put("no_identitas", member.noIdentitas)
//            data.put("nama_lengkap", member.namaLengkap)
//            data.put("member_handphone", member.noHp)
//
//            data.put("tanggal_lahir", member.tanggalLahir)
//            data.put("tempat_lahir", member.tempatLahir)
//            data.put("jenis_kelamin", member.jenisKelamin)
//            data.put("nama_gadis_ibu", member.namaGadisIbuKandung)
//            data.put("status_perkawinan", member.statusPerkawinan)
//            data.put("pendidikan_terakhir", member.pendidikanTerakhir)
//
//            data.put("alamat_ktp_jalan", member.jalanSesuaiKtp)
//            data.put("alamat_ktp_nomer", member.nomorSesuaiKtp)
//            data.put("alamat_ktp_rt", member.rtSesuaiKtp)
//            data.put("alamat_ktp_rw", member.rwSesuaiKtp)
//            data.put("alamat_ktp_kelurahan", member.kelurahanSesuaiKtp)
//            data.put("alamat_ktp_kecamatan", member.kecamatanSesuaiKtp) //tidak ada di API
////        data.put("alamat_ktp_kota_provinsi", member.kotaSesuaiKtp //tidak ada di API)
//            data.put("alamat_ktp_kota", member.kotaSesuaiKtp)
//            data.put("alamat_ktp_provinsi", member.provinsiSesuaiKtp)
//            data.put("alamat_ktp_status_tempat_tinggal", member.statusTempatTinggalSesuaiKtp)
//            data.put("alamat_ktp_lama_tahun_tinggal", member.lamaTahunTinggalSesuaiKtp)
//            data.put("alamat_ktp_lama_bulan_tinggal", member.lamaBulanTinggalSesuaiKtp)
//            data.put("domisili_sesuai_ktp", member.domisiliSesuaiKtp)
//            data.put("alamat_domisili_jalan", member.jalanDomisili)
//            data.put("alamat_domisili_nomer", member.nomorDomisili)
//            data.put("alamat_domisili_rt", member.rtDomisili)
//            data.put("alamat_domisili_rw", member.rwDomisili)
//            data.put("alamat_domisili_kelurahan", member.kelurahanDomisili)
//            data.put("alamat_domisili_kecamatan", member.kecamatanDomisili)
////        data.put("alamat_domisili_kota_provinsi", member.kotaDomisili)
//            data.put("alamat_domisili_kota", member.kotaDomisili)
//            data.put("alamat_domisili_provinsi", member.provinsiDomisili)
//            data.put("alamat_domisili_status_tempat_tinggal", member.statusTempatTinggalDomisili)
//            data.put("alamat_domisili_lama_tahun_tinggal", member.lamaTahunTinggalDomisili)
//            data.put("alamat_domisili_lama_bulan_tinggal", member.lamaBulanTinggalDomisili)
//
//            data.put("memiliki_npwp", member.memilikiNpwp)
//            data.put("nomer_npwp", member.nomorNpwp)
//            data.put("pekerja_usaha", member.pekerjaan)
//            data.put("bidang_pekerja", member.bidangPekerjaan)
//            data.put("posisi_jabatan", member.posisiPekerjaan)
//            data.put("nama_perusahaan", member.namaPerusahaan)
//            data.put("lama_tahun_bekerja", member.lamaTahunBekerja)
//            data.put("lama_bulan_bekerja", member.lamaBulanBekerja)
//            data.put("penghasilan_omset", member.penghasilan)
//            data.put("alamat_kantor_jalan", member.jalanKantor)
//            data.put("alamat_kantor_nomer", member.nomorKantor)
//            data.put("alamat_kantor_rt", member.rtKantor)
//            data.put("alamat_kantor_rw", member.rwKantor)
//            data.put("alamat_kantor_kelurahan", member.kelurahanKantor)
//            data.put("alamat_kantor_kecamatan", member.kecamatanKantor)
////        data.put("alamat_kantor_kota_provinsi", member.kotaKantor)
//            data.put("alamat_kantor_kota", member.kotaKantor)
//            data.put("alamat_kantor_provinsi", member.provinsiKantor)
//
//            data.put("nama", member.namaEmergency)
//            data.put("no_hp", member.noHpEmergency)
//            data.put("hubungan", member.hubunganEmergency)
//
//            data.put("is_verified", member.isVerified)
//            return data
//        }
    }
}