package com.android.id.peers.pembayaran

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.anggota.StatusPinjaman
import com.android.id.peers.pinjaman.pencairan.PencairanAdapter
import com.android.id.peers.pinjaman.pencairan.PencairanDetailActivity
import com.android.id.peers.util.callback.StatusPinjamanCallback
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.activity_pencairan.*
import kotlinx.android.synthetic.main.layout_shimmer.*

class PembayaranActivity : AppCompatActivity() {
    var pinjaman = ArrayList<StatusPinjaman>()
    lateinit var statusPinjamanAdapter : PembayaranAdapter
    var connected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        title = "Pembayaran Cicilan"

        pembayaran_cicilan.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        pembayaran_cicilan.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.line_separator_recycler_view))
        pembayaran_cicilan.addItemDecoration(dividerItemDecoration)

        statusPinjamanAdapter = PembayaranAdapter(pinjaman, this) { pinjaman -> pembayaranItemClickListener(pinjaman)}
        pembayaran_cicilan.adapter = statusPinjamanAdapter

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        if (connected) {
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmerAnimation()
            val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

            ApiConnections.authenticate(
                preferences,
                this,
                ApiConnections.REQUEST_TYPE_GET_PEMBAYARAN_CICILAN,
                object :
                    StatusPinjamanCallback {
                    override fun onSuccess(result: List<StatusPinjaman>) {
                        val newList = result.sortedByDescending { it.id }.distinctBy { it.idLoan }.sortedBy { it.id }
                        Log.d("StatusPinjaman", "LENGTH : ${result.size}")
                        pinjaman.clear()
                        pinjaman.addAll(newList)
                        Log.d("StatusPinjaman", "LENGTH(2) : ${pinjaman.size}")
                        statusPinjamanAdapter.notifyDataSetChanged()
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmerAnimation()
                        pembayaran_cicilan.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun pembayaranItemClickListener(pinjaman : StatusPinjaman) {
        val intent = Intent(this, PembayaranDetailActivity::class.java)
        intent.putExtra("id_member", pinjaman.idMember)
        intent.putExtra("angsuran", pinjaman.angsuran)
        startActivity(intent)
    }
}