package com.android.id.peers.pinjaman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.id.peers.LoadingActivity
import com.android.id.peers.R
import com.android.id.peers.pinjaman.data.DataAnggotaActivity
import com.android.id.peers.pinjaman.pencairan.PencairanActivity
import kotlinx.android.synthetic.main.activity_menu_pinjaman.*

class MenuPinjamanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_pinjaman)

        daftar.setOnClickListener {
            val intent = Intent(this, DataAnggotaActivity::class.java)
//            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

        pencairan.setOnClickListener {
            val intent = Intent(this, PencairanActivity::class.java)
            startActivity(intent)
        }
    }
}