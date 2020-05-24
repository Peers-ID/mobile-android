package com.android.id.peers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_transition.*

class TransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        simpan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}
