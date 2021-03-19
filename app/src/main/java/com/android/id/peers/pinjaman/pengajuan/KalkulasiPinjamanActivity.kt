package com.android.id.peers.pinjaman.pengajuan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.id.peers.R
import com.android.id.peers.TermsActivity
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.CurrencyFormat
import kotlinx.android.synthetic.main.activity_kalkulasi_pinjaman.*
import java.util.*

class KalkulasiPinjamanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalkulasi_pinjaman)

        val paramPreferences = getSharedPreferences("parameter_koperasi", Context.MODE_PRIVATE)
        val hariPerbulan = paramPreferences.getInt("hari_per_bulan", 30)
//        var typeDendaKeterlambatan = paramPreferences.getString("type_denda_keterlambatan", "Fix")
        val idDasarDenda = paramPreferences.getInt("id_dasar_denda", 0)
        val typePelunasanDipercepat = paramPreferences.getString("type_pelunasan_dipercepat", "Fix")
        val idDasarPelunasan = paramPreferences.getInt("id_dasar_pelunasan", 0)

        title = "Konfirmasi Pinjaman"

        val member = intent.getParcelableExtra<Member>("member")!!
        val product = intent.getParcelableExtra<Product>("product")!!
        val jumlahPinjaman = intent.getLongExtra("jumlah_pinjaman", 0)

        Log.d("KalkulasiPinjamanActivity", "NO HP : ${member.noHp}")

        nama_product.text = product.namaProduct
        jumlah_pinjaman.text = jumlahPinjaman.toString()
        val tenorText = "${product.tenor} ${product.satuanTenor}"
        tenor.text = tenorText
        val bungaText = "${product.bunga}% / ${product.tenorBunga}"
        bunga.text = bungaText

        var pengaliTenor = 0
        when (product.satuanTenor.toLowerCase(Locale.ROOT)) {
            "bulan" -> pengaliTenor = 1
            "minggu" -> pengaliTenor = 4
            "hari" -> pengaliTenor = hariPerbulan
        }

        var pengaliBunga = 0
        when (product.tenorBunga.toLowerCase(Locale.ROOT)) {
            "bulan" -> pengaliBunga = 1
            "minggu" -> pengaliBunga = 4
            "hari" -> pengaliBunga = hariPerbulan
        }

        val totalBunga : Long = (product.bunga * product.tenor * pengaliBunga / pengaliTenor * jumlahPinjaman / 100).toLong()
        Log.d("KalkulasiPinjamanActivity", "BUNGA : ${product.bunga}, TENOR : ${product.tenor}, PENGALI BUNGA : ${pengaliBunga}, PENGALI TENOR : ${pengaliTenor}, JUMLAH PINJAMAN : $jumlahPinjaman")
        Log.d("KalkulasiPinjamanActivity", "TOTAL BUNGA : $totalBunga")
        val totalPinjaman = jumlahPinjaman + totalBunga

        val cicilanPerTenor = totalPinjaman / product.tenor
        var cicilanPerBulan : Long = 0
        when {
            product.satuanTenor.toLowerCase(Locale.ROOT) == "bulan" -> cicilanPerBulan = cicilanPerTenor
            product.satuanTenor.toLowerCase(Locale.ROOT) == "minggu" -> cicilanPerBulan = if (product.tenor >= 4)
                                                                            cicilanPerTenor * 4
                                                                        else
                                                                            cicilanPerTenor * product.tenor
            product.satuanTenor.toLowerCase(Locale.ROOT) == "hari" -> cicilanPerBulan = if (product.tenor >= hariPerbulan)
                                                                            cicilanPerTenor * hariPerbulan
                                                                        else
                                                                            cicilanPerTenor * product.tenor
        }

        val pokokPerTenor = jumlahPinjaman / product.tenor
        var pokokPerBulan : Long = 0
        when {
            product.satuanTenor.toLowerCase(Locale.ROOT) == "bulan" -> pokokPerBulan = pokokPerTenor
            product.satuanTenor.toLowerCase(Locale.ROOT) == "minggu" -> pokokPerBulan = if (product.tenor >= 4)
                pokokPerTenor * 4
            else
                pokokPerTenor * product.tenor
            product.satuanTenor.toLowerCase(Locale.ROOT) == "hari" -> pokokPerBulan = if (product.tenor >= hariPerbulan)
                pokokPerTenor * hariPerbulan
            else
                pokokPerTenor * product.tenor
        }
        val bungaPerBulan = cicilanPerBulan - pokokPerBulan

        Log.d("KalkulasiPinjamanActivity", "Pokok Per Bulan $bungaPerBulan, Bunga Per Bulan $bungaPerBulan")

        val biayaAdmin = if (product.typeAdmin.toLowerCase(Locale.ROOT) == "fix") product.admin else product.admin * jumlahPinjaman / 100
        val adminText =  CurrencyFormat.formatRupiah.format(biayaAdmin)
        admin.text = adminText

        val biayaProvisi = if (product.typeProvisi.toLowerCase(Locale.ROOT) == "fix") product.provisi else product.provisi * jumlahPinjaman / 100
        val provisiText = CurrencyFormat.formatRupiah.format(biayaProvisi)
        provisi.text = provisiText

        simpanan_pokok.text = CurrencyFormat.formatRupiah.format(product.simpananPokok)
        simpanan_wajib.text = CurrencyFormat.formatRupiah.format(product.simpananWajib)
        val pengaliDendaKeterlambatan = if (idDasarDenda == 1) totalPinjaman else jumlahPinjaman
        val dendaKeterlambatan = if (product.typeDendaKeterlambatan.toLowerCase(Locale.ROOT) == "fix") product.dendaKeterlambatan else product.dendaKeterlambatan * pengaliDendaKeterlambatan / 100
        val dendaKeterlambatanText = CurrencyFormat.formatRupiah.format(dendaKeterlambatan)
        denda_keterlambatan.text = dendaKeterlambatanText

        val pengaliPelunasanDipercepat = if (idDasarPelunasan == 1) totalPinjaman else jumlahPinjaman
        val dendaPelunasanDipercepat = if (typePelunasanDipercepat!!.toLowerCase(Locale.ROOT) == "fix") product.pelunasanDipercepat else product.pelunasanDipercepat * pengaliPelunasanDipercepat / 100
        val dendaPelunasanDipercepatText = CurrencyFormat.formatRupiah.format(dendaPelunasanDipercepat)
        denda_pelunasan_dipercepat.text = dendaPelunasanDipercepatText

        val biayaAsuransi = if (product.typeAsuransi.toLowerCase(Locale.ROOT) == "fix") product.asuransi else product.asuransi * jumlahPinjaman / 100
        val danaJpk = if (product.typeJpk.toLowerCase(Locale.ROOT) == "fix") product.jpk else product.jpk * jumlahPinjaman / 100

        asuransi.text = CurrencyFormat.formatRupiah.format(biayaAsuransi)
        jpk.text = CurrencyFormat.formatRupiah.format(danaJpk)

        val pengurang = (biayaAdmin + biayaProvisi + product.simpananPokok + biayaAsuransi + danaJpk).toLong()

        val jumlahPencairan = jumlahPinjaman - pengurang
        jumlah_pencairan.text = CurrencyFormat.formatRupiah.format(jumlahPencairan)
        cicilan.text = CurrencyFormat.formatRupiah.format(cicilanPerBulan)

        ajukan.setOnClickListener {
            val pinjaman = Pinjaman()
            pinjaman.idProduct = product.id
            pinjaman.jumlahPengajuan = jumlahPinjaman
            pinjaman.jumlahPencairan = jumlahPencairan
            pinjaman.jumlahCicilan = cicilanPerBulan
            pinjaman.utangPokok = pokokPerBulan
            pinjaman.bungaPinjaman = bungaPerBulan

            val intent = Intent(this, TermsActivity::class.java)
            intent.putExtra("member", member)
            intent.putExtra("pinjaman", pinjaman)
            startActivity(intent)
        }
    }
}