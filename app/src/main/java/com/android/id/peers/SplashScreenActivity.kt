package com.android.id.peers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.id.peers.auth.LoginActivity
import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.OtherFees
import com.android.id.peers.members.models.MemberAcquisitionConfig
import com.android.id.peers.util.callback.SplashScreen
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.NetworkConnectivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlin.properties.Delegates

class SplashScreenActivity : AppCompatActivity() {
    val activity = this

    var done: Int by Delegates.observable(0) { property, oldValue, newValue ->
        if(newValue == 3) {
            dot_progress_bar.stopAnimation()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

        val username = preferences.getString("email", null)
        if (username == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val networkConnectivity = NetworkConnectivity(this)
        if(networkConnectivity.isConnectedOverWifi()) {
            networkConnectivity.deleteCache()
        }

        if(networkConnectivity.isNetworkConnected()) {
            val apiConnections = ApiConnections()
            apiConnections.authenticate(preferences, this, ApiConnections.REQUEST_TYPE_GET_CONFIG,
                object :
                    SplashScreen {
                    override fun onSuccess(result: MemberAcquisitionConfig) {
                        val configPreferences = getSharedPreferences("member_config", Context.MODE_PRIVATE)
                        saveConfig(configPreferences, result)
                        done += 1
                    }

                    override fun onSuccess(result: LoanFormulaConfig) {
                        val loanPreferences = getSharedPreferences("loan_config", Context.MODE_PRIVATE)
                        saveLoanFormula(loanPreferences, result)
                        done += 1
                    }

                    override fun onSuccess(result: List<OtherFees>) {
                        val feePreferences = getSharedPreferences("fee_config", Context.MODE_PRIVATE)
                        saveOtherFees(feePreferences, result)
                        done += 1
                    }
                })
            dot_progress_bar.startAnimation()
        }
    }

    fun saveConfig(configPreferences: SharedPreferences, result: MemberAcquisitionConfig) {
        configPreferences.edit().putInt("jenis_identitas", result.jenisIdentitas)
            .putInt("no_identitas", result.noIdentitas)
            .putInt("nama_lengkap", result.namaLengkap)
//                        .putInt("no_hp", result.noHp)
            .putInt("tanggal_lahir", result.tanggalLahir)
            .putInt("jenis_kelamin", result.jenisKelamin)
            .putInt("nama_gadis_ibu", result.namaGadisIbu)
            .putInt("pendidikan_terakhir", result.pendidikanTerakhir)
            .putInt("alamat_ktp_jalan", result.alamatKtpJalan)
            .putInt("alamat_ktp_no", result.alamatKtpNo)
            .putInt("alamat_ktp_rt", result.alamatKtpRt)
            .putInt("alamat_ktp_rw", result.alamatKtpRw)
            .putInt("alamat_ktp_kelurahan", result.alamatKtpKelurahan)
//            .putInt("alamat_ktp_kecamatan", result.alamatKtpKecamatan)
            .putInt("alamat_ktp_kota", result.alamatKtpKota)
            .putInt("alamat_ktp_provinsi", result.alamatKtpProvinsi)
            .putInt("alamat_ktp_status_tempat_tinggal", result.alamatKtpStatusTempatTinggal)
            .putInt("alamat_ktp_lama_tinggal", result.alamatKtpLamaTinggal)
            .putInt("domisili_sesuai_ktp", result.domisiliSesuaiKtp)
            .putInt("alamat_domisili_jalan", result.alamatDomisiliJalan)
            .putInt("alamat_domisili_no", result.alamatDomisiliNo)
            .putInt("alamat_domisili_rt", result.alamatDomisiliRt)
            .putInt("alamat_domisili_rw", result.alamatDomisiliRw)
            .putInt("alamat_domisili_kelurahan", result.alamatDomisiliKelurahan)
            .putInt("alamat_domisili_kecamatan", result.alamatDomisiliKecamatan)
//            .putInt("alamat_domisili_kota_provinsi", result.alamatDomisiliKotaProvinsi)
            .putInt("alamat_domisili_kota", result.alamatDomisiliKota)
            .putInt("alamat_domisili_provinsi", result.alamatDomisiliProvinsi)
            .putInt("alamat_domisili_status_tempat_tinggal", result.alamatDomisiliStatusTempatTinggal)
            .putInt("alamat_domisili_lama_tempat_tinggal", result.alamatDomisiliLamaTempatTinggal)
            .putInt("memiliki_npwp", result.memilikiNpwp)
            .putInt("nomer_npwp", result.nomerNpwp)
            .putInt("pekerja_usaha", result.pekerjaUsaha)
            .putInt("bidang_pekerja", result.bidangPekerja)
            .putInt("posisi_jabatan", result.posisiJabatan)
            .putInt("nama_perusahaan", result.namaPerusahaan)
            .putInt("lama_bekerja", result.lamaBekerja)
            .putInt("penghasilan_omset", result.penghasilanOmset)
            .putInt("alamat_kantor_jalan", result.alamatKantorJalan)
            .putInt("alamat_kantor_no", result.alamatKantorNo)
            .putInt("alamat_kantor_rt", result.alamatKantorRt)
            .putInt("alamat_kantor_rw", result.alamatKantorRw)
            .putInt("alamat_kantor_kelurahan", result.alamatKantorKelurahan)
            .putInt("alamat_kantor_kecamatan", result.alamatKantorKecamatan)
//            .putInt("alamat_kantor_kota_provinsi", result.alamatKantorKotaProvinsi)
            .putInt("alamat_kantor_kota", result.alamatKantorKota)
            .putInt("alamat_kantor_provinsi", result.alamatKantorProvinsi)
            .putInt("nama_emergency", result.namaEmergency)
            .putInt("no_hp_emergency", result.noHpEmergency)
            .putInt("hubungan", result.hubungan)
            .apply()
    }

    fun saveLoanFormula(configPreferences: SharedPreferences, result: LoanFormulaConfig) {
        configPreferences.edit()
            .putString("formula_name", result.formulaName)
            .putInt("min_loan_amount", result.minLoanAmount)
            .putInt("max_loan_amount", result.maxLoanAmount)
            .putInt("kelipatan", result.kelipatan)
            .putInt("min_tenure", result.minTenure)
            .putInt("max_tenure", result.maxTenure)
            .putString("tenure_cycle", result.tenureCycle)
            .putString("service_type", result.serviceType)
            .putLong("service_amount", result.serviceAmount)
            .putString("service_cycle", result.serviceCycle)
            .apply()
    }

    fun saveOtherFees(configPreferences: SharedPreferences, result: List<OtherFees>) {
        val json = Gson().toJson(result)
        configPreferences.edit().putString("fees", json).apply()
    }
}
