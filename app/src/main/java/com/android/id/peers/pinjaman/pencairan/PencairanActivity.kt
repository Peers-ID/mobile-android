package com.android.id.peers.pinjaman.pencairan

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
import com.android.id.peers.anggota.PinjamanAdapter
import com.android.id.peers.anggota.StatusPinjaman
import com.android.id.peers.util.callback.StatusPinjamanCallback
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_pencairan.*
import kotlinx.android.synthetic.main.layout_shimmer.*

class PencairanActivity : AppCompatActivity() {
    var pinjaman = ArrayList<StatusPinjaman>()
    lateinit var statusPinjamanAdapter : PencairanAdapter
    var connected: Boolean = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencairan)

        title = "Pencairan Pinjaman"

        pencairan_pinjaman.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        pencairan_pinjaman.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.line_separator_recycler_view))
        pencairan_pinjaman.addItemDecoration(dividerItemDecoration)

        statusPinjamanAdapter = PencairanAdapter(pinjaman, this) { pinjaman -> pencairanItemClickListener(pinjaman)}
        pencairan_pinjaman.adapter = statusPinjamanAdapter

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
                ApiConnections.REQUEST_TYPE_GET_PENCAIRAN_PINJAMAN,
                object :
                    StatusPinjamanCallback {
                    override fun onSuccess(result: List<StatusPinjaman>) {
                        Log.d("StatusPinjaman", "LENGTH : ${result.size}")
                        pinjaman.clear()
                        pinjaman.addAll(result)
                        Log.d("StatusPinjaman", "LENGTH(2) : ${pinjaman.size}")
                        statusPinjamanAdapter.notifyDataSetChanged()
                        shimmer_view_container.visibility = View.GONE
                        shimmer_view_container.stopShimmerAnimation()
                        pencairan_pinjaman.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun pencairanItemClickListener(pinjaman : StatusPinjaman) {
        val intent = Intent(this, PencairanDetailActivity::class.java)
        intent.putExtra("id_pinjaman", pinjaman.id)
        startActivity(intent)
    }
}