package com.android.id.peers.anggota

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.util.callback.StatusPinjamanCallback
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_GET_PINJAMAN_MEMBER_STATUS
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import kotlinx.android.synthetic.main.activity_loan_disbursement.*
import kotlinx.android.synthetic.main.activity_status_pinjaman_anggota.*
import kotlinx.android.synthetic.main.layout_empty_data.*

class StatusPinjamanAnggotaActivity : AppCompatActivity() {
    var pinjaman = ArrayList<StatusPinjaman>()
    lateinit var statusPinjamanAdapter : PinjamanAdapter
    var connected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_pinjaman_anggota)

        title = "Status Pinjaman Anggota"

        status_pinjaman_anggota.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        status_pinjaman_anggota.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.line_separator_recycler_view))
        status_pinjaman_anggota.addItemDecoration(dividerItemDecoration)

        statusPinjamanAdapter = PinjamanAdapter(pinjaman, this)
        status_pinjaman_anggota.adapter = statusPinjamanAdapter

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        reload.setOnClickListener {
            error_container.visibility = View.GONE
            loadData()
        }

        if (connected) {
            loadData()
        }
    }

    private fun loadData() {
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmerAnimation()
        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

        authenticate(preferences, this, REQUEST_TYPE_GET_PINJAMAN_MEMBER_STATUS, object :
            StatusPinjamanCallback {
            override fun onSuccess(result: List<StatusPinjaman>) {
//                    pinjaman = ArrayList(result)
                Log.d("StatusPinjaman", "LENGTH : ${result.size}")
                pinjaman.clear()
                pinjaman.addAll(result)
                Log.d("StatusPinjaman", "LENGTH(2) : ${pinjaman.size}")
                statusPinjamanAdapter.notifyDataSetChanged()
                shimmer_view_container.visibility = View.GONE
                shimmer_view_container.stopShimmerAnimation()
                if (pinjaman.isEmpty()) {
                    error_container.visibility = View.VISIBLE
                    status_pinjaman_anggota.visibility = View.GONE
                } else {
                    error_container.visibility = View.GONE
                    status_pinjaman_anggota.visibility = View.VISIBLE
                }
            }
        })
    }
}