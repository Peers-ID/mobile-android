package com.android.id.peers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.auth.LoginActivity
import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.LoanFormulaConfig.Companion.saveLoanFormula
import com.android.id.peers.loans.models.OtherFees
import com.android.id.peers.loans.models.OtherFees.Companion.saveOtherFees
import com.android.id.peers.members.models.*
import com.android.id.peers.members.models.MemberAcquisitionConfig.Companion.saveConfig
import com.android.id.peers.util.callback.KabupatenCallback
import com.android.id.peers.util.callback.KecamatanCallback
import com.android.id.peers.util.callback.ProvinceCallback
import com.android.id.peers.util.callback.SplashScreen
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.connection.NetworkConnectivity
import com.android.id.peers.util.database.OfflineViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlin.properties.Delegates

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var offlineViewModel : OfflineViewModel
    private lateinit var mProvince : List<Province>
    private lateinit var mKabupaten : List<Kabupaten>
    private lateinit var mKecamatan : List<Kecamatan>
    private lateinit var mDesa : List<Desa>
    val viewModelStoreOwner = this
    val activity = this
    var skipping : Boolean = true
    var threshold : Int = 9999

    var done: Int by Delegates.observable(0) { property, oldValue, newValue ->
//        Log.d("SplashScreen", "done Value : $done")
        if (skipping) {
            if(newValue == 3) {
                dot_progress_bar.stopAnimation()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            if(newValue == (3 + threshold)) {
                dot_progress_bar.stopAnimation()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mProvince = ArrayList()
        mKabupaten = ArrayList()
        mKecamatan = ArrayList()
        mDesa = ArrayList()

        offlineViewModel = ViewModelProvider(this).get(OfflineViewModel::class.java)
        offlineViewModel.allProvince.observe(this, Observer {provinces ->
            mProvince = provinces
            Log.d("SplashScreen", "Province Observed: ${mProvince.size}")
        })
        offlineViewModel.allKabupaten.observe(this, Observer {kabupaten ->
            mKabupaten = kabupaten
            Log.d("SplashScreen", "Kabupaten Observed: ${mKabupaten.size}")
        })
        offlineViewModel.allKecamatan.observe(this, Observer {kecamatan ->
            mKecamatan = kecamatan
            Log.d("SplashScreen", "Kecamatan Observed: ${mKecamatan.size}")
        })
//        offlineViewModel.allDesa.observe(this, Observer {desa ->
//            mDesa = desa
//            Log.d("SplashScreen", "Desa Observed: ${mDesa.size}")
//        })

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

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer {isConnected ->
            if (isConnected) {
                Log.d("SplashScreen", "Network is connected")
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
                val regionPreferences = getSharedPreferences("regions", Context.MODE_PRIVATE)
                Log.d("SplashScreen", "Is filled: ${regionPreferences.getBoolean("is_filled", false)}")
                if(!regionPreferences.getBoolean("is_filled", false)) {
                    skipping = false
                    apiConnections.getProvince(context = this, callback = pCallback)
                    regionPreferences.edit().putBoolean("is_filled", true).apply()
                    Log.d("SplashScreen", "HERE")
                }
                dot_progress_bar.startAnimation()
            } else {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                finish()
                Log.d("SplashScreen", "Network is not connected")
            }
        })
    }

    val pCallback = object : ProvinceCallback {
        override fun onSuccess(result: List<Province>) {
            threshold = result.size
            offlineViewModel.insertProvince(result)
            for (province in result) {
                val apiConnections = ApiConnections()
                apiConnections.getKabupaten(context = activity, callback = bCallback, idProvinsi = province.id)
                done += 1
            }
        }
    }

    val bCallback = object : KabupatenCallback {
        override fun onSuccess(result: List<Kabupaten>) {
            threshold += result.size
            offlineViewModel.insertKabupaten(result)
            for (kabupaten in result) {
                val apiConnections = ApiConnections()
                apiConnections.getKecamatan(activity, cCallback, kabupaten.id)
                done += 1
            }
        }
    }

    val cCallback = object : KecamatanCallback {
        override fun onSuccess(result: List<Kecamatan>) {
            threshold += result.size
            offlineViewModel.insertKecamatan(result)
            for (kecamatan in result) {
                done += 1
            }
        }

    }
}
