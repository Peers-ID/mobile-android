package com.android.id.peers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class VerificationActivity : AppCompatActivity() {

    var noHP = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        noHP = (intent.getStringExtra("hand_phone"))!!

        val handphoneNo = findViewById<TextView>(R.id.handphone_no)
        handphoneNo.text = noHP

        val handphoneNo2 = findViewById<TextView>(R.id.handphone_no2)
        handphoneNo2.text = "+628-1312-"

        val verifikasi = findViewById<Button>(R.id.verifikasi)
        verifikasi.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finishAndRemoveTask()
            startActivity(intent)
        }
    }
}
