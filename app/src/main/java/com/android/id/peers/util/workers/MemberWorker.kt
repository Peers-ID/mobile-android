package com.android.id.peers.util.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_GET_MEMBER_BY_PHONE
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_MEMBER
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate

class MemberWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    val context = appContext

    override fun doWork(): Result {

        val preferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val member = Member()

        member.jenisIdentitas = inputData.getInt("jenis_identitas", 0)
        member.noIdentitas = inputData.getString("no_identitas")!!
        member.namaLengkap = inputData.getString("nama_lengkap")!!
        member.noHp = inputData.getString("member_handphone")!!
        member.tanggalLahir = inputData.getString("tanggal_lahir")!!
        member.tempatLahir = inputData.getString("tempat_lahir")!!
        member.jenisKelamin = inputData.getInt("jenis_kelamin", 0)
        member.namaGadisIbuKandung = inputData.getString("nama_gadis_ibu")!!
        member.statusPerkawinan = inputData.getInt("status_perkawinan", 0)
        member.pendidikanTerakhir = inputData.getInt("pendidikan_terakhir", 0)

        member.jalanSesuaiKtp = inputData.getString("alamat_ktp_jalan")!!
        member.nomorSesuaiKtp = inputData.getString("alamat_ktp_nomer")!!
        member.rtSesuaiKtp = inputData.getString("alamat_ktp_rt")!!
        member.rwSesuaiKtp = inputData.getString("alamat_ktp_rw")!!
        member.kelurahanSesuaiKtp = inputData.getString("alamat_ktp_kelurahan")!!
        member.kecamatanSesuaiKtp = inputData.getString("alamat_ktp_kecamatan")!!
        member.kotaSesuaiKtp = inputData.getString("alamat_ktp_kota")!!
        member.provinsiSesuaiKtp = inputData.getString("alamat_ktp_provinsi")!!
        member.statusTempatTinggalSesuaiKtp = inputData.getInt("alamat_ktp_status_tempat_tinggal", 0)
        member.lamaTahunTinggalSesuaiKtp = inputData.getInt("alamat_ktp_lama_tahun_tinggal", 0)
        member.lamaBulanTinggalSesuaiKtp = inputData.getInt("alamat_ktp_lama_bulan_tinggal", 0)

        member.jalanDomisili = inputData.getString("alamat_domisili_jalan")!!
        member.nomorDomisili = inputData.getString("alamat_domisili_nomer")!!
        member.rtDomisili = inputData.getString("alamat_domisili_rt")!!
        member.rwDomisili = inputData.getString("alamat_domisili_rw")!!
        member.kelurahanDomisili = inputData.getString("alamat_domisili_kelurahan")!!
        member.kecamatanDomisili = inputData.getString("alamat_domisili_kecamatan")!!
        member.kotaDomisili = inputData.getString("alamat_domisili_kota")!!
        member.provinsiDomisili = inputData.getString("alamat_domisili_provinsi")!!
        member.statusTempatTinggalDomisili = inputData.getInt("alamat_domisili_status_tempat_tinggal", 0)
        member.lamaTahunTinggalDomisili = inputData.getInt("alamat_domisili_lama_tahun_tinggal", 0)
        member.lamaBulanTinggalDomisili = inputData.getInt("alamat_domisili_lama_bulan_tinggal", 0)

        member.memilikiNpwp = inputData.getInt("memiliki_npwp", 0)
        member.nomorNpwp = inputData.getString("nomer_npwp")!!
        member.pekerjaan = inputData.getInt("nomer_npwp", 0)
        member.bidangPekerjaan = inputData.getString("bidang_pekerja")!!
        member.posisiPekerjaan = inputData.getString("posisi_jabatan")!!
        member.namaPerusahaan = inputData.getString("nama_perusahaan")!!

        member.lamaTahunBekerja = inputData.getInt("lama_tahun_bekerja", 0)
        member.lamaBulanBekerja = inputData.getInt("lama_bulan_bekerja", 0)
        member.penghasilan = inputData.getLong("penghasilan_omset", 0)
        member.jalanKantor = inputData.getString("alamat_kantor_jalan")!!
        member.nomorKantor = inputData.getString("alamat_kantor_nomer")!!
        member.rtKantor = inputData.getString("alamat_kantor_rt")!!
        member.rwKantor = inputData.getString("alamat_kantor_rw")!!
        member.kelurahanKantor = inputData.getString("alamat_kantor_kelurahan")!!
        member.kecamatanKantor = inputData.getString("alamat_kantor_kecamatan")!!
        member.kotaKantor = inputData.getString("alamat_kantor_kota")!!
        member.provinsiKantor = inputData.getString("alamat_kantor_provinsi")!!

        member.namaEmergency = inputData.getString("nama")!!
        member.noHpEmergency = inputData.getString("no_hp")!!
        member.hubunganEmergency = inputData.getInt("hubungan", 0)
        member.isVerified = inputData.getBoolean("is_verified", false)

        authenticate(preferences, context, REQUEST_TYPE_GET_MEMBER_BY_PHONE, object : RepaymentCollection {
            override fun onSuccess(result: Member) {
                if (result.id == 0) {
                    authenticate(preferences,
                        context, REQUEST_TYPE_POST_MEMBER, member)
                }
            }
        }, memberPhone = member.noHp)

        return Result.success()
    }
}