package com.android.id.peers

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.id.peers.anggota.StatusPinjamanAnggotaActivity
import com.android.id.peers.members.models.Member
import com.android.id.peers.pinjaman.pencairan.PencairanDetailActivity
import com.android.id.peers.pinjaman.pengajuan.Pinjaman
import com.android.id.peers.pinjaman.pengajuan.PinjamanResponse
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.callback.CitcallCallback
import com.android.id.peers.util.callback.PostPictureCallback
import com.android.id.peers.util.callback.PinjamanResponseCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_MEMBER
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_MEMBER_AND_LOAN
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_MEMBER_PICTURE
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_PUT_MEMBER_AND_LOAN
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class VerificationActivity : AppCompatActivity() {

//    var noHP = ""
    var correctPhoneNumber = ""
    var checker = ""
    var member : Member? = null
//    var loan : Loan? = null
    var pinjaman : Pinjaman? = null
    val context = this
    var isExisting = false
    var idPinjaman = 0

    var connected: Boolean = true
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

        if (intent.getParcelableExtra<Member>("member") != null) {
             member = intent.getParcelableExtra("member")!!
        }
//        noHP = (intent.getStringExtra("hand_phone"))!!
        if (intent.getParcelableExtra<Pinjaman>("pinjaman") != null) {
//            loan = (intent.getParcelableExtra("loan"))!!
            pinjaman = intent.getParcelableExtra("pinjaman")
        }

        Log.d("Verification", "NO HP : ${member!!.noHp}")

//        if (loan != null)
//            Log.d("Verification", "Loan : ${loan!!.id}")

        citCall()
        enableUlangiButton(15000)

        handphone_no.text = member!!.noHp
        handphone_no2.text = ""

        ulangi.setOnClickListener {
            citCall()
        }

        verifikasi.setOnClickListener {
            checker = "${handphone_no2.text}${handphone_edit.text}"
            if (checker == correctPhoneNumber) {

                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                connected = activeNetwork?.isConnectedOrConnecting == true

                member!!.isVerified = true

                if (connected) {
                    val mainView = this.window.decorView

                    preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    val memberPreferences = getSharedPreferences("member_mode", Context.MODE_PRIVATE)
                    val isExistingMember = memberPreferences.getBoolean("existing_member", false)

                    Log.d("VerificationActivity", "IS EXISTING : $isExistingMember")

//                    postPictures(member)

                    val intent = Intent(this, MainActivity::class.java)
                    postMemberAndPicturesAndLoan(member!!, preferences, isExistingMember)

//                    if(loan != null) {
//                        if(member != null) {
//                            val memb = member!!
//                            memb.isVerified = true
//                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                                this, ApiConnections.REQUEST_TYPE_POST_MEMBER, memb, loan = loan!!)
//                            intent.putExtra("message", "Member and loan has been successfully created")
//                            finishAffinity()
//                            startActivity(intent)
//                        } else {
//                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                                this, ApiConnections.REQUEST_TYPE_POST_LOAN, object :
//                                    LoanApplicationCallback {
//                                    override fun onSuccess(result: Boolean) {
//                                        if (result) {
//                                            intent.putExtra("message", "Loan has been successfully created")
//                                            finishAffinity()
//                                            startActivity(intent)
//                                        }
//                                    }
//                                }, loan = loan!!)
//                        }
//                    } else {
//                        if(member != null) {
//                            val memb = member!!
//                            memb.isVerified = true
//                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                                this, ApiConnections.REQUEST_TYPE_POST_MEMBER, memb)
//                            intent.putExtra("message", "Member has been successfully created")
//                            finishAffinity()
//                            startActivity(intent)
//                        }
//                    }
                    Log.d("LoanApplication", "Network is connected")
                }
            } else {
                PeersSnackbar.popUpSnack(context.window.decorView, "Last 4-digit number is invalid")
            }
        }
    }

    private fun responseHandling(response: PinjamanResponse) {
        var message = ""
        var code = 1001
        /* Pop Up Alert Window */
        if (response.codePengajuan == 1001 && response.codePencairan == 1001) {
            message = response.descPencairan
            code = response.codePencairan
        } else if (response.codePengajuan == 1002) {
            message = response.descPengajuan
            code = response.codePengajuan
        } else {
            message = response.descPencairan
            code = response.codePencairan
        }
        showAlertDialog(message, code)
        idPinjaman = response.idLoan
    }

    private fun showAlertDialog(message: String, code: Int) {
        val builder = AlertDialog.Builder(this)

        if (code == 1001) {
            with(builder)
            {
                setTitle("Konfirmasi")
                setMessage(message)
                setPositiveButton("Ya", positiveButtonClick)
                setNegativeButton("Tidak", negativeButtonClick)
                show()
            }
        } else {
            with(builder)
            {
                setTitle("Konfirmasi")
                setMessage(message)
                setNeutralButton("Ya", neutralButtonClick)
                show()
            }
        }
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(this, PencairanDetailActivity::class.java)
        intent.putExtra("id_pinjaman", idPinjaman)
        startActivity(intent)
        finishAffinity()
    }

    private val negativeButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(this, StatusPinjamanAnggotaActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private val neutralButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(this, StatusPinjamanAnggotaActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun citCall() {

        if (connected) {
            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_POST_CITCALL, object : CitcallCallback {
                    override fun onSuccess(result: String) {
                        correctPhoneNumber = result
                        val hiddenPhone = result.removeRange(result.length - 4, result.length)
                        handphone_no2.text = hiddenPhone
                    }
                }, memberPhone = member!!.noHp)
        } /*else {
                val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
                    OfflineViewModel::class.java)
                offlineViewModel.insertCollection(noHP)
            }*/

    }
    private fun enableUlangiButton(milis: Long) {
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(milis)
            ulangi.isEnabled = true
            ulangi.visibility = View.VISIBLE
        }
    }

    private fun postPictures(member: Member) {
        val preferences = getSharedPreferences("login_date", Context.MODE_PRIVATE)
        if (member.dokumenKtp.isNotEmpty()) {
            authenticate(preferences, this, REQUEST_TYPE_POST_MEMBER)
        }
    }

    private fun postMemberAndPicturesAndLoan(member : Member, preferences : SharedPreferences, isExisting : Boolean) {
        if (member.dokumenKtp.isNotEmpty()) {
            threshold++
            authenticate(preferences, this, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenKtp = fileName
                    done += 1
                    Log.d("Verification", "DONE VALUE KTP : $done")
                }

            }, imageData = Base64.decode(member.dokumenKtpByteArrayString, Base64.DEFAULT), fileName = member.dokumenKtp)
        }
        if (member.dokumenSim.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenSim = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenSimByteArrayString, Base64.DEFAULT), fileName = member.dokumenSim)
        }
        if (member.dokumenKk.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenKk = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenKkByteArrayString, Base64.DEFAULT), fileName = member.dokumenKk)
        }
        if (member.dokumenBpkb.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenBpkb = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenBpkbByteArrayString, Base64.DEFAULT), fileName = member.dokumenBpkb)
        }
        if (member.dokumenAktaNikah.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenAktaNikah = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenAktaNikahByteArrayString, Base64.DEFAULT), fileName = member.dokumenAktaNikah)
        }
        if (member.dokumenSlipGaji.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenSlipGaji = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenSlipGajiByteArrayString, Base64.DEFAULT), fileName = member.dokumenSlipGaji)
        }
        if (member.dokumenKetKerja.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenKetKerja = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenKetKerjaByteArrayString, Base64.DEFAULT), fileName = member.dokumenKetKerja)
        }
        if (member.dokumenLainnya.isNotEmpty()) {
            threshold++
            authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
                override fun onPictureUploaded(fileName: String) {
                    member.dokumenLainnya = fileName
                    done++
                    Log.d("Verification", "DONE VALUE : $done")
                }

            }, imageData = Base64.decode(member.dokumenLainnyaByteArrayString, Base64.DEFAULT), fileName = member.dokumenLainnya)
        }
        Log.d("Verification", "THRESHOLD VALUE : $threshold")
    }

    private var threshold = 0

    var done: Int by Delegates.observable(0) { _, _, newValue ->
        if (newValue == threshold)
            if (isExisting)
                authenticate(preferences, context, REQUEST_TYPE_PUT_MEMBER_AND_LOAN, object : PinjamanResponseCallback {
                    override fun onSuccess(result: PinjamanResponse) {
                        responseHandling(result)
                    }

                }, member = member, pinjaman = pinjaman)
            else
                authenticate(preferences, context, REQUEST_TYPE_POST_MEMBER_AND_LOAN, object : PinjamanResponseCallback {
                    override fun onSuccess(result: PinjamanResponse) {
                        responseHandling(result)
                    }

                }, member = member, pinjaman = pinjaman)
    }
}
