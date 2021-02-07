package com.android.id.peers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.auth.LoginActivity
import com.android.id.peers.members.models.*
import com.android.id.peers.members.models.MemberAcquisitionConfig.Companion.saveConfig
import com.android.id.peers.pinjaman.pengajuan.ParameterKoperasi
import com.android.id.peers.util.callback.*
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
//import com.android.id.peers.util.connection.ApiConnections.Companion.getKabupaten
//import com.android.id.peers.util.connection.ApiConnections.Companion.getKecamatan
//import com.android.id.peers.util.connection.ApiConnections.Companion.getProvince
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.connection.NetworkConnectivity
import com.android.id.peers.util.database.OfflineViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class SplashScreenActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var offlineViewModel : OfflineViewModel
    private lateinit var mProvince : List<Province>
    private lateinit var mKabupaten : List<Kabupaten>
    private lateinit var mKecamatan : List<Kecamatan>
    private lateinit var mDesa : List<Desa>
    val viewModelStoreOwner = this
    val activity = this
    var skipping : Boolean = true //If the login went successfully through the splash screen to main activity, the region data download from api will be skipped
    val threshold : Int = 1
    private val coroutineScope = this

    var connected: Boolean = true

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext[Job]!!.cancel()
    }

    var done: Int by Delegates.observable(0) { property, oldValue, newValue ->
        Log.d("SplashScreen", "done Value : $done")
        if (skipping) {
            if(newValue == 2) {
                dot_progress_bar.stopAnimation()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            if(newValue == (2 + threshold)) {
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

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

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
        offlineViewModel.allDesa.observe(this, Observer {desa ->
            mDesa = desa
            Log.d("SplashScreen", "Desa Observed: ${mDesa.size}")
        })

        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val paramPreferences = getSharedPreferences("parameter_koperasi", Context.MODE_PRIVATE)

        val username = preferences.getString("email", null)
        if (username == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        if(NetworkConnectivity.isConnectedOverWifi(this)) {
            NetworkConnectivity.deleteCache(this)
        }

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        if (connected) {
            Log.d("SplashScreen", "Network is connected")
//            authenticate(preferences, this, ApiConnections.REQUEST_TYPE_GET_CONFIG, object: SplashScreen {
//
//            })
            authenticate(preferences, this, ApiConnections.REQUEST_TYPE_GET_CONFIG,
                object :
                    SplashScreen {
                    override fun onSuccess(result: MemberAcquisitionConfig) {
                        val configPreferences = getSharedPreferences("member_config", Context.MODE_PRIVATE)
                        saveConfig(configPreferences, result)
                        done += 1
                    }

//                    override fun onSuccess2(result: LoanFormulaConfig) {
//                        val loanPreferences = getSharedPreferences("loan_config", Context.MODE_PRIVATE)
//                        saveLoanFormula(loanPreferences, result)
//                        done += 1
//                    }
//
//                    override fun onSuccess3(result: List<OtherFees>) {
//                        val feePreferences = getSharedPreferences("fee_config", Context.MODE_PRIVATE)
//                        saveOtherFees(feePreferences, result)
//                        done += 1
//                    }
                })

            authenticate(preferences, this, ApiConnections.REQUEST_TYPE_GET_PARAMETER_KOPERASI,
                object : ParameterCallback {
                    override fun onSuccess(result: ParameterKoperasi) {
                        paramPreferences.edit()
                            .putInt("hari_per_bulan", result.hariPerBulan)
                            .putInt("id_angsuran_sebagian", result.idAngsuranSebagian)
                            .putInt("masa_tenggang", result.masaTenggang)
                            .putString("type_denda_keterlambatan", result.typeDendaKeterlambatan)
                            .putInt("id_dasar_denda", result.idDasarDenda)
                            .putString("type_pelunasan_dipercepat", result.typePelunasanDipercepat)
                            .putInt("id_dasar_pelunasan", result.idDasarPelunasan)
                            .putString("id_urutan_simpanan", result.idUrutanSimpanan)
                            .apply()
                        done += 1
                    }

                })

//            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
//                    LoanDisbursement {
//                    override fun onSuccess(result: List<Loan>) {
//                        val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)
//                        saveLoans(loanPreferences, result, "loan_disburse")
//                        done += 1
//                    }
//                }
//                , listType = 3)
//
//            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
//                    LoanDisbursement {
//                    override fun onSuccess(result: List<Loan>) {
//                        val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)
//                        saveLoans(loanPreferences, result, "repayment_collection")
//                        done += 1
//                    }
//                }
//                , listType = 1)

            val regionPreferences = getSharedPreferences("regions", Context.MODE_PRIVATE)
//                Log.d("SplashScreen", "Is filled: ${regionPreferences.getBoolean("is_filled", false)}")
            if(!regionPreferences.getBoolean("is_filled", false)) {
                skipping = false
//                runBlocking {
//                    ApiConnections.getWilayah(activity, activity, wCallback)
//                    done +=1
//                }
                coroutineScope.async {
                    ApiConnections.getWilayah(activity, activity, wCallback)
                    done +=1
                }

//                getProvince(context = this, callback = pCallback)
//                    regionPreferences.edit().putBoolean("is_filled", true).apply()
            }
            dot_progress_bar.startAnimation()
        } else {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("message", "There was no internet access!")
            startActivity(intent)
            finish()
        }
    }

    val wCallback = object : WilayahCallback {
        override fun onSuccess(result: Boolean) {
            if(result) {
                done += 1
            }
        }

    }

//    val pCallback = object : ProvinceCallback {
//        override fun onSuccess(result: List<Province>) {
//            threshold = result.size
//            coroutineScope.launch(Dispatchers.Main) {
//                offlineViewModel.insertProvince(result)
//            }
//
//            val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
//            val apiToken = preferences.getString("raja_api_token", "")
//            for (province in result) {
//                val apiConnections = ApiConnections()
//                getKabupaten(context = activity, callback = bCallback, idProvinsi = province.kodeWilayah/*, apiToken = apiToken!!*/)
//                done += 1
//            }
//        }
//    }
//
//    val bCallback = object : KabupatenCallback {
//        override fun onSuccess(result: List<Kabupaten>) {
//            threshold += result.size
//            coroutineScope.launch(Dispatchers.Main) {
//                offlineViewModel.insertKabupaten(result)
//            }
////            val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
////            val apiToken = preferences.getString("raja_api_token", "")
//            for (kabupaten in result) {
//                val apiConnections = ApiConnections()
//                getKecamatan(activity, cCallback, kabupaten.kodeWilayah/*, apiToken!!*/)
//                done += 1
//            }
//        }
//    }
//
//    val cCallback = object : KecamatanCallback {
//        override fun onSuccess(result: List<Kecamatan>) {
//            threshold += result.size
//            offlineViewModel.insertKecamatan(result)
//            for (kecamatan in result) {
//                done += 1
//            }
//        }
//
//    }
}
