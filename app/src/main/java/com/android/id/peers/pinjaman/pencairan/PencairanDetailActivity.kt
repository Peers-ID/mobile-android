package com.android.id.peers.pinjaman.pencairan

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.anggota.StatusPinjamanAnggotaActivity
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.callback.PencairanCallback
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_GET_DETAIL_PENCAIRAN
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.NetworkConnectivity.Companion.connected
import kotlinx.android.synthetic.main.activity_pencairan_detail.*
import kotlinx.android.synthetic.main.activity_pencairan_detail.admin
import kotlinx.android.synthetic.main.activity_pencairan_detail.asuransi
import kotlinx.android.synthetic.main.activity_pencairan_detail.cicilan
import kotlinx.android.synthetic.main.activity_pencairan_detail.jpk
import kotlinx.android.synthetic.main.activity_pencairan_detail.jumlah_pencairan
import kotlinx.android.synthetic.main.activity_pencairan_detail.jumlah_pinjaman
import kotlinx.android.synthetic.main.activity_pencairan_detail.nama_product
import kotlinx.android.synthetic.main.activity_pencairan_detail.simpanan_pokok
import kotlinx.android.synthetic.main.activity_pencairan_detail.tenor
import java.util.*

class PencairanDetailActivity : AppCompatActivity() {
    var idMember = 0
    var idPinjaman = 0
    var noKtp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencairan_detail)

        title = "Konfirmasi Pencairan"

        idPinjaman = intent.getIntExtra("id_pinjaman", 0)
        Log.d("PencairanDetail", "ID PINJAMAN : $idPinjaman")

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

        if (connected) {
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmerAnimation()
            authenticate(preferences, this, REQUEST_TYPE_GET_DETAIL_PENCAIRAN, object : PencairanCallback {
                override fun onSuccess(result: PencairanPinjaman) {
                    nama_anggota.text = result.namaAnggota
                    no_ktp.text = result.noKtp
                    tanggal_pengajuan.text = result.tanggalPengajuan
                    nama_product.text = result.namaProduct
                    jumlah_pinjaman.text = CurrencyFormat.formatRupiah.format(result.jumlahPinjaman)
                    simpanan_pokok.text = CurrencyFormat.formatRupiah.format(result.simpananPokok)
                    admin.text = CurrencyFormat.formatRupiah.format(result.biayaAdmin)
                    Log.d("PencairanDetail", "BIAYA ADMIN : ${result.biayaAdmin}")
                    jumlah_pencairan.text = CurrencyFormat.formatRupiah.format(result.jumlahPencairan)
                    val cicilanText = "${CurrencyFormat.formatRupiah.format(result.cicilan)} / ${result.satuanTenor}"
                    cicilan.text = cicilanText

                    asuransi.text = CurrencyFormat.formatRupiah.format(result.asuransi)
                    jpk.text = CurrencyFormat.formatRupiah.format(result.jpk)
                    provisi.text = CurrencyFormat.formatRupiah.format(result.provisi)

                    val tenorText = "${result.tenor} ${result.satuanTenor}"
                    tenor.text = tenorText
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmerAnimation()
                    pencairan_detail_container.visibility = View.VISIBLE
                    idMember = result.idMember
                    noKtp = result.noKtp
                }
            }, loanId = idPinjaman)
        }

        konfirmasi.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            with(builder)
            {
                setTitle("Konfirmasi")
                setMessage("Apakah anda yakin untuk\nmelanjutkan pencairan pinjaman?")
                setPositiveButton("Ya", positiveButtonClick)
                setNegativeButton("Tidak", negativeButtonClick)
                show()
            }
        }
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val intent = Intent(this, PencairanFotoActivity::class.java)
        intent.putExtra("id_member", idMember)
        intent.putExtra("id_pinjaman", idPinjaman)
        intent.putExtra("no_ktp", noKtp)
        startActivity(intent)
    }

    private val negativeButtonClick = { _: DialogInterface, _: Int ->
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}