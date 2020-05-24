package com.android.id.peers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.callback.CitcallCallback
import com.android.id.peers.util.callback.LoanApplicationCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.connection.NetworkConnectivity
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerificationActivity : AppCompatActivity() {

    var noHP = ""
    var correctPhoneNumber = ""
    var checker = ""
    var member : Member? = null
    var loan : Loan? = null
    val context = this

    var connected: Boolean = true

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
        noHP = (intent.getStringExtra("hand_phone"))!!
        if (intent.getParcelableExtra<Member>("loan") != null) {
            loan = (intent.getParcelableExtra("loan"))!!
        }

        citCall()
        enableUlangiButton(15000)

        handphone_no.text = noHP
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

                if (connected) {
                    val mainView = this.window.decorView
                    val intent = Intent(this, MainActivity::class.java)
                    if(loan != null) {
                        if(member != null) {
                            val memb = member!!
                            memb.isVerified = true
                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                                this, ApiConnections.REQUEST_TYPE_POST_MEMBER, memb, loan = loan!!)
                            intent.putExtra("message", "Member and loan has been successfully created")
                            finishAffinity()
                            startActivity(intent)
                        } else {
                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                                this, ApiConnections.REQUEST_TYPE_POST_LOAN, object :
                                    LoanApplicationCallback {
                                    override fun onSuccess(result: Boolean) {
                                        if (result) {
                                            intent.putExtra("message", "Loan has been successfully created")
                                            finishAffinity()
                                            startActivity(intent)
                                        } else {
                                            PeersSnackbar.popUpSnack(mainView, "Already have on progress, approved or pending Loan!")
                                        }
                                    }
                                }, loan = loan!!)
                        }
                    } else {
                        if(member != null) {
                            val memb = member!!
                            memb.isVerified = true
                            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                                this, ApiConnections.REQUEST_TYPE_POST_MEMBER, memb)
                            intent.putExtra("message", "Member has been successfully created")
                            finishAffinity()
                            startActivity(intent)
                        }
                    }
                    Log.d("LoanApplication", "Network is connected")
                }
            } else {
                PeersSnackbar.popUpSnack(context.window.decorView, "Last 4-digit number is invalid")
            }
        }
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
                }, memberPhone = noHP)
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
}
