package com.android.id.peers.pinjaman.data

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.android.id.peers.R
import com.android.id.peers.pinjaman.MenuPinjamanActivity

class DataAnggotaActivity : AppCompatActivity() {

    override fun onBackPressed() {
//        super.onBackPressed()
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Konfirmasi")
            setMessage("Apakah Anda yakin untuk membatalkan proses pendaftaran?")
            setPositiveButton("Ya", positiveButtonClick)
            setNegativeButton("Tidak", negativeButtonClick)
            show()
        }
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
//        val intent = Intent(this, MenuPinjamanActivity::class.java)
//        startActivity(intent)
        finish()
    }

    private val negativeButtonClick = { _: DialogInterface, _: Int ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_anggota)
    }
}