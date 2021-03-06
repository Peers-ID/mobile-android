package com.android.id.peers.simpanan

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.id.peers.R
import com.android.id.peers.util.callback.SimpananItemCallback
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_simpanan.*
import kotlinx.android.synthetic.main.layout_empty_data.*
import kotlinx.android.synthetic.main.layout_shimmer2.shimmer_view_container

class SimpananActivity : AppCompatActivity() {
    var memberItemList = ArrayList<Member>()
    var simpananList = ArrayList<Simpanan>()
    lateinit var simpananItemAdapter : SimpananItemAdapter
    var connected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simpanan)

        title = "Simpanan Anggota"

        simpananItemAdapter = SimpananItemAdapter(memberItemList)
        simpanan_item_rv.adapter = simpananItemAdapter

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

        memberItemList.clear()

        ApiConnections.authenticate(
            preferences,
            this,
            ApiConnections.REQUEST_TYPE_GET_SIMPANAN,
            object : SimpananItemCallback {
                override fun onSuccess(result: ArrayList<Simpanan>) {
                    val simpanan = ArrayList<Simpanan>(result)

                    if (result.size > 0) {
                        if (result[0].namaMember.isNotEmpty()) {
                            Log.d("SimpananActivity", "SIMPANAN : ${simpanan[0].simpananPokok}")
                            val member = Member(result[0].namaMember, simpanan)
                            memberItemList.add(member)
                            Log.d("SimpananActivity", "Member LIST SIZE : ${memberItemList.size}")
                            simpananItemAdapter.notifyDataSetChanged()
                        }
                        if (result[0].length == -1) {
                            shimmer_view_container.visibility = View.GONE
                            shimmer_view_container.stopShimmerAnimation()

                            if (memberItemList.isEmpty()) {
                                simpanan_item_rv.visibility = View.GONE
                                error_container.visibility = View.VISIBLE
                            } else {
                                simpanan_item_rv.visibility = View.VISIBLE
                                error_container.visibility = View.GONE
                            }
                        }
                    }
                }
            })
    }
}