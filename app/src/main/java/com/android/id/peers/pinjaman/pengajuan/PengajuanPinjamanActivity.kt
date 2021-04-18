package com.android.id.peers.pinjaman.pengajuan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.callback.ProductCallback
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_GET_ACTIVE_PRODUCTS
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_pengajuan_pinjaman.*
import java.util.*
import kotlin.collections.ArrayList

class PengajuanPinjamanActivity : AppCompatActivity() {
    private var products = ArrayList<Product>()
    private var member = Member()
    private var selectedProduct = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengajuan_pinjaman)

        member = intent.getParcelableExtra<Member>("member")!!

        Log.d("PengajuanPinjaman","selected kelompok : ${member!!.selectedKelompok}")

        Log.d("PengajuanPinjaman", "NO HP : ${member!!.noHp}")

        title = "Pengajuan Pinjaman"
        val productsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, products)

        product.adapter = productsAdapter

        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        authenticate(preferences, this, REQUEST_TYPE_GET_ACTIVE_PRODUCTS, object: ProductCallback {
            override fun onSuccess(result: List<Product>) {
                productsAdapter.clear()
                productsAdapter.addAll(result)
                productsAdapter.notifyDataSetChanged()
                progress_bar.visibility = View.GONE
                pengajuan_container.visibility = View.VISIBLE
            }

        })

        var textBefore = ""
        jumlah_pinjaman.addTextChangedListener(object: TextWatcher {
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
                jumlah_pinjaman.removeTextChangedListener(this)
                jumlah_pinjaman.setText(newAmount)
                //set new cursor position
                jumlah_pinjaman.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                jumlah_pinjaman.addTextChangedListener(this)
            }

        })

        product.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedProduct = products[position]
                val tenorText = "${selectedProduct.tenor} ${selectedProduct.satuanTenor}"
                Log.d("PengajuanPinjaman", "Tenor Text : $tenorText")
                tenor.text = tenorText
                val bungaText = "${selectedProduct.bunga}% / ${selectedProduct.tenorBunga}"
                bunga.text = bungaText
                val adminText = if (selectedProduct.typeAdmin.toLowerCase(Locale.ROOT) == "fix") CurrencyFormat.formatRupiah.format(selectedProduct.admin) else "${selectedProduct.admin}%"
                admin.text = adminText

                val provisiText = if (selectedProduct.typeProvisi.toLowerCase(Locale.ROOT) == "fix") CurrencyFormat.formatRupiah.format(selectedProduct.provisi) else "${selectedProduct.provisi}%"
                provisi.text = provisiText

                val asuransiText = if (selectedProduct.typeAsuransi.toLowerCase(Locale.ROOT) == "fix") CurrencyFormat.formatRupiah.format(selectedProduct.asuransi) else "${selectedProduct.asuransi}%"
                asuransi.text = asuransiText

                val jpkText = if (selectedProduct.typeJpk.toLowerCase(Locale.ROOT) == "fix") CurrencyFormat.formatRupiah.format(selectedProduct.jpk) else "${selectedProduct.jpk}%"
                jpk.text = jpkText

                simpanan_pokok.text = CurrencyFormat.formatRupiah.format(selectedProduct.simpananPokok)
                simpanan_wajib.text = CurrencyFormat.formatRupiah.format(selectedProduct.simpananWajib)
                val dendaKeterlambatanText = if (selectedProduct.typeDendaKeterlambatan.toLowerCase(Locale.ROOT) == "fix") CurrencyFormat.formatRupiah.format(selectedProduct.dendaKeterlambatan) else "${selectedProduct.dendaKeterlambatan}%"
                denda_keterlambatan.text = dendaKeterlambatanText
                val dendaPelunasanDipercepatText = "${selectedProduct.pelunasanDipercepat}%"
                denda_pelunasan_dipercepat.text = dendaPelunasanDipercepatText
            }

            override fun onNothingSelected(parent: MaterialSpinner) {

            }

        }

        kalkulasi_pinjaman.setOnClickListener {
            var allTrue = true
            if (product.selection < 0) {
                allTrue = false
                product.error = "Produk tidak boleh kosong"
            }
            if (jumlah_pinjaman.text.toString().isEmpty() || jumlah_pinjaman.text.toString() == "Rp0") {
                allTrue = false
                jumlah_pinjaman_container.error = "Jumlah Pinjaman tidak boleh kosong"
            }

            if (allTrue) {
                val intent = Intent(this, KalkulasiPinjamanActivity::class.java)
                intent.putExtra("member", member)
                intent.putExtra("product", selectedProduct)
                intent.putExtra("jumlah_pinjaman", CurrencyFormat.removeCurrencyFormat(jumlah_pinjaman.text.toString()).toLong())
                startActivity(intent)
            }
        }
    }
}