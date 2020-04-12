package com.android.id.peers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}
