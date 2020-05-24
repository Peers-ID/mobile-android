package com.android.id.peers.members.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.work.Data
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.Loan.Companion.postLoan
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.callback.SplashScreen
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
    @ColumnInfo(name = "hubungan") var hubunganEmergency : Int = -1,

    /*verified*/
    @ColumnInfo(name = "is_verified") var isVerified : Boolean = false

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

    companion object {
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
                        val config = MemberAcquisitionConfig()
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val firstObj = data.getJSONObject(0)
                            val idKoperasi = firstObj.getInt("koperasi_id")
                            config.jenisIdentitas = firstObj.getInt("jenis_identitas")
                            config.noIdentitas = firstObj.getInt("no_identitas")
                            config.namaLengkap = firstObj.getInt("nama_lengkap")
                            config.memberHandphone = firstObj.getInt("member_handphone")
                            config.tanggalLahir = firstObj.getInt("tanggal_lahir")
                            config.tempatLahir = firstObj.getInt("tempat_lahir")
                            config.jenisKelamin = firstObj.getInt("jenis_kelamin")
                            config.namaGadisIbu = firstObj.getInt("nama_gadis_ibu")
                            config.statusPerkawinan = firstObj.getInt("status_perkawinan")
                            config.pendidikanTerakhir = firstObj.getInt("pendidikan_terakhir")
                            config.alamatKtpJalan = firstObj.getInt("alamat_ktp_jalan")
                            config.alamatKtpNo = firstObj.getInt("alamat_ktp_nomer")
                            config.alamatKtpRt = firstObj.getInt("alamat_ktp_rt")
                            config.alamatKtpRw = firstObj.getInt("alamat_ktp_rw")
                            config.alamatKtpKelurahan = firstObj.getInt("alamat_ktp_kelurahan")
                            config.alamatKtpKecamatan = firstObj.getInt("alamat_ktp_kecamatan")
                            config.alamatKtpKota = firstObj.getInt("alamat_ktp_kota")
                            config.alamatKtpProvinsi = firstObj.getInt("alamat_ktp_provinsi")
                            config.alamatKtpStatusTempatTinggal = firstObj.getInt("alamat_ktp_status_tempat_tinggal")
                            config.alamatKtpLamaTinggal = firstObj.getInt("alamat_ktp_lama_tinggal")
                            config.domisiliSesuaiKtp = firstObj.getInt("domisili_sesuai_ktp")
                            config.alamatDomisiliJalan = firstObj.getInt("alamat_domisili_jalan")
                            config.alamatDomisiliNo = firstObj.getInt("alamat_domisili_nomer")
                            config.alamatDomisiliRt = firstObj.getInt("alamat_domisili_rt")
                            config.alamatDomisiliRw = firstObj.getInt("alamat_domisili_rw")
                            config.alamatDomisiliKelurahan = firstObj.getInt("alamat_domisili_kelurahan")
                            config.alamatDomisiliKecamatan = firstObj.getInt("alamat_domisili_kecamatan")
//                    config.alamatDomisiliKotaProvinsi = firstObj.getInt("alamat_domisili_kota_provinsi")
                            config.alamatDomisiliKota = firstObj.getInt("alamat_domisili_kota")
                            config.alamatDomisiliProvinsi = firstObj.getInt("alamat_domisili_provinsi")
                            config.alamatDomisiliStatusTempatTinggal = firstObj.getInt("alamat_domisili_status_tempat_tinggal")
                            config.alamatDomisiliLamaTempatTinggal = firstObj.getInt("alamat_domisili_lama_tempat_tinggal")
                            config.memilikiNpwp = firstObj.getInt("memiliki_npwp")
                            config.nomerNpwp = firstObj.getInt("nomer_npwp")
                            config.pekerjaUsaha = firstObj.getInt("pekerja_usaha")
                            config.bidangPekerja = firstObj.getInt("bidang_pekerja")
                            config.posisiJabatan = firstObj.getInt("posisi_jabatan")
                            config.namaPerusahaan = firstObj.getInt("nama_perusahaan")
                            config.lamaBekerja = firstObj.getInt("lama_bekerja")
                            config.penghasilanOmset = firstObj.getInt("penghasilan_omset")
                            config.alamatKantorJalan = firstObj.getInt("alamat_kantor_jalan")
                            config.alamatKantorNo = firstObj.getInt("alamat_kantor_nomer")
                            config.alamatKantorRt = firstObj.getInt("alamat_kantor_rt")
                            config.alamatKantorRw = firstObj.getInt("alamat_kantor_rw")
                            config.alamatKantorKelurahan = firstObj.getInt("alamat_kantor_kelurahan")
                            config.alamatKantorKecamatan = firstObj.getInt("alamat_kantor_kecamatan")
//                    config.alamatKantorKotaProvinsi = firstObj.getInt("alamat_kantor_kota_provinsi")
                            config.alamatKantorKota = firstObj.getInt("alamat_kantor_kota")
                            config.alamatKantorProvinsi = firstObj.getInt("alamat_kantor_provinsi")
                            config.namaEmergency = firstObj.getInt("nama")
                            config.noHpEmergency = firstObj.getInt("no_hp")
                            config.hubungan = firstObj.getInt("hubungan")
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

        fun getMember(preferences: SharedPreferences, context: Context, callback: RepaymentCollection, memberId: Int) {
            val url = "${API_HOSTNAME}member/$memberId"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val member = Member()
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val memberJsonObj = data.getJSONObject(0)
                            member.setMemberJenisIdentitas(memberJsonObj.getString("jenis_identitas"))
                            member.noIdentitas = memberJsonObj.getString("no_identitas")
                            member.namaLengkap = memberJsonObj.getString("nama_lengkap")
                            member.noHp = memberJsonObj.getString("member_handphone")
                            member.tanggalLahir = memberJsonObj.getString("tanggal_lahir")
                            member.tempatLahir = memberJsonObj.getString("tempat_lahir")
                            Log.d("getMember", "Jenis Kelamin : ${memberJsonObj.getString("jenis_kelamin")}")
                            member.setMemberJenisKelamin(memberJsonObj.getString("jenis_kelamin"))
                            member.namaGadisIbuKandung = memberJsonObj.getString("nama_gadis_ibu")
                            member.setMemberStatusPerkawinan(memberJsonObj.getString("status_perkawinan"))
                            member.setMemberPendidikanTerakhir(memberJsonObj.getString("pendidikan_terakhir"))

                            member.jalanSesuaiKtp = memberJsonObj.getString("alamat_ktp_jalan")
                            Log.d("getMember", member.jalanSesuaiKtp)
                            member.nomorSesuaiKtp = memberJsonObj.getString("alamat_ktp_nomer")
                            member.rtSesuaiKtp = memberJsonObj.getString("alamat_ktp_rt")
                            member.rwSesuaiKtp = memberJsonObj.getString("alamat_ktp_rw")
                            member.kelurahanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kelurahan")
                            member.kecamatanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kecamatan")
                            member.kotaSesuaiKtp = memberJsonObj.getString("alamat_ktp_kota")
                            member.provinsiSesuaiKtp = memberJsonObj.getString("alamat_ktp_provinsi")
                            member.setStatusTempatTinggalSesuaiKtp(memberJsonObj.getString("alamat_ktp_status_tempat_tinggal"))
                            val lamaTinggalSesuaiKtp = memberJsonObj.getInt("alamat_ktp_lama_tinggal")
                            member.lamaTahunTinggalSesuaiKtp = lamaTinggalSesuaiKtp / 12
                            member.lamaBulanTinggalSesuaiKtp = lamaTinggalSesuaiKtp % 12

                            member.jalanDomisili = memberJsonObj.getString("alamat_domisili_jalan")
                            member.nomorDomisili = memberJsonObj.getString("alamat_domisili_nomer")
                            member.rtDomisili = memberJsonObj.getString("alamat_domisili_rt")
                            member.rwDomisili = memberJsonObj.getString("alamat_domisili_rw")
                            member.kelurahanDomisili = memberJsonObj.getString("alamat_domisili_kelurahan")
                            member.kecamatanDomisili = memberJsonObj.getString("alamat_domisili_kecamatan")
                            member.kotaDomisili = memberJsonObj.getString("alamat_domisili_kota")
                            member.provinsiDomisili = memberJsonObj.getString("alamat_domisili_provinsi")
                            member.setStatusTempatTinggalDomisili(memberJsonObj.getString("alamat_domisili_status_tempat_tinggal"))
                            val lamaTinggalDomisili = memberJsonObj.getInt("alamat_domisili_lama_tempat_tinggal")
                            member.lamaTahunTinggalDomisili = lamaTinggalDomisili / 12
                            member.lamaBulanTinggalDomisili = lamaTinggalDomisili % 12

                            member.memilikiNpwp = if(memberJsonObj.getInt("memiliki_npwp") == 0) 1 else 0
                            member.nomorNpwp = memberJsonObj.getString("nomer_npwp")
                            member.setPekerjaan(memberJsonObj.getString("pekerja_usaha"))
                            member.bidangPekerjaan = memberJsonObj.getString("bidang_pekerja")
                            member.posisiPekerjaan = memberJsonObj.getString("posisi_jabatan")
                            member.namaPerusahaan = memberJsonObj.getString("nama_perusahaan")

                            val lamaBekerja = memberJsonObj.getInt("lama_bekerja")
                            member.lamaTahunBekerja = lamaBekerja / 12
                            member.lamaBulanBekerja = lamaBekerja % 12
                            member.penghasilan = memberJsonObj.getLong("penghasilan_omset")
                            member.jalanKantor = memberJsonObj.getString("alamat_kantor_jalan")
                            member.nomorKantor = memberJsonObj.getString("alamat_kantor_nomer")
                            member.rtKantor = memberJsonObj.getString("alamat_kantor_rt")
                            member.rwKantor = memberJsonObj.getString("alamat_kantor_rw")
                            member.kelurahanKantor = memberJsonObj.getString("alamat_kantor_kelurahan")
                            member.kecamatanKantor = memberJsonObj.getString("alamat_kantor_kecamatan")
                            member.kotaKantor = memberJsonObj.getString("alamat_kantor_kota")
                            member.provinsiKantor = memberJsonObj.getString("alamat_kantor_provinsi")

                            member.namaEmergency = memberJsonObj.getString("nama")
                            member.noHpEmergency = memberJsonObj.getString("no_hp")
                            member.setHubunganEmergency(memberJsonObj.getString("hubungan"))

                        }
                        callback.onSuccess(member)
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

        fun getMemberByPhone(preferences: SharedPreferences, context: Context, callback: RepaymentCollection, memberPhone: String) {
            val url = "${API_HOSTNAME}member/phone/$memberPhone"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val member = Member()
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val memberJsonObj = data.getJSONObject(0)
                            member.id = memberJsonObj.getInt("member_id")
                            member.noIdentitas = memberJsonObj.getString("no_identitas")
                            member.namaLengkap = memberJsonObj.getString("nama_lengkap")

                            callback.onSuccess(member)
                        } else {
                            callback.onSuccess(Member())
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
                            Log.d("getMemberByPhone", "RES : $res")
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

        fun postMember(member: Member, preferences: SharedPreferences, context: Context, loan: Loan? = null) {
            val url = "${API_HOSTNAME}member"

            val params = HashMap<String, String>()
//        params["token"] = preferences.getString("token", null)!!

            params["koperasi_id"] = preferences.getInt("koperasi_id", 0).toString()
            params["jenis_identitas"] = member.memberJenisIdentitasString()
            params["no_identitas"] = member.noIdentitas
            params["nama_lengkap"] = member.namaLengkap
            params["member_handphone"] = member.noHp
//        Log.d("postMember", "Member Tgl Lahir : ${member.tanggalLahir}")
            val oldPattern = "dd-MM-yyyy"
            val pattern = "yyyy-MM-dd HH:mm:ss"
            val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
            val oldDate: Date = oldDateFormat.parse(member.tanggalLahir)!!
            val date: String = simpleDateFormat.format(oldDate)
            params["tanggal_lahir"] = date
            params["tempat_lahir"] = member.tempatLahir
            params["jenis_kelamin"] = member.jenisKelaminString()
            params["nama_gadis_ibu"] = member.namaGadisIbuKandung
            params["status_perkawinan"] = member.statusPerkawinanString()
            params["pendidikan_terakhir"] = member.pendidikanTerakhirString()

            params["alamat_ktp_jalan"] = member.jalanSesuaiKtp
            params["alamat_ktp_nomer"] = member.nomorSesuaiKtp
            params["alamat_ktp_rt"] = member.rtSesuaiKtp
            params["alamat_ktp_rw"] = member.rwSesuaiKtp
            params["alamat_ktp_kelurahan"] = member.kelurahanSesuaiKtp
            params["alamat_ktp_kecamatan"] = member.kecamatanSesuaiKtp //tidak ada di API
//        params["alamat_ktp_kota_provinsi"] = member.kotaSesuaiKtp //tidak ada di API
            params["alamat_ktp_kota"] = member.kotaSesuaiKtp
            params["alamat_ktp_provinsi"] = member.provinsiSesuaiKtp
            params["alamat_ktp_status_tempat_tinggal"] = member.statusTempatTinggalSesuaiKtpString()
            params["alamat_ktp_lama_tinggal"] = ((member.lamaTahunTinggalSesuaiKtp - 1) * 12 + (member.lamaBulanTinggalSesuaiKtp - 1)).toString()
            params["domisili_sesuai_ktp"] = if (member.domisiliSesuaiKtp) "1" else "0"
            params["alamat_domisili_jalan"] = member.jalanDomisili
            params["alamat_domisili_nomer"] = member.nomorDomisili
            params["alamat_domisili_rt"] = member.rtDomisili
            params["alamat_domisili_rw"] = member.rwDomisili
            params["alamat_domisili_kelurahan"] = member.kelurahanDomisili
            params["alamat_domisili_kecamatan"] = member.kecamatanDomisili
//        params["alamat_domisili_kota_provinsi"] = member.kotaDomisili
            params["alamat_domisili_kota"] = member.kotaDomisili
            params["alamat_domisili_provinsi"] = member.provinsiDomisili
            params["alamat_domisili_status_tempat_tinggal"] = member.statusTempatTinggalDomisiliString()
            params["alamat_domisili_lama_tempat_tinggal"] = ((member.lamaTahunTinggalDomisili - 1) * 12 + (member.lamaBulanTinggalDomisili - 1)).toString()

            params["memiliki_npwp"] = if (member.memilikiNpwp == 0) "1" else "0"
            params["nomer_npwp"] = member.nomorNpwp
            params["pekerja_usaha"] = member.pekerjaanString()
            params["bidang_pekerja"] = member.bidangPekerjaan
            params["posisi_jabatan"] = member.posisiPekerjaan
            params["nama_perusahaan"] = member.namaPerusahaan
            params["lama_bekerja"] = ((member.lamaTahunBekerja - 1) * 12 + (member.lamaBulanBekerja - 1)).toString()
            params["penghasilan_omset"] = member.penghasilan.toString()
            params["alamat_kantor_jalan"] = member.jalanKantor
            params["alamat_kantor_nomer"] = member.nomorKantor
            params["alamat_kantor_rt"] = member.rtKantor
            params["alamat_kantor_rw"] = member.rwKantor
            params["alamat_kantor_kelurahan"] = member.kelurahanKantor
            params["alamat_kantor_kecamatan"] = member.kecamatanKantor
//        params["alamat_kantor_kota_provinsi"] = member.kotaKantor
            params["alamat_kantor_kota"] = member.kotaKantor
            params["alamat_kantor_provinsi"] = member.provinsiKantor

            params["nama"] = member.namaEmergency
            params["no_hp"] = member.noHpEmergency
            params["hubungan"] = member.hubunganEmergencyString()

            params["is_verified"] = member.isVerified.toString()

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
//                val jsonObj = JSONObject(strResp)
//                val loginStatus = jsonObj.getString("status")
                    Log.d("postMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (loan != null) {
                        postLoan(loan, preferences, context)
                    }
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

        fun postPicture(imageData: ByteArray, preferences: SharedPreferences, context: Context, memberId: Int, fileName: String) {
            val url = "${API_HOSTNAME}member/picture/$memberId"
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

        @SuppressLint("RestrictedApi")
        fun putMemberOnDataBuilder(member: Member): Data.Builder {
            val data = Data.Builder()

            data.put("jenis_identitas", member.jenisIdentitas)
            data.put("no_identitas", member.noIdentitas)
            data.put("nama_lengkap", member.namaLengkap)
            data.put("member_handphone", member.noHp)
            
            data.put("tanggal_lahir", member.tanggalLahir)
            data.put("tempat_lahir", member.tempatLahir)
            data.put("jenis_kelamin", member.jenisKelamin)
            data.put("nama_gadis_ibu", member.namaGadisIbuKandung)
            data.put("status_perkawinan", member.statusPerkawinan)
            data.put("pendidikan_terakhir", member.pendidikanTerakhir)

            data.put("alamat_ktp_jalan", member.jalanSesuaiKtp)
            data.put("alamat_ktp_nomer", member.nomorSesuaiKtp)
            data.put("alamat_ktp_rt", member.rtSesuaiKtp)
            data.put("alamat_ktp_rw", member.rwSesuaiKtp)
            data.put("alamat_ktp_kelurahan", member.kelurahanSesuaiKtp)
            data.put("alamat_ktp_kecamatan", member.kecamatanSesuaiKtp) //tidak ada di API
//        data.put("alamat_ktp_kota_provinsi", member.kotaSesuaiKtp //tidak ada di API)
            data.put("alamat_ktp_kota", member.kotaSesuaiKtp)
            data.put("alamat_ktp_provinsi", member.provinsiSesuaiKtp)
            data.put("alamat_ktp_status_tempat_tinggal", member.statusTempatTinggalSesuaiKtp)
            data.put("alamat_ktp_lama_tahun_tinggal", member.lamaTahunTinggalSesuaiKtp)
            data.put("alamat_ktp_lama_bulan_tinggal", member.lamaBulanTinggalSesuaiKtp)
            data.put("domisili_sesuai_ktp", member.domisiliSesuaiKtp)
            data.put("alamat_domisili_jalan", member.jalanDomisili)
            data.put("alamat_domisili_nomer", member.nomorDomisili)
            data.put("alamat_domisili_rt", member.rtDomisili)
            data.put("alamat_domisili_rw", member.rwDomisili)
            data.put("alamat_domisili_kelurahan", member.kelurahanDomisili)
            data.put("alamat_domisili_kecamatan", member.kecamatanDomisili)
//        data.put("alamat_domisili_kota_provinsi", member.kotaDomisili)
            data.put("alamat_domisili_kota", member.kotaDomisili)
            data.put("alamat_domisili_provinsi", member.provinsiDomisili)
            data.put("alamat_domisili_status_tempat_tinggal", member.statusTempatTinggalDomisili)
            data.put("alamat_domisili_lama_tahun_tinggal", member.lamaTahunTinggalDomisili)
            data.put("alamat_domisili_lama_bulan_tinggal", member.lamaBulanTinggalDomisili)

            data.put("memiliki_npwp", member.memilikiNpwp)
            data.put("nomer_npwp", member.nomorNpwp)
            data.put("pekerja_usaha", member.pekerjaan)
            data.put("bidang_pekerja", member.bidangPekerjaan)
            data.put("posisi_jabatan", member.posisiPekerjaan)
            data.put("nama_perusahaan", member.namaPerusahaan)
            data.put("lama_tahun_bekerja", member.lamaTahunBekerja)
            data.put("lama_bulan_bekerja", member.lamaBulanBekerja)
            data.put("penghasilan_omset", member.penghasilan)
            data.put("alamat_kantor_jalan", member.jalanKantor)
            data.put("alamat_kantor_nomer", member.nomorKantor)
            data.put("alamat_kantor_rt", member.rtKantor)
            data.put("alamat_kantor_rw", member.rwKantor)
            data.put("alamat_kantor_kelurahan", member.kelurahanKantor)
            data.put("alamat_kantor_kecamatan", member.kecamatanKantor)
//        data.put("alamat_kantor_kota_provinsi", member.kotaKantor)
            data.put("alamat_kantor_kota", member.kotaKantor)
            data.put("alamat_kantor_provinsi", member.provinsiKantor)

            data.put("nama", member.namaEmergency)
            data.put("no_hp", member.noHpEmergency)
            data.put("hubungan", member.hubunganEmergency)

            data.put("is_verified", member.isVerified)
            return data
        }
    }
}