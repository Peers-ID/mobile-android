package com.android.id.peers.pembayaran

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.callback.CicilanCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_PEMBAYARAN_CICILAN
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.NetworkConnectivity
import kotlinx.android.synthetic.main.activity_pembayaran_detail.*
import kotlinx.android.synthetic.main.activity_transition.*
import kotlinx.android.synthetic.main.layout_empty_data.*

class PembayaranDetailActivity : AppCompatActivity() {
    var idMember = 0
    var angsuranKe = 0
    var pembayaranCicilan = Cicilan()
    var cicilanBayar = Cicilan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran_detail)

        title = "Penagihan"

        idMember = intent.getIntExtra("id_member", 0)
        angsuranKe = intent.getIntExtra("angsuran", 0)
        Log.d("PencairanDetail", "ID PINJAMAN : $idMember")

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        NetworkConnectivity.connected = activeNetwork?.isConnectedOrConnecting == true

        reload.setOnClickListener {
            error_container.visibility = View.GONE
            loadData()
        }

        if (NetworkConnectivity.connected) {
            loadData()
        }

        var textBefore = ""
        simpanan_sukarela.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = CurrencyFormat.getNumberOfDigits(textBefore.substring(initialCursorPosition,
                    textBefore.length))

                val newAmount = CurrencyFormat.formatAmount(s.toString())
                simpanan_sukarela.removeTextChangedListener(this)
                simpanan_sukarela.setText(newAmount)
                //set new cursor position
                simpanan_sukarela.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                simpanan_sukarela.addTextChangedListener(this)
            }

        })

        var textBefore2 = ""
        jumlah_setoran.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore2 = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = CurrencyFormat.getNumberOfDigits(textBefore2.substring(initialCursorPosition,
                    textBefore2.length))

                val newAmount = CurrencyFormat.formatAmount(s.toString())
                jumlah_setoran.removeTextChangedListener(this)
                jumlah_setoran.setText(newAmount)
                //set new cursor position
                jumlah_setoran.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                jumlah_setoran.addTextChangedListener(this)
            }

        })

        bayar.setOnClickListener {
            val cicilanBayar = calculateDetailPembayaran(pembayaranCicilan, 0)
            Log.d("PembayaranDetail", "JUMLAH SETORAN : ${cicilanBayar.jumlahSetoran}")
            Log.d("PembayaranDetail","POKOK "+cicilanBayar.pokok.toString())
            if (validateInput()) showDialog(cicilanBayar)
        }

        bayar_dengan_simpanan.setOnClickListener {
            val cicilanBayar = calculateDetailPembayaran(pembayaranCicilan, 1)
            if (validateInput()) showDialog(cicilanBayar)
        }
    }

    private fun loadData() {
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmerAnimation()
        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

        authenticate(
            preferences,
            this,
            ApiConnections.REQUEST_TYPE_GET_DETAIL_CICILAN,
            object : CicilanCallback {
                override fun onSuccess(result: Cicilan) {
                    pembayaranCicilan = result
                    nama_anggota.text = result.namaAnggota
                    nama_product.text = result.namaProduct
//                    sisa_pinjaman.text = CurrencyFormat.formatRupiah.format(result.sisaPinjaman)
                    tanggal.text = result.jatuhTempo
                    angsuran.text = CurrencyFormat.formatRupiah.format(result.pokok + result.bunga)
                    simpanan_wajib.text = CurrencyFormat.formatRupiah.format(result.simpananWajib)
                    jpk.text = CurrencyFormat.formatRupiah.format(result.jpk)
                    denda.text = CurrencyFormat.formatRupiah.format(result.denda)
                    total_pembayaran.text = CurrencyFormat.formatRupiah.format(result.totalPembayaran)
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmerAnimation()
                    if (pembayaranCicilan.idAnggota == 0) {
                        pembayaran_detail_container.visibility = View.GONE
                        error_container.visibility = View.VISIBLE
                    } else {
                        pembayaran_detail_container.visibility = View.VISIBLE
                        error_container.visibility = View.GONE
                    }
                }
            },
            memberId = idMember, angsuran = angsuranKe
        )
    }

    private fun validateInput() : Boolean {
        return if (jumlah_setoran.text.toString().isEmpty() || jumlah_setoran.text.toString() == "0" || jumlah_setoran.text.toString() == "Rp0") {
            jumlah_setoran.error = "Jumlah Setoran tidak boleh kosong"
            false
        } else {
            true
        }
    }

    private fun showDialog(cicilanBayar: Cicilan) {
        val dialogView = layoutInflater.inflate(R.layout.layout_dialog_pembayaran, null)
        dialogView.findViewById<TextView>(R.id.angsuran_pokok)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.pokok)
        dialogView.findViewById<TextView>(R.id.bunga)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.bunga)
        dialogView.findViewById<TextView>(R.id.denda2)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.denda)
        dialogView.findViewById<TextView>(R.id.simpanan_wajib2)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.simpananWajib)
        dialogView.findViewById<TextView>(R.id.jpk)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.jpk)
        dialogView.findViewById<TextView>(R.id.simpanan_sukarela2)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.simpananSukarela)
        dialogView.findViewById<TextView>(R.id.total_pembayaran2)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.totalPembayaran)
        dialogView.findViewById<TextView>(R.id.jumlah_setoran2)?.text = CurrencyFormat.formatRupiah.format(cicilanBayar.jumlahSetoran)
        val builder = AlertDialog.Builder(this)
        this.cicilanBayar = cicilanBayar
        builder.setTitle("Konfirmasi Pembayaran Pinjaman")
            .setView(dialogView)
            .setPositiveButton("Ya", positiveButtonClick)
            .setNegativeButton("Tidak", negativeButtonClick)
            .show()
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        authenticate(preferences, this, REQUEST_TYPE_POST_PEMBAYARAN_CICILAN, null, cicilan = cicilanBayar)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private val negativeButtonClick = { _: DialogInterface, _: Int ->
    }

    private fun calculateDetailPembayaran(cicilan: Cicilan, bayarDenganSimpanan: Int) : Cicilan {
        cicilan.jumlahSetoran = CurrencyFormat.removeCurrencyFormat(jumlah_setoran.text.toString()).toLong()
        cicilan.totalPembayaran = cicilan.pokok + cicilan.bunga + cicilan.simpananWajib + cicilan.denda + cicilan.jpk

        val cicilanBayar = bayar(cicilan)
        cicilanBayar.idAnggota = cicilan.idAnggota
        cicilanBayar.idLoan = cicilan.idLoan
        cicilanBayar.angsuran = cicilan.angsuran
        cicilanBayar.pembayaranKe = cicilan.pembayaranKe
        cicilanBayar.totalPembayaran = cicilan.totalPembayaran
        cicilanBayar.simpananWajib = cicilan.simpananWajib
        cicilanBayar.jpk = cicilan.jpk
        cicilanBayar.pokok = cicilan.pokok
        val simpananSukarela = CurrencyFormat.removeCurrencyFormat(simpanan_sukarela.text.toString()).toLong()
        cicilanBayar.simpananSukarela = cicilanBayar.simpananSukarela + simpananSukarela
        cicilanBayar.bayarDenganSimpanan = bayarDenganSimpanan

        Log.d("calculateDetail", "SUKARELA : ${cicilanBayar.simpananSukarela}")
        Log.d("totalPembayaran",cicilan.totalPembayaran.toString())

        return cicilanBayar
    }

    private fun bayar(cicilan: Cicilan) : Cicilan {
        val paramPreferences = getSharedPreferences("parameter_koperasi", Context.MODE_PRIVATE)
        val idAngsuranSebagian = paramPreferences.getInt("id_angsuran_sebagian", 0)
        Log.d("bayar", "ID ANGSURAN SEBAGIAN : $idAngsuranSebagian")
        val cicilanBayar = Cicilan()
        cicilanBayar.jumlahSetoran = cicilan.jumlahSetoran

        when (idAngsuranSebagian) {
            1 -> {
                when {
                    cicilanBayar.jumlahSetoran < cicilan.pokok -> {
                        cicilanBayar.pokok = cicilan.jumlahSetoran
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.jumlahSetoran - cicilan.pokok
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda + cicilan.simpananWajib -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda + cicilan.simpananWajib + cicilan.jpk -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda - cicilan.jpk
                    }
                    else -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananWajib = cicilan.simpananWajib
                        cicilanBayar.jpk = cicilan.jpk
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda - cicilan.simpananWajib - cicilan.jpk
                    }
                }
            }
            2 -> {
                when {
                    cicilanBayar.jumlahSetoran < cicilan.bunga -> {
                        cicilanBayar.bunga = cicilan.jumlahSetoran
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok -> {
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.pokok = cicilan.jumlahSetoran - cicilan.bunga
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda + cicilan.simpananWajib -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda
                    }
                    cicilanBayar.jumlahSetoran < cicilan.bunga + cicilan.pokok + cicilan.denda + cicilan.simpananWajib + cicilan.jpk -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda - cicilan.jpk
                    }
                    else -> {
                        cicilanBayar.pokok = cicilan.pokok
                        cicilanBayar.bunga = cicilan.bunga
                        cicilanBayar.denda = cicilan.denda
                        cicilanBayar.simpananWajib = cicilan.simpananWajib
                        cicilanBayar.jpk = cicilan.jpk
                        cicilanBayar.simpananSukarela = cicilan.jumlahSetoran - cicilan.pokok - cicilan.bunga - cicilan.denda - cicilan.simpananWajib - cicilan.jpk
                    }
                }
            }
        }
        return cicilanBayar
    }
}