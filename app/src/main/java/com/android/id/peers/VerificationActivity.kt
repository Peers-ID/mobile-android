package com.android.id.peers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.database.OfflineViewModel
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {

    var noHP = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        noHP = (intent.getStringExtra("hand_phone"))!!

        handphone_no.text = noHP
        handphone_no2.text = "+628-1312-"

        verifikasi.setOnClickListener {
            val connectionStateMonitor = ConnectionStateMonitor(application)
            connectionStateMonitor.observe(this, Observer { isConnected ->
                if (isConnected) {
                    val apiConnections = ApiConnections()
                    apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                        this, ApiConnections.REQUEST_TYPE_POST_CITCALL, noHP)
                } /*else {
                    val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
                        OfflineViewModel::class.java)
                    offlineViewModel.insertCollection(noHP)
                }*/
            })
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}
