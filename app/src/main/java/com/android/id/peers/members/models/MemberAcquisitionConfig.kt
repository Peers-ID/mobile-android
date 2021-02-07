package com.android.id.peers.members.models

import android.content.SharedPreferences
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MemberAcquisitionConfig(
//    var jenisIdentitas: Int = 1,
    var noIdentitas: Int = 1,
    var memberHandphone: Int = 1,
    var email: Int = 1,
    var namaLengkap: Int = 1,
    var tanggalLahir: Int = 1,
    var usia: Int = 1,
    var tempatLahir: Int = 1,
    var jenisKelamin: Int = 1,
    var statusPerkawinan: Int = 1,
    var pendidikanTerakhir: Int = 1,
    var namaGadisIbu: Int = 1,

    var alamatKtpJalan: Int = 1,
//    var alamatKtpNo: Int = 1,
//    var alamatKtpRt: Int = 1,
//    var alamatKtpRw: Int = 1,
    var alamatKtpKelurahan: Int = 1,
    var alamatKtpKecamatan: Int = 1,
    var alamatKtpKota: Int = 1,
    var alamatKtpProvinsi: Int = 1,
    var alamatKtpKodePos: Int = 1,
    var alamatKtpStatusTempatTinggal: Int = 1,
    var alamatKtpLamaTinggal: Int = 1,
    var domisiliSesuaiKtp: Int = 1,
    var alamatDomisiliJalan: Int = 1,
//    var alamatDomisiliNo: Int = 1,
//    var alamatDomisiliRt: Int = 1,
//    var alamatDomisiliRw: Int = 1,
    var alamatDomisiliKelurahan: Int = 1,
    var alamatDomisiliKecamatan: Int = 1,
//    var alamatDomisiliKotaProvinsi: Int = 1,
    var alamatDomisiliKota: Int = 1,
    var alamatDomisiliProvinsi: Int = 1,
    var alamatDomisiliKodePos: Int = 1,
    var alamatDomisiliStatusTempatTinggal: Int = 1,
    var alamatDomisiliLamaTempatTinggal: Int = 1,
    var memilikiNpwp: Int = 1,
    var nomerNpwp: Int = 1,
    var pekerjaUsaha: Int = 1,
//    var bidangPekerja: Int = 1,
//    var posisiJabatan: Int = 1,
    var jenisUmkm: Int = 1,
    var namaPerusahaan: Int = 1,
    var lamaBekerja: Int = 1,
    var penghasilanOmset: Int = 1,
    var alamatKantorJalan: Int = 1,
//    var alamatKantorNo: Int = 1,
//    var alamatKantorRt: Int = 1,
//    var alamatKantorRw: Int = 1,
    var alamatKantorKelurahan: Int = 1,
    var alamatKantorKecamatan: Int = 1,
//    var alamatKantorKotaProvinsi: Int = 1,
    var alamatKantorKota: Int = 1,
    var alamatKantorProvinsi: Int = 1,
    var alamatKantorKodePos: Int = 1,
    var namaEmergency: Int = 1,
    var noHpEmergency: Int = 1,
    var hubungan: Int = 1,

    var namaPasangan: Int = 1,
    var noIdentitasPasangan: Int = 1,
    var pekerjaanPasangan: Int = 1,
    var noHpPasangan: Int = 1,

    var namaPenjamin: Int = 1,
    var noHpPenjamin: Int = 1,
    var hubunganPenjamin: Int = 1,

    var dokumenKtp: Int = 1,
    var dokumenSim: Int = 1,
    var dokumenKk: Int = 1,
    var dokumenKetKerja: Int = 1,
    var dokumenSlipGaji: Int = 1,
    var dokumenAktaNikah: Int = 1,
    var dokumenBpkb: Int = 1,
    var dokumenLainnya: Int = 1,

    var surveyLuasRumah: Int = 1,
    var surveyJenisAtap: Int = 1,
    var surveyJenisDinding: Int = 1,
    var surveyKondisiRumah: Int = 1,
    var surveyLetakRumah: Int = 1,
    var surveyTanggunganKeluarga: Int = 1,

    var surveyDataFisikPerabot: Int = 1,
    var surveyAksesLembagaKeuangan: Int = 1,
    var surveyInfoTtgUsaha: Int = 1,
    var surveyIndexRumah: Int = 1,
    var surveyIndexAsset: Int = 1,
    var surveyKepemilikanAsset: Int = 1,
    var surveyPendapatanLuarUsaha: Int = 1,
    var surveyPerkembanganAsset: Int = 1,
    var surveyPerkembanganUsaha: Int = 1

    ): Parcelable {
    companion object {
        fun saveConfig(configPreferences: SharedPreferences, result: MemberAcquisitionConfig) {
            configPreferences.edit()
//                .putInt("jenis_identitas", result.jenisIdentitas)
                .putInt("no_identitas", result.noIdentitas)
                .putInt("no_hp", result.memberHandphone)
                .putInt("email", result.email)
                .putInt("nama_lengkap", result.namaLengkap)
                .putInt("tanggal_lahir", result.tanggalLahir)
                .putInt("tempat_lahir", result.tempatLahir)
                .putInt("jenis_kelamin", result.jenisKelamin)
                .putInt("nama_gadis_ibu", result.namaGadisIbu)
                .putInt("status_perkawinan", result.statusPerkawinan)
                .putInt("pendidikan_terakhir", result.pendidikanTerakhir)
                .putInt("alamat_ktp_jalan", result.alamatKtpJalan)
//                .putInt("alamat_ktp_no", result.alamatKtpNo)
//                .putInt("alamat_ktp_rt", result.alamatKtpRt)
//                .putInt("alamat_ktp_rw", result.alamatKtpRw)
                .putInt("alamat_ktp_kelurahan", result.alamatKtpKelurahan)
                .putInt("alamat_ktp_kecamatan", result.alamatKtpKecamatan)
                .putInt("alamat_ktp_kota", result.alamatKtpKota)
                .putInt("alamat_ktp_provinsi", result.alamatKtpProvinsi)
                .putInt("alamat_ktp_kode_pos", result.alamatKtpKodePos)
                .putInt("alamat_ktp_status_tempat_tinggal", result.alamatKtpStatusTempatTinggal)
                .putInt("alamat_ktp_lama_tinggal", result.alamatKtpLamaTinggal)
                .putInt("domisili_sesuai_ktp", result.domisiliSesuaiKtp)
                .putInt("alamat_domisili_jalan", result.alamatDomisiliJalan)
//                .putInt("alamat_domisili_no", result.alamatDomisiliNo)
//                .putInt("alamat_domisili_rt", result.alamatDomisiliRt)
//                .putInt("alamat_domisili_rw", result.alamatDomisiliRw)
                .putInt("alamat_domisili_kelurahan", result.alamatDomisiliKelurahan)
                .putInt("alamat_domisili_kecamatan", result.alamatDomisiliKecamatan)
//            .putInt("alamat_domisili_kota_provinsi", result.alamatDomisiliKotaProvinsi)
                .putInt("alamat_domisili_kota", result.alamatDomisiliKota)
                .putInt("alamat_domisili_provinsi", result.alamatDomisiliProvinsi)
                .putInt("alamat_domisili_kode_pos", result.alamatDomisiliKodePos)
                .putInt("alamat_domisili_status_tempat_tinggal", result.alamatDomisiliStatusTempatTinggal)
                .putInt("alamat_domisili_lama_tempat_tinggal", result.alamatDomisiliLamaTempatTinggal)
                .putInt("memiliki_npwp", result.memilikiNpwp)
                .putInt("nomer_npwp", result.nomerNpwp)
                .putInt("pekerja_usaha", result.pekerjaUsaha)
//                .putInt("bidang_pekerja", result.bidangPekerja)
//                .putInt("posisi_jabatan", result.posisiJabatan)
                .putInt("jenis_umkm", result.jenisUmkm)
                .putInt("nama_perusahaan", result.namaPerusahaan)
                .putInt("lama_bekerja", result.lamaBekerja)
                .putInt("penghasilan_omset", result.penghasilanOmset)
                .putInt("alamat_kantor_jalan", result.alamatKantorJalan)
//                .putInt("alamat_kantor_no", result.alamatKantorNo)
//                .putInt("alamat_kantor_rt", result.alamatKantorRt)
//                .putInt("alamat_kantor_rw", result.alamatKantorRw)
                .putInt("alamat_kantor_kelurahan", result.alamatKantorKelurahan)
                .putInt("alamat_kantor_kecamatan", result.alamatKantorKecamatan)
//            .putInt("alamat_kantor_kota_provinsi", result.alamatKantorKotaProvinsi)
                .putInt("alamat_kantor_kota", result.alamatKantorKota)
                .putInt("alamat_kantor_provinsi", result.alamatKantorProvinsi)
                .putInt("alamat_kantor_kode_pos", result.alamatKantorKodePos)
                .putInt("nama_emergency", result.namaEmergency)
                .putInt("no_hp_emergency", result.noHpEmergency)
                .putInt("hubungan", result.hubungan)
                .putInt("nama_pasangan", result.namaPasangan)
                .putInt("no_identitas_pasangan", result.noIdentitasPasangan)
                .putInt("pekerjaan_pasangan", result.pekerjaanPasangan)
                .putInt("no_hp_pasangan", result.noHpPasangan)
                .putInt("nama_penjamin", result.namaPenjamin)
                .putInt("no_hp_penjamin", result.noHpPenjamin)
                .putInt("hubungan_penjamin", result.hubunganPenjamin)
                .putInt("dokumen_ktp", result.dokumenKtp)
                .putInt("dokumen_sim", result.dokumenSim)
                .putInt("dokumen_kk", result.dokumenKk)
                .putInt("dokumen_keterangan_kerja", result.dokumenKetKerja)
                .putInt("dokumen_slip_gaji", result.dokumenSlipGaji)
                .putInt("dokumen_akta_nikah", result.dokumenAktaNikah)
                .putInt("dokumen_bpkb", result.dokumenBpkb)
                .putInt("dokumen_lainnya", result.dokumenLainnya)
                .putInt("survey_luas_rumah", result.surveyLuasRumah)
                .putInt("survey_jenis_atap", result.surveyJenisAtap)
                .putInt("survey_jenis_dinding", result.surveyJenisDinding)
                .putInt("survey_kondisi_rumah", result.surveyKondisiRumah)
                .putInt("survey_letak_rumah", result.surveyLetakRumah)
                .putInt("survey_tanggungan_keluarga", result.surveyTanggunganKeluarga)
                .putInt("survey_data_fisik_perabot", result.surveyDataFisikPerabot)
                .putInt("survey_akses_lembaga_keuangan", result.surveyAksesLembagaKeuangan)
                .putInt("survey_info_ttg_usaha", result.surveyInfoTtgUsaha)
                .putInt("survey_index_rumah", result.surveyIndexRumah)
                .putInt("survey_index_asset", result.surveyIndexAsset)
                .putInt("survey_kepemilikan_asset", result.surveyKepemilikanAsset)
                .putInt("survey_pendapatan_luar_usaha", result.surveyPendapatanLuarUsaha)
                .putInt("survey_perkembangan_asset", result.surveyPerkembanganAsset)
                .putInt("survey_perkembangan_usaha", result.surveyPerkembanganUsaha)
                .apply()
        }
    }
}